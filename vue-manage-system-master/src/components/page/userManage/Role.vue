<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 角色管理
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="query.role_name" placeholder="角色名称" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                <el-button type="primary" icon="el-icon-add" class="el-icon-lx-roundaddfill" @click="handleAdd()">新增</el-button>
                <el-button type="primary" icon="el-icon-delete" class="handle-del mr10" @click="delAllSelection()">批量删除</el-button>
            </div>
            <el-table
                    :data="tableData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="roleId" label="ID" width="55" align="center"></el-table-column>
                <el-table-column prop="industryCategory" label="行业类型" align="center"></el-table-column>
                <el-table-column prop="roleName" label="角色名称" align="center"></el-table-column>
                <el-table-column prop="remark" label="备注" align="center"></el-table-column>
                <el-table-column prop="createTime" :formatter="formatDate" label="创建时间" align="center"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button type="text" class="el-icon-lx-share" @click="handlePermiss(scope.$index, scope.row)">权限</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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
        <!-- 新增弹出框 -->
        <el-dialog title="新增" :visible.sync="addVisible" width="30%">
            <el-form :model="form" :rules="rules" ref="ruleForm" label-width="80px">
                <el-form-item  prop="industryCategory"  label="行业类型">
                    <!--@click.native="queryIndustryCategoryList('industryCategory')"-->
                    <el-select  v-model="form.industryCategory" placeholder="请选择行业类型" style="width: 100%" >
                        <el-option v-for="(item,index) in industryCategoryList" :key="index" :label="item.name" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="roleName" label="角色名称">
                    <el-input v-model="form.roleName"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveAdd">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form  :model="form" :rules="rules" ref="ruleForm" label-width="80px">
                <el-form-item  prop="industryCategory" label="行业类型">
                    <!--@click.native="queryIndustryCategoryList('industryCategory')"-->
                    <el-select  v-model="form.industryCategory" placeholder="请选择行业类型" style="width: 100%" >
                        <el-option v-for="(item,index) in industryCategoryList" :key="index" :label="item.name" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="roleName" label="角色名称">
                    <el-input v-model="form.roleName"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 权限弹出框 -->
        <el-dialog title="权限" :visible.sync="permissVisible" width="30%">
            <el-form  :model="form" :rules="rules" ref="ruleForm" label-width="80px">
                <div class="buttons">
                    <!--<el-button @click="getCheckedNodes">通过 node 获取</el-button>
                    <el-button @click="getCheckedKeys">通过 key 获取</el-button>
                    <el-button @click="setCheckedNodes">通过 node 设置</el-button>
                    <el-button @click="setCheckedKeys">通过 key 设置</el-button>
                    <el-button @click="resetChecked">清空</el-button>-->
                    <el-button type="primary" @click="checkedAll"  size="small">全选</el-button>
                    <el-button type="primary" @click="resetChecked"  size="small">清空</el-button>
                </div>

                <el-tree
                        :data="perms"
                        show-checkbox
                        default-expand-all
                        node-key="id"
                        ref="tree"
                        highlight-current
                        :props="defaultProps">
                </el-tree>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="permissVisible = false">取 消</el-button>
                <el-button type="primary" @click="savePerms">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import {getRoleList,addRole,getRoleById,editRole,delRole,delRoleByIds,getMenuTreeByRoleId,saveRolePerms,getDictsByParentCode} from '@/api/system'
    export default {
        data:function(){
            var validaIndustryCategory = function(rule, value, callback) {
                if (!this.form.industryCategory) {
                    callback(new Error('请选择行业类型'));
                }
            };
            return {
                query: {
                    roleName: '',
                    pageIndex: 1,
                    pageSize: 10
                },
                tableData: [],
                multipleSelection: [],
                delList: [],
                addVisible: false,
                editVisible: false,
                permissVisible:false,
                pageTotal: 0,
                form: {},
                idx: -1,
                id: -1,
                ids:[],
                menus:[],
                industryCategoryList:[],
                rules: {
                    industryCategory: [{ validator: validaIndustryCategory, trigger: 'blur' }],
                    roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
                },
                //权限
                roleId:'',
                permsIds:[],
                perms: [/*{
                    id: 1,
                    name: '一级 1',
                    children: [{
                        id: 4,
                        name: '二级 1-1',
                        children: [{
                            id: 9,
                            name: '三级 1-1-1'
                        }, {
                            id: 10,
                            name: '三级 1-1-2'
                        }]
                    }]
                }, {
                    id: 2,
                    name: '一级 2',
                    children: [{
                        id: 5,
                        name: '二级 2-1'
                    }, {
                        id: 6,
                        name: '二级 2-2'
                    }]
                }, {
                    id: 3,
                    name: '一级 3',
                    children: [{
                        id: 7,
                        name: '二级 3-1'
                    }, {
                        id: 8,
                        name: '二级 3-2'
                    }]
                }*/],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                }
            };
        },
        //刷新表格数据
        created() {
            this.getData();
        },
        methods: {
            formatDate (row, column){
                //获取单元格数据
                let data = row[column.property];
                if(data == null) {
                    return null
                }
                let dt = new Date(data)
                return dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + ' ' + dt.getHours() + ':' + dt.getMinutes() + ':' + dt.getSeconds()
            },
            getData() {
                getRoleList(this.query).then(res=>{
                    this.tableData = res.rows;
                    this.pageTotal = res.total;
                })
                //查询行业类型下拉框
                getDictsByParentCode('industryCategory').then(res=>{
                    if(res.flag){
                        this.industryCategoryList = res.data;
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            //触发搜索按钮
            handleSearch() {
                this.$set(this.query, 'pageIndex', 1);
                this.getData();
            },
            //删除操作
            handleDelete(index, row) {
                //二次确认删除
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                        delRole(row.roleId).then(res=>{
                            if(res.flag){
                                this.$message.success(res.message);
                            }else {
                                this.$message.error(res.message);
                            }
                        });
                        this.getData();
                    }).catch(() => {});
            },
            //多选操作
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            delAllSelection() {
                const length = this.multipleSelection.length;
                let str = '';
                this.delList = this.delList.concat(this.multipleSelection);
                for (let i = 0; i < length; i++) {
                    str += this.multipleSelection[i].role_name + ',';
                    this.ids[i] = this.multipleSelection[i].roleId;
                }
                if (length>0){
                    //二次确认删除
                    this.$confirm('确定要批量删除吗？', '提示', {
                        type: 'warning'
                    }).then(() => {
                        delRoleByIds(this.ids).then(res=>{
                            if(res.flag){
                                this.$message.success(`删除了${str}`);
                                this.multipleSelection = [];
                                this.getData();
                            }else {
                                this.$message.error(res.message);
                            }
                        });
                    }).catch(() => {});
                } else {
                    this.$message.error("请选择勾选要删除数据")
                }

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
                getRoleById(row.roleId).then(res=>{
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
                debugger
                if(!this.validaRoleData()){
                    return
                }
                /*this.$refs.ruleForm.validate(valid => {
                    if (valid) {*/
                addRole(this.form).then(res=>{
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
                   /* } else {
                        this.$message.error('请填写表单信息');
                        return false;
                    }
                });*/
            },
            //编辑操作
            saveEdit() {
                /*this.$refs.ruleForm.validate(valid => {
                    if (valid) {*/
                if(!this.validaRoleData()){
                    return
                }
                editRole(this.form).then(res=>{
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
                    /*} else {
                        this.$message.error('请填写表单信息');
                        return false;
                    }
                });*/
            },
            //分页导航
            handlePageChange(val) {
                this.$set(this.query, 'pageIndex', val);
                this.getData();
            },
            //权限窗口
            handlePermiss(index, row) {
                this.idx = index;
                this.form = row;
                //记录一下角色id
                this.roleId = row.roleId;
                getMenuTreeByRoleId(row.roleId).then(res=>{
                    if(res.flag){
                        this.perms = res.data.menus;
                        this.permsIds = res.data.permsIds;
                        //由于dom元素还没有加载完，使用getCheckedKeys()方法会报错
                        this.$nextTick(() => {
                            this.$refs.tree.setCheckedKeys(this.permsIds)
                        });
                        this.permissVisible = true;
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            savePerms(){
                var menuIds = this.getCheckedKeys();
                saveRolePerms({menuIds:menuIds,roleId:this.roleId}).then(res=>{
                    if(res.flag){
                        this.$message.success("添加权限成功");
                        this.permissVisible = false;
                        this.getData();
                    }else {
                       this.$message.error(res.message);
                    }
                })
            },
            getCheckedNodes() {
                console.log(this.$refs.tree.getCheckedNodes());
            },
            getCheckedKeys() {
                console.log(this.$refs.tree.getCheckedKeys());
                return this.$refs.tree.getCheckedKeys();
            },
            setCheckedNodes() {
                this.$refs.tree.setCheckedNodes([{
                    id: 5,
                    label: '二级 2-1'
                }, {
                    id: 9,
                    label: '三级 1-1-1'
                }]);
            },
            setCheckedKeys() {
                this.$refs.tree.setCheckedKeys(this.permsIds);
            },
            checkedAll(){
                var keys = [];
                this.perms.forEach((perm)=>{
                    keys.push(perm.id);
                })
                this.$refs.tree.setCheckedKeys(keys);
            },
            resetChecked() {
                this.$refs.tree.setCheckedKeys([]);
            },
            validaRoleData(){
                if(!this.form.industryCategory){
                    this.$message.error("请选择行业类型")
                    return false;
                }
                if(!this.form.roleName){
                    this.$message.error("请输入角色名称")
                    return false;
                }
            }
        }
    };
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
