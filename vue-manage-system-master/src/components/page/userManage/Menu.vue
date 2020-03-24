<template>
    <div>
        <div class="container">
            <div>
                <el-button type="primary" @click="expandAll()"  size="small">全部展开</el-button>
                <el-button type="primary" @click="closeAll()"  size="small">全部折叠</el-button>
                <el-button type="primary" class="el-icon-lx-roundaddfill" @click="handleAdd()">新增</el-button>
            </div>
            <div>
                <el-table :data="tableData" style="width: 100%">
                    <TableTreeColumn ref="c1" prop="name" treeKey="id"  label="资源名称">
                    </TableTreeColumn>
                    <el-table-column prop="url" label="资源路径"></el-table-column>
                    <el-table-column prop="perms" label="权限编码"></el-table-column>
                    <el-table-column prop="type" label="资源类型"></el-table-column>
                    <el-table-column prop="orderNum" label="排序号"></el-table-column>
                    <el-table-column label="操作" width="280">
                        <template slot-scope="scope">
                            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                            <el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>

        <!-- 新增弹出框 -->
        <el-dialog title="新增" :visible.sync="addVisible" width="30%">
            <el-form :model="form" :rules="rules" ref="ruleForm" label-width="80px">
                <el-form-item label="上级资源">
                    <el-select v-model="form.parentId" placeholder="请选择上级资源" style="width: 100%">
                        <el-option v-for="(item,index) in menuList" :key="index" :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="name" label="资源名称">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item  prop="type" label="资源类型">
                    <el-select v-model="form.type" placeholder="请选择资源类型" style="width: 100%">
                        <el-option v-for="(item,index) in typeList" :key="index" :label="item.name" :value="item.type"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="url" label="资源路径">
                    <el-input v-model="form.url"></el-input>
                </el-form-item>
                <el-form-item prop="perms" label="权限编码">
                    <el-input v-model="form.perms"></el-input>
                </el-form-item>
                <el-form-item prop="orderNum" label="排序号">
                    <el-input v-model="form.orderNum"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveAdd">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form :model="form" :rules="rules" ref="ruleForm" label-width="80px">
                <el-form-item label="上级资源">
                    <el-select v-model="form.parentId" placeholder="请选择上级资源" style="width: 100%">
                        <el-option v-for="(item,index) in menuList" :key="index" :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="name" label="资源名称">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item  prop="type"  label="资源类型">
                    <el-select v-model="form.type" placeholder="请选择资源类型" style="width: 100%">
                        <el-option v-for="(item,index) in typeList" :key="index" :label="item.name" :value="item.type"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="url" label="资源路径">
                    <el-input v-model="form.url"></el-input>
                </el-form-item>
                <el-form-item prop="perms" label="权限编码">
                    <el-input v-model="form.perms"></el-input>
                </el-form-item>
                <el-form-item  prop="orderNum" label="排序号">
                    <el-input v-model="form.orderNum"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
    import TableTreeColumn from '@/components/common/tableTree/tableTreeColumn.vue'
    import {getMenuList,getMenuById,editMenu,addMenu} from  '@/api/system'
    export default {
        components: {
            TableTreeColumn,
        },
        data () {
            const isNum = (rule, value, callback) => {
                const age= /^[0-9]*$/
                if (!age.test(value)) {
                    callback(new Error('年龄只能为数字'))
                }else{
                    callback()
                }
            };
            return {
                tableData:null,//列表显示数据
                menuList : [],
                editVisible:false,
                addVisible:false,
                form: {},
                idx: -1,
                id: -1,
                typeList:[{type:0,name:'目录'},{type:1,name:'菜单'},{type:2,name:'资源'}],
                rules: {
                    name: [{ required: true, message: '请输入资源名称', trigger: 'blur' }],
                    url: [{ required: true, message: '请输入资源路径', trigger: 'blur' }],
                    type: [{ required: true, message: '请输入资源类型', trigger: 'blur' }],
                    perms: [{ required: true, message: '请输入权限编码', trigger: 'blur' }],
                    orderNum: [{ required: true, message: '请输入排序号', trigger: 'blur' },
                        { validator: isNum, message: '排序号只能为数字', trigger: 'blur' }],
                },
            }
        },
        created: function(){
            this.getData();
        },
        methods: {
            getData() {
                var _this = this;
                //获取菜单数据
                /*const menus=[//简单模拟一下数据 typ[0上级菜单，1页面菜单]
                    {id: 1, parentId: null, name: "系统管理", url: "sys",type:0},
                    {id: 2, parentId: 1, name: "账号管理", url: "sys/account",type:1},
                    {id: 9, parentId: 2, name: "账号管理1", url: "sys/account",type:2},
                    {id: 3, parentId: 1, name: "菜单管理", url: "sys/menu",type:1},
                    {id: 4, parentId: 1, name: "角色管理", url: "sys/role",type:1},
                    {id: 6, parentId: null, name: "系统管理", url: "sys",type:0},
                    {id: 7, parentId: 6, name: "角色管理1", url: "sys/role",type:1},
                    {id: 8, parentId: 7, name: "角色管理2", url: "sys/role",type:2},
                ];
                 this.menuList = menus;
                 //this.tableData =this.toTree(menus)
                */
                getMenuList().then(res => {
                    if (res.flag) {
                        _this.menuList = res.data;
                        _this.tableData = _this.parseTree(_this.menuList);
                        //对树状数据按orderNum进行排序
                        _this.tableData = _this.orderNodes(_this.tableData);
                    } else {
                        _this.$message.error(res.message);
                    }
                });
            },
            /* toTree(data){//转换为数据结构-自己封装吧。
                data.forEach((item)=>{//删除所有children,以防止多次调用
                    delete item.children;
                });
                var map = {};// 将数据存储为以id为 KEY的 map索引数据列
                data.forEach((item)=> {
                    map[item.id] = item;
                });
                var val = [];
                data.forEach((item)=> {
                    var parent = map[item.parentId]; //以当前遍历项，的parentId,去map对象中找到索引的id
                    if(parent){//如果找到索引，说明此项不在顶级当中,那么需要把此项添加到，他对应的父级中
                        (parent.children || ( parent.children = [] )).push(item);
                    }else{
                        val.push(item);//如果没有在map中找到对应的索引ID,那么直接把 当前的item添加到 val结果集中，作为顶级
                    }
                });
                return val;
            },*/
            parseTree(data) {
                var treeList = [];
                data.forEach((item) => {
                    //说明为父级
                    if (!item.parentId) {
                        //父级边距设为0
                        item.level = 0;
                        treeList.push(item);
                    }
                });
                treeList.forEach((parent) => {
                    this.recursiveTree(parent, data, 0);
                });
                return treeList;
            },
            recursiveTree(parent, data, level) {
                data.forEach((item) => {
                    if (item.parentId == parent.id) {
                        //子级边距+2
                        item.level = level + 2;
                        item = this.recursiveTree(item, data, item.level);
                        (parent.children || (parent.children = [])).push(item);
                    }
                });
                return parent;
            },
            //递归对树形树状按orderNum字段进行排序
            orderNodes(data){
                for( var i =0; i< data.length-1; i++) {
                    for( var j =0; j< data. length- i-1; j++) {
                        //比较前一个元素与后一个元素的大小，将每趟中最大的元素放在末尾，
                        // 末尾的元素再下一趟  不用再比较，所以 是  j< arr. length- i-1
                        //交换前后两个元素的位置
                        if( data[ j].orderNum> data[ j+1].orderNum) {
                            var temp = data[j];
                            data[ j]= data[ j+1];
                            data[ j+1]= temp;
                        }
                    }
                }
                data.forEach((item) => {
                    this.orderChildren(item.children);
                });
                return data;
            },
            orderChildren(data){
                debugger
                for( var i =0; i< data.length-1; i++) {
                    for (var j = 0; j < data.length - i - 1; j++) {
                        //比较前一个元素与后一个元素的大小，将每趟中最大的元素放在末尾，
                        // 末尾的元素再下一趟  不用再比较，所以 是  j< arr. length- i-1
                        //交换前后两个元素的位置
                        if (data[j].orderNum > data[j + 1].orderNum) {
                            var temp = data[j];
                            data[j] = data[j + 1];
                            data[j + 1] = temp;
                        }
                    }
                }
                data.forEach((item) => {
                    debugger
                    if(item.children&&item.children.length>0){
                        this.orderChildren(item.children)
                    }
                });
                return data;
            },
            //全部展开
            expandAll() {
                this.$refs.c1.expandedAll(this.menuList);
            },
            //全部折叠
            closeAll() {
                var foldList = [];
                this.menuList.forEach((menu) => {
                    if (!menu.parentId) {
                        foldList.push(menu);
                    }
                });
                this.$refs.c1.closeAll(foldList);
            },
            //新增窗口
            handleAdd() {
                this.form = {};
                this.editVisible = false;
                this.addVisible = true;
            },
            //编辑窗口
            handleEdit(index, row) {
                this.idx = index;
                this.form = row;
                getMenuById(row.id).then(res=>{
                    if(res.flag){
                        this.from = res.data;
                        this.addVisible = false;
                        this.editVisible = true;
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            //新增操作
            saveAdd() {
                this.$refs.ruleForm.validate(valid => {
                    if (valid) {
                        addMenu(this.form).then(res=>{
                            if (res.flag) {
                                this.$message.success(res.message);
                            }else {
                                this.$message.error(res.message);
                                console.log(res.message);
                                return false;
                            }
                            this.addVisible = false;
                            this.getData();
                        })
                    } else {
                        this.$message.error('请输入角色名称');
                        return false;
                    }
                });
            },
            //编辑操作
            saveEdit() {
                this.$refs.ruleForm.validate(valid => {
                    if (valid) {
                        editMenu(this.form).then(res=>{
                            if (res.flag) {
                                this.$message.success(res.message);
                            }else {
                                this.$message.error(res.message);
                                console.log(res.message);
                                return false;
                            }
                            this.editVisible = false;
                            this.getData();
                        });
                    } else {
                        this.$message.error('请输入角色名称');
                        return false;
                    }
                });
            },
        }
    }
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }
    .table {
        width: 100%;
        font-size: 14px;
    }
    .red {
        color: #ff0000;
    }
    .mr10 {
        margin-right: 10px;
    }
    .table-td-thumb {
        display: block;
        margin: auto;
        width: 40px;
        height: 40px;
    }
</style>
