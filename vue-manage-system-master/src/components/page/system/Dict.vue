<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i>数据字典管理
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-container class="container" style="border: 1px solid #eee">
            <div style="width:25%;">
                <div>
                    <el-input style="width:200px;"
                              placeholder="输入关键字进行过滤"
                              v-model="filterText">
                    </el-input>

                    <el-button type="primary" icon="el-icon-add" class="el-icon-lx-roundaddfill"
                               @click="saveHandle">新增
                    </el-button>
                </div>


                <el-tree style="background-color: rgb(238, 241, 246)"
                         class="filter-tree"
                         :data="treeData"
                         :props="defaultProps"
                         default-expand-all
                         :filter-node-method="filterNode"
                         @node-contextmenu="rightClick"
                         @node-click="handleNodeClick"
                         ref="tree">
                             <span class="custom-tree-node" slot-scope="{ node, data }">
                                <span>{{ node.label }}</span>
                                    <span>
                                      <el-button type="text" size="mini" @click="() => viewDict(data)" style="color: #303133">
                                        <i class="el-icon-lx-info"></i>
                                      </el-button>
                                      <el-button type="text" size="mini" @click="() => edit(node, data)" style="color: #303133">
                                         <i class="el-icon-lx-tag"></i>
                                      </el-button>
                                        <el-button type="text" size="mini" @click="() => remove(node, data)"  style="color: #303133">
                                            <i class="el-icon-lx-delete"></i>
                                      </el-button>
                                </span>
                          </span>
                </el-tree>
            </div>

            <!--tabs表格-->
            <el-container>
                    <div style="width: 90%">
                        <div class="handle-box">
                            <el-input v-model="query.code" placeholder="请输入字典编码" class="handle-input mr10"style="width: 25%"></el-input>
                            <el-input v-model="query.name" placeholder="请输入字典名称" class="handle-input mr10" style="width: 25%"></el-input>
                            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                            <el-button type="primary" icon="el-icon-add" class="el-icon-lx-roundaddfill"
                                       @click="handleAdd()">新增
                            </el-button>
                            <el-button type="primary" icon="el-icon-delete" class="handle-del mr10" @click="delAllSelection()">批量删除</el-button>
                        </div>
                        <el-table
                                :data="tableData"
                                border
                                class="table"
                                ref="multipleTable"
                                @selection-change="handleSelectionChange"
                                header-cell-class-name="table-header">
                            <el-table-column type="selection" width="55" align="center"></el-table-column>
                            <el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
                            <el-table-column prop="name" label="字典名称" align="center"></el-table-column>
                            <el-table-column prop="code" label="字典编码" align="center"></el-table-column>
                            <el-table-column prop="value" label="字典值"  width="130" align="center"></el-table-column>
                            <el-table-column prop="remark" label="备注" width="100"align="center"></el-table-column>
                            <el-table-column prop="orderNum" label="排序号" width="150" align="center"></el-table-column>
                            <el-table-column label="操作" width="200" align="center">
                                <template slot-scope="scope">
                                    <el-button type="text" icon="el-icon-edit"
                                               @click="handleEdit(scope.$index, scope.row)">编辑
                                    </el-button>
                                    <el-button type="text" icon="el-icon-delete" class="red"
                                               @click="handleDelete(scope.$index, scope.row)">删除
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="pagination">
                            <el-pagination
                                    background
                                    layout="total, prev, pager, next"
                                    :current-page="query.pageIndex"
                                    :page-size="query.pageSize"
                                    :total="pageTotal"
                                    @current-change="handlePageChange"></el-pagination>
                        </div>

                    </div>
            </el-container>
        </el-container>

        <!-- 新增弹出框 -->
        <el-dialog title="新增" :visible.sync="addDictVisible" width="30%">
            <el-form :model="form" :rules="rules" ref="rulesForm" label-width="80px">
                <el-form-item prop="name" label="字典名称">
                    <el-input  placeholder="请输入字典名称" v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item prop="code" label="字典编码">
                    <el-input placeholder="请输入字典编码" v-model="form.code"></el-input>
                </el-form-item>
                <el-form-item prop="value" label="字典值">
                    <el-input placeholder="请输入字典值" v-model="form.value"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input placeholder="请输入备注" v-model="form.remark" ></el-input>
                </el-form-item>
                <el-form-item prop="orderNum" label="排序号">
                    <el-input placeholder="请输入排序号" v-model="form.orderNum"></el-input>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="addDictVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveAddDict">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 新增弹出框 -->
        <el-dialog title="新增" :visible.sync="addVisible" width="30%">
            <el-form :model="form" :rules="rules" ref="rulesForm" label-width="80px">
                <el-form-item prop="name" label="字典名称">
                    <el-input  placeholder="请输入字典名称" v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item prop="code" label="字典编码">
                    <el-input placeholder="请输入字典编码" v-model="form.code"></el-input>
                </el-form-item>
                <el-form-item prop="value" label="字典值">
                    <el-input placeholder="请输入字典值" v-model="form.value"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input placeholder="请输入备注" v-model="form.remark" ></el-input>
                </el-form-item>
                <el-form-item prop="orderNum" label="排序号">
                    <el-input placeholder="请输入排序号" v-model="form.orderNum"></el-input>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveAdd">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form :model="form" :rules="rules" ref="rulesForm" label-width="80px">
                <el-form-item prop="name" label="字典名称">
                    <el-input  placeholder="请输入字典名称" v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item prop="code" label="字典编码">
                    <el-input placeholder="请输入字典编码" v-model="form.code"></el-input>
                </el-form-item>
                <el-form-item prop="value" label="字典值">
                    <el-input placeholder="请输入字典值" v-model="form.value"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input placeholder="请输入备注" v-model="form.remark"></el-input>
                </el-form-item>
                <el-form-item prop="orderNum" label="排序号">
                    <el-input placeholder="请输入排序号" v-model="form.orderNum"></el-input>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
             </span>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="viewDictVisible" width="30%">
            <el-form :model="form" :rules="rules" ref="rulesForm" label-width="80px">
                <el-form-item label="字典名称">
                    <el-input  placeholder="请输入字典名称" v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="字典编码">
                    <el-input placeholder="请输入字典编码" v-model="form.code"></el-input>
                </el-form-item>
                <el-form-item label="字典值">
                    <el-input placeholder="请输入字典值" v-model="form.value"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input placeholder="请输入备注" v-model="form.remark"></el-input>
                </el-form-item>
                <el-form-item label="排序号">
                    <el-input placeholder="请输入排序号" v-model="form.orderNum"></el-input>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>

    import {getDictsByParentId,gerDictById,addDict,editDict,delDictsByIds,delDictById,getDictList} from '@/api/system'
    export default {
        data() {
            return {
                filterText: '',
                query: {
                    name: '',
                    code: '',
                    parentId:'',
                    pageIndex: 1,
                    pageSize: 10
                },
                dictId:'',
                pageTotal: 0,
                tableData: [],
                form: {},
                treeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                editVisible: false,
                addVisible: false,
                addDictVisible:false,
                viewDictVisible:false,
                ids:[],
                rules : {
                    name: [{ required: true, message: '请输入字典名称', trigger: 'blur' }],
                    code: [{ required: true, message: '请输入字典编码', trigger: 'blur' }],
                    value: [{ required: true, message: '请输入字典值', trigger: 'blur' }],
                    orderNum: [{ required: true, message: '请输入排序号', trigger: 'blur' }],
                }
            };
        },
        created() {
            this.getTreeData();
        },
        watch: {
            filterText(val) {
                this.$refs.tree.filter(val);
            }
        },
        methods: {
            filterNode(value, data) {
                if (!value) return true;
                return data.name.indexOf(value) !== -1;
            },
            getTreeData() {
                getDictsByParentId('').then(res => {
                    if (res.flag) {
                        var dicts = res.data;
                        var i = 1;
                        dicts.forEach((item)=>{
                            item.name = i+'、'+item.name+"("+item.code+")";
                            i+=1;
                        });
                        this.treeData = dicts;
                    } else {
                        this.$message.error(res.message);
                    }
                });
            },
            gettableData() {
                getDictList(this.query).then(res => {
                    this.tableData = res.rows;
                    this.pageTotal = res.total;
                });
            },
            //被鼠标点击
            handleNodeClick(data) {
                console.log(data);
                this.dictId = data.id;//赋值
                this.query.parentId = data.id;
                //获取父id,获取子节点
                /*getDictsByParentId(this.dictId).then(res => {
                    if (res.flag) {
                        this.tableData = res.data;
                    } else {
                        this.$message.error(res.message);
                    }
                });*/
                debugger
                this.gettableData();
            },
            rightClick(MouseEvent, object, Node, element) { // 鼠标右击触发事件
                this.dictId = object.id;
                this.query.parentId = data.id;
                console.log('右键被点击的event:', MouseEvent);
                console.log('右键被点击的object:', object);
                console.log('右键被点击的value:', Node);
                console.log('右键被点击的element:', element);
                console.log('鼠标点击了树形结构图');
                //获取父id,获取子节点
                /*getDictsByParentId(this.dictId).then(res => {
                    if (res.flag) {
                        this.tableData = res.data;
                    } else {
                        this.$message.error(res.message);
                    }
                });*/
                this.gettableData();
            },
            //触发搜索按钮
            handleSearch() {
                this.$set(this.query, 'pageIndex', 1);
                this.gettableData();
            },
            //分页导航
            handlePageChange(val) {
                this.$set(this.query, 'pageIndex', val);
                this.gettableData();
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
                this.userForm = row;
                gerDictById(row.id).then(res => {
                    if (res.flag) {
                        this.form = res.data;
                        this.addVisible = false;
                        this.editVisible = true;
                    } else {
                        this.$message.error(res.message);
                    }
                });
            },
            //新增操作
            saveAdd() {
                this.form.parentId = this.dictId;//设置父节点id
                if(!this.validaData()){
                    return;
                }
                addDict(this.form).then(res => {
                    if (res.flag) {
                        this.$message.success("新增成功");
                        this.addVisible = false;
                        this.getTreeData();
                        this.gettableData();
                    } else {
                        this.$message.error(res.message);
                        console.log(res.message);
                        return false;
                    }
                });
            },
            //编辑操作
            saveEdit() {
                if(!this.validaData()){
                    return false;
                }
                editDict(this.form).then(res => {
                    if (res.flag) {
                        this.$message.success("修改成功");
                        this.editVisible = false;
                        this.getTreeData();
                        this.gettableData();
                    } else {
                        this.$message.error(res.message);
                        console.log(res.message);
                        return false;
                    }
                });
            },
            validaData(){
                if(!this.form.name){
                    this.$message.error("请输入字典名称")
                    return false;
                }
                if(!this.form.code){
                    this.$message.error("请输入字典编码")
                    return false;
                }
                if(!this.form.value){
                    this.$message.error("请输入字典值")
                    return false;
                }
                if(!this.form.orderNum){
                    this.$message.error("请输入排序号")
                    return false;
                }
                return true;
            },
            handleDelete(index, row) {
                this.$confirm('确定要批量删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    delDictById(row.id).then(res => {
                        if (res.flag) {
                            this.$message.success('删除成功');
                            this.getTreeData();
                            this.gettableData();
                        } else {
                            this.$message.error(res.message);
                        }
                    });
                }).catch(() => {
                });
            },
            //多选操作
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            delAllSelection() {
                const length = this.multipleSelection.length;
                let str = '';
                for (let i = 0; i < length; i++) {
                    str += this.multipleSelection[i].name + ',';
                    this.ids[i] = this.multipleSelection[i].id;
                }
                if (length>0){
                    //二次确认删除
                    this.$confirm('确定要批量删除吗？', '提示', {
                        type: 'warning'
                    }).then(() => {
                        delDictsByIds(this.ids).then(res=>{
                            if(res.flag){
                                this.$message.success(`删除了${str}`);
                                this.multipleSelection = [];
                                this.getTreeData();
                            }else {
                                this.$message.error(res.message);
                            }
                        });
                    }).catch(() => {});
                } else {
                    this.$message.error("请选择勾选要删除数据")
                }
            },
            remove(node, data){
                this.$confirm('确定要批量删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    delDictById(data.id).then(res=>{
                        if(res.flag){
                            this.$message.success("删除了"+data.name)
                            this.getTreeData();
                        }else {
                            this.$message.error(res.message)
                        }
                    })
                })
            },
            edit(node, data){
                this.form = {};
                gerDictById(data.id).then(res=>{
                    if(res.flag){
                        this.form = res.data;
                        this.editVisible=true;
                    }else {
                        this.$message.error(res.message)
                    }
                })
            },
            saveHandle(node,data){
                this.form = {};
                this.addDictVisible = true;
            },
            saveAddDict(){
                if(!this.validaData()){
                    return;
                }
                addDict(this.form).then(res => {
                    if (res.flag) {
                        this.$message.success("新增成功");
                        this.addDictVisible = false;
                        this.getTreeData();
                        this.gettableData()
                    } else {
                        this.$message.error(res.message);
                        console.log(res.message);
                        return false;
                    }
                });
            },
            viewDict(data){
                gerDictById(data.id).then(res=>{
                    if(res.flag){
                        this.form = res.data;
                        this.viewDictVisible=true;
                    }else {
                        this.$message.error(res.message)
                    }
                })
            },
        }
    };
</script>

<style scoped>
    .el-header {
        background-color: #B3C0D1;
        color: #333;
        line-height: 60px;
    }

    .el-aside {
        color: #333;
    }

    .menu__item {
        display: block;
        line-height: 20px;
        text-align: center;
        margin-top: 10px;
    }

    .menu {
        height: 100px;
        width: 100px;
        position: absolute;
        border-radius: 10px;
        border: 1px solid #999999;
        background-color: #f4f4f4;
    }

    li:hover {
        background-color: #1790ff;
        color: white;
    }

    .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;
    }
</style>
