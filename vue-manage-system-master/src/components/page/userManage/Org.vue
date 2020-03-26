<template>
    <div>
        <el-container style="border: 1px solid #eee">
            <!--树形结构-->
            <el-tree style="width:20%;background-color: rgb(238, 241, 246)"
                     :data="treeData"
                     :props="defaultProps"
                     default-expand-all
                     @node-contextmenu="rightClick"
                     @node-click="handleNodeClick">
            </el-tree>

            <!--鼠标右键菜单栏，其实就是添加一个基于鼠标位置的模态框而已 -->

            <div v-show="menuVisible">
                <ul id="menu" class="menu">
                    <li class="menu__item" @click="">添加下级机构</li>
                    <li class="menu__item" @click="">修改</li>
                    <li class="menu__item">删除</li>
                </ul>
            </div>
            <!--tabs表格-->
            <el-container>
                <el-tabs v-model="activeName" type="card" @tab-click="handleClick" style="width: 90%">
                    <el-tab-pane label="组织机构信息" name="first">
                        <div>
                            <el-form :model="orgForm" ref="ruleForm" label-width="20%">
                                <el-form-item prop="orgName" label="机构名称">
                                    <el-input v-model="orgForm.orgName"></el-input>
                                </el-form-item>
                                <el-form-item prop="orgName" label="行业类型">
                                    <el-input v-model="orgForm.industryCategory"></el-input>
                                </el-form-item>
                                <el-form-item prop="county" label="所属区">
                                    <el-input v-model="orgForm.county"></el-input>
                                </el-form-item>
                                <el-form-item label="所属街道/镇">
                                    <el-input v-model="orgForm.street"></el-input>
                                </el-form-item>
                                <el-form-item label="所属居委会/村">
                                    <el-input v-model="orgForm.neighborhood"></el-input>
                                </el-form-item>
                                <el-form-item label="备注">
                                    <el-input v-model="orgForm.remark"></el-input>
                                </el-form-item>
                            </el-form>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="用户列表" name="second">
                        <div class="handle-box">
                            <el-input v-model="query.username" placeholder="用户名称" class="handle-input mr10"
                                      style="width: 25%"></el-input>
                            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                            <el-button type="primary" icon="el-icon-add" class="el-icon-lx-roundaddfill"
                                       @click="handleAdd()">新增
                            </el-button>
                        </div>
                        <el-table
                                :data="tableData"
                                border
                                class="table"
                                ref="multipleTable"
                                header-cell-class-name="table-header">
                            <el-table-column type="selection" width="55" align="center"></el-table-column>
                            <el-table-column prop="userId" label="ID" width="55" align="center"></el-table-column>
                            <el-table-column prop="name" label="真实姓名" align="center"></el-table-column>
                            <el-table-column prop="username" label="用户名称" align="center"></el-table-column>
                            <el-table-column prop="roleNames" label="角色" align="center"></el-table-column>
                            <el-table-column prop="mobile" label="联系电话" align="center"></el-table-column>
                            <el-table-column prop="email" label="邮箱地址" align="center"></el-table-column>
                            <el-table-column label="操作" width="180" align="center">
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

                        <!-- 新增弹出框 -->
                        <el-dialog title="新增" :visible.sync="addVisible" width="30%">
                            <el-form :model="userForm" :rules="userRules" ref="ruleUserForm" label-width="80px">
                                <el-form-item prop="username" label="用户名称">
                                    <el-input v-model="userForm.username"></el-input>
                                </el-form-item>
                                <el-form-item prop="name" label="真实姓名">
                                    <el-input v-model="userForm.name"></el-input>
                                </el-form-item>
                                <!--<el-form-item prop="password"  label="密码">
                                    <el-input v-model="form.password" autocomplete="off"></el-input>
                                </el-form-item>
                                <el-form-item prop="checkPassword" label="确认密码">
                                    <el-input type="password" v-model="form.checkPassword" autocomplete="off"></el-input>
                                </el-form-item>-->
                                <el-form-item prop="sex" label="性别">
                                    <el-radio v-model="radio" label="1">男</el-radio>
                                    <el-radio v-model="radio" label="2">女</el-radio>
                                </el-form-item>
                                <!-- 使用<el-checkbox label="复选框 A"></el-checkbox>复选框不行-->
                                <el-form-item label="角色">
                                    <el-checkbox-group v-model="checkedRoles">
                                        <el-checkbox v-for="role in roles" :label="role.roleName" :key="role.roleId">{{role.roleName}}</el-checkbox>
                                    </el-checkbox-group>
                                </el-form-item>
                                <el-form-item label="联系方式">
                                    <el-input v-model="userForm.mobile"></el-input>
                                </el-form-item>
                                <el-form-item label="邮箱">
                                    <el-input v-model="userForm.email"></el-input>
                                </el-form-item>
                            </el-form>

                            <span slot="footer" class="dialog-footer">
                                <el-button @click="addVisible = false">取 消</el-button>
                                <el-button type="primary" @click="saveAdd">确 定</el-button>
                            </span>
                        </el-dialog>

                        <!-- 编辑弹出框 -->
                        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
                            <el-form :model="userForm" :rules="userRules" ref="ruleUserForm" label-width="80px">
                                <el-form-item prop="username" label="用户名称">
                                    <el-input v-model="userForm.username"></el-input>
                                </el-form-item>
                                <el-form-item prop="name" label="真实姓名">
                                    <el-input v-model="userForm.name"></el-input>
                                </el-form-item>
                                <!--<el-form-item prop="password"  label="密码">
                                    <el-input type="password" v-model="form.password" autocomplete="off"></el-input>
                                </el-form-item>
                                <el-form-item prop="checkPassword" label="确认密码">
                                    <el-input type="password" v-model="form.checkPassword" autocomplete="off"></el-input>
                                </el-form-item>-->
                                <el-form-item prop="radio" label="性别">
                                    <el-radio v-model="radio" label="1">男</el-radio>
                                    <el-radio v-model="radio" label="2">女</el-radio>
                                </el-form-item>
                                <!-- 使用<el-checkbox label="复选框 A"></el-checkbox>复选框不行-->
                                <el-form-item label="角色" prop="checkedRoles">
                                    <el-checkbox-group v-model="checkedRoles">
                                        <el-checkbox v-for="role in roles" :label="role.roleName" :key="role.roleId">{{role.roleName}}</el-checkbox>
                                    </el-checkbox-group>
                                </el-form-item>
                                <el-form-item label="联系方式">
                                    <el-input v-model="userForm.mobile"></el-input>
                                </el-form-item>
                                <el-form-item label="邮箱">
                                    <el-input v-model="userForm.email"></el-input>
                                </el-form-item>
                            </el-form>
                            <span slot="footer" class="dialog-footer">
                                <el-button @click="editVisible = false">取 消</el-button>
                                <el-button type="primary" @click="saveEdit">确 定</el-button>
                             </span>
                        </el-dialog>
                    </el-tab-pane>
                </el-tabs>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    import {
        getOrgTree, getUsersByOrgId,getUserAndRolesById,getRolesByOrgIdAndUserId,
        getOrgAndUsersByOrgId,editUser,addUser,delUserByUserId
    } from '@/api/system';

    export default {

        data() {
            const roleOptions = [{roleId:1,roleName:'上海'}, {roleId:2,roleName:'北京'}, {roleId:3,roleName:'广州'}, {roleId:4,roleName:'深圳'}];
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.userForm.checkPassword !== '') {
                        this.$refs.ruleUserForm.validateField('checkPassword');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.userForm.password) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                checkedRoles: ['上海', '北京'],
                roles: roleOptions,
                query: {
                    orgId: '',
                    username: '',
                    pageIndex: 1,
                    pageSize: 10
                },
                pageTotal: 0,
                tableData: [],
                activeName: 'first',
                userForm: {},
                orgForm:{},
                radio:'1',
                menuVisible: false,
                treeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'orgName'
                },
                editVisible:false,
                addVisible:false,
                userRules: {
                    username:[{ required: true, message: '请输入用户名称', trigger: 'blur' }],
                    name:[{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
                    password: [{ validator: validatePass, trigger: 'blur' }],
                    checkPassword: [{ validator: validatePass2, trigger: 'blur' }],
                    radio:[{ required: true, message: '请选择性别', trigger: 'blur' }],
                    checkRoles:[{ required: true, message: '请选择角色', trigger: 'blur' }],
                }
            };
        },
        created() {
            this.getData();
        },
        methods: {
            getData() {
                getOrgTree().then(res => {
                    if (res.flag) {
                        this.treeData = res.data;
                    } else {
                        this.$message.error(res.message);
                    }
                });
            },
            gettableData() {
                getUsersByOrgId(this.query).then(res => {
                    this.tableData = res.rows;
                    this.pageTotal = res.total;
                });
            },
            handleClick(tab, event) {
                if (tab.name == 'second') {
                    this.gettableData();
                }
                console.log(tab, event);
            },
            //被鼠标点击
            handleNodeClick(data) {
                console.log(data);
                this.query.orgId = data.orgId;//赋值
                getOrgAndUsersByOrgId(this.query.orgId).then(res=>{
                    if (res.flag){
                        this.orgForm = res.data.org;
                        this.tableData = res.data.users;
                    }else {
                        this.$message.error(res.message)
                    }
                })
            },
            rightClick(MouseEvent, object, Node, element) { // 鼠标右击触发事件
                this.menuVisible = false; // 先把模态框关死，目的是 第二次或者第n次右键鼠标的时候 它默认的是true
                this.menuVisible = true;  // 显示模态窗口，跳出自定义菜单栏
                var menu = document.querySelector('#menu');
                document.addEventListener('click', this.foo); // 给整个document添加监听鼠标事件，点击任何位置执行foo方法
                menu.style.display = 'block';
                menu.style.left = MouseEvent.clientX - 0 + 'px';
                menu.style.top = MouseEvent.clientY - 80 + 'px';
                console.log('右键被点击的event:', MouseEvent);
                console.log('右键被点击的object:', object);
                console.log('右键被点击的value:', Node);
                console.log('右键被点击的element:', element);
                console.log('鼠标点击了树形结构图');

            },
            foo() { // 取消鼠标监听事件 菜单栏
                this.menuVisible = false;
                document.removeEventListener('click', this.foo); // 要及时关掉监听，不关掉的是一个坑，不信你试试，虽然前台显示的时候没有啥毛病，加一个alert你就知道了
            },
            //触发搜索按钮
            handleSearch() {
                this.$set(this.query, 'pageIndex', 1);
                this.gettableData();
            },
            //分页导航
            handlePageChange(val) {
                this.$set(this.query, 'pageIndex', val);
                this.getData();
            },
            //新增窗口
            handleAdd() {
                this.userForm = {};
                if(!this.query.orgId){
                    this.$message.error("请选择组织机构");
                    return
                }
                getRolesByOrgIdAndUserId(this.query.orgId).then(res=>{
                    if(res.flag){
                        this.roles = res.data.roles;
                        this.checkedRoles = res.data.checkedRoles;
                        this.editVisible = false;
                        this.addVisible = true;
                    }else {
                        this.$message.error(res.message)
                    }
                })
            },
            //编辑窗口
            handleEdit(index, row) {
                this.idx = index;
                this.userForm = row;
                getUserAndRolesById({userId:row.userId,orgId:this.query.orgId}).then(res=>{
                    if(res.flag){
                        this.userForm = res.data.user;
                        this.radio = res.data.user.sex+'';
                        this.roles = res.data.roles;
                        this.checkedRoles = res.data.checkedRoles;
                        this.addVisible = false;
                        this.editVisible = true;
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            //新增操作
            saveAdd() {
                this.userForm.sex = this.radio;//设置性别
                addUser(this.userForm).then(res=>{
                    if (res.flag) {
                        this.$message.success(res.message);
                    }else {
                        this.$message.error(res.message);
                        console.log(res.message);
                        return false;
                    }
                    this.addVisible = false;
                    this.gettableData();
                })
            },
            //编辑操作
            saveEdit() {
                this.userForm.sex = this.radio;//设置性别
                this.userForm.checkedRoles = this.checkedRoles;//勾选的角色
                editUser(this.userForm).then(res=>{
                    if (res.flag) {
                        this.$message.success(res.message);
                    }else {
                        this.$message.error(res.message);
                        console.log(res.message);
                        return false;
                    }
                    this.editVisible = false;
                    this.gettableData();
                });
            },
            handleDelete(index, row){
                delUserByUserId(row.userId).then(res=>{
                    if(res.flag){
                        this.$message.success("删除了"+row.getusername);
                    }else {
                        this.$message.error(res.message)
                    }
                })
            }
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
</style>
