<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i>组织机构管理
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-container class="container" style="border: 1px solid #eee">
            <!--树形结构-->
            <!--<el-tree style="width:20%;background-color: rgb(238, 241, 246)" 
                     :data="treeData"
                     :props="defaultProps"
                     default-expand-all
                     @node-contextmenu="rightClick"
                     @node-click="handleNodeClick">
            </el-tree>-->

            <div style="width:25%;">
                <div>
                    <el-input style="width:200px;"
                              placeholder="输入关键字进行过滤"
                              v-model="filterText">
                    </el-input>

                    <el-button type="primary" icon="el-icon-add" class="el-icon-lx-roundaddfill"
                               @click="handleOrgAdd">新增
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
                </el-tree>
            </div>

            <!--鼠标右键菜单栏，其实就是添加一个基于鼠标位置的模态框而已 -->

            <div v-show="menuVisible">
                <ul id="menu" class="menu">
                    <li class="menu__item" @click="addChildrenOrg">添加下级机构</li>
                    <li class="menu__item" @click="handleOrgEdit">修改</li>
                    <li class="menu__item" @click="delOrg">删除</li>
                </ul>
            </div>
            <!--tabs表格-->
            <el-container>
                <el-tabs v-model="activeName" type="card" @tab-click="handleClick" style="width: 95%">
                    <el-tab-pane label="组织机构信息" name="first">
                        <div>
                            <el-form :model="veiwOrgForm" ref="ruleForm" label-width="25%">
                                <el-form-item  label="机构名称">
                                    <el-input readonly v-model="veiwOrgForm.orgName"></el-input>
                                </el-form-item>
                                <el-form-item  label="机构编码">
                                    <el-input readonly v-model="veiwOrgForm.orgCode"></el-input>
                                </el-form-item>
                                <el-form-item  label="行业类型">
                                    <el-input readonly  v-model="veiwOrgForm.industryCategory"></el-input>
                                </el-form-item>
                                <el-form-item label="所属区">
                                    <el-input readonly v-model="veiwOrgForm.county"></el-input>
                                </el-form-item>
                                <el-form-item label="所属街道/镇">
                                    <el-input readonly v-model="veiwOrgForm.street"></el-input>
                                </el-form-item>
                                <el-form-item label="所属居委会/村">
                                    <el-input readonly v-model="veiwOrgForm.neighborhood"></el-input>
                                </el-form-item>
                                <el-form-item label="备注">
                                    <el-input readonly type="textarea" v-model="veiwOrgForm.remark"></el-input>
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
                            <el-table-column prop="userId" label="ID" width="55" align="center"></el-table-column>
                            <el-table-column prop="name" label="真实姓名" align="center"></el-table-column>
                            <el-table-column prop="username" label="用户名称" align="center"></el-table-column>
                            <el-table-column prop="roleNames" label="角色"  width="130" align="center"></el-table-column>
                            <el-table-column prop="mobile" label="联系电话" width="100"align="center"></el-table-column>
                            <el-table-column prop="email" label="邮箱地址" width="150" align="center"></el-table-column>
                            <el-table-column label="操作" width="200" align="center">
                                <template slot-scope="scope">
                                    <el-button type="text" icon="el-icon-edit"
                                               @click="handleEdit(scope.$index, scope.row)">编辑
                                    </el-button>
                                    <el-button type="text" icon="el-icon-lx-forward" class="red"
                                               @click="resetPassword(scope.$index, scope.row)">重置密码
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
                    </el-tab-pane>
                </el-tabs>

                <!-- 新增弹出框 -->
                <el-dialog title="新增" :visible.sync="orgAddVisible" width="30%">
                    <el-form :model="orgForm" :rules="orgRules" ref="ruleOrgForm" label-width="28%">
                        <el-form-item label="上级机构">
                            <el-select  v-model="orgForm.parentId" placeholder="请选择上级机构" style="width: 100%" @click.native="getOrgList">
                                <el-option value="">请选择上级机构</el-option>
                                <el-option v-for="(item,index) in orgList" :key="index" :label="item.orgName" :value="item.orgId"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="orgName" label="机构名称">
                            <el-input v-model="orgForm.orgName"></el-input>
                        </el-form-item>
                        <el-form-item prop="orgName" label="行业类型">
                            <!--@click.native="queryIndustryCategoryList('industryCategory')"-->
                            <el-select  v-model="orgForm.industryCategory" placeholder="请选择行业类型" style="width: 100%" >
                                <el-option v-for="(item,index) in industryCategoryList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="county" label="所属区">
                            <!--@click.native="queryCountyList('county')"-->
                            <el-select v-model="orgForm.county" placeholder="请选择所属区" style="width: 100%">
                                <el-option v-for="(item,index) in countyList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="所属街道/镇">
                            <el-select v-model="orgForm.street" placeholder="请选择所属街道/镇" style="width: 100%" @click.native="queryStreetList" @change="queryNeighborhoodList">
                                <el-option v-for="(item,index) in streetList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="所属居委会/村">
                            <el-select v-model="orgForm.neighborhood" placeholder="请选择所属居委会/村" style="width: 100%"  >
                                <el-option v-for="(item,index) in neighborhoodList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="备注">
                            <el-input type="textarea" v-model="orgForm.remark"></el-input>
                        </el-form-item>
                    </el-form>

                    <span slot="footer" class="dialog-footer">
                                <el-button @click="orgAddVisible = false">取 消</el-button>
                                <el-button type="primary" @click="addOrg">确 定</el-button>
                            </span>
                </el-dialog>
                <!-- 组织机构弹窗-->
                <!-- 编辑弹出框 -->
                <el-dialog title="编辑" :visible.sync="orgEditVisible" width="30%">
                    <el-form :model="orgForm" :rules="orgRules" ref="ruleOrgForm" label-width="28%">
                        <el-form-item label="上级机构">
                            <el-select  v-model="orgForm.parentId" placeholder="请选择上级机构" style="width: 100%" @click.native="getOrgList">
                                <el-option value="">请选择上级机构</el-option>
                                <el-option v-for="(item,index) in orgList" :key="index" :label="item.orgName" :value="item.orgId"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="orgName" label="机构名称">
                            <el-input v-model="orgForm.orgName"></el-input>
                        </el-form-item>
                        <el-form-item prop="industryCategory" label="行业类型">
                            <!--@click.native="queryIndustryCategoryList('industryCategory')"-->
                            <el-select  v-model="orgForm.industryCategory" placeholder="请选择行业类型" style="width: 100%" >
                                <el-option v-for="(item,index) in industryCategoryList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="county" label="所属区">
                            <!--@click.native="queryCountyList('county')"-->
                            <el-select v-model="orgForm.county" placeholder="请选择所属区" style="width: 100%"  >
                                <el-option v-for="(item,index) in countyList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="所属街道/镇">
                            <el-select v-model="orgForm.street" placeholder="请选择所属街道/镇" style="width: 100%" @click.native="queryStreetList" @change="queryNeighborhoodList">
                                <el-option v-for="(item,index) in streetList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="所属居委会/村">
                            <el-select v-model="orgForm.neighborhood" placeholder="请选择所属居委会/村" style="width: 100%"  @change="">
                                <el-option v-for="(item,index) in neighborhoodList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="备注">
                            <el-input type="textarea" v-model="orgForm.remark"></el-input>
                        </el-form-item>
                    </el-form>

                    <span slot="footer" class="dialog-footer">
                                <el-button @click="orgEditVisible=false">取 消</el-button>
                                <el-button type="primary" @click="editOrg">确 定</el-button>
                            </span>
                </el-dialog>

                <!-- 新增弹出框 -->
                <el-dialog title="添加下级机构" :visible.sync="addChildrenOrgVisible" width="30%">
                    <el-form :model="orgForm" :rules="orgRules" ref="ruleOrgForm" label-width="28%">
                        <el-form-item label="上级机构">
                            <el-select disabled v-model="orgForm.parentId" placeholder="请选择上级机构" style="width: 100%" @click.native="getOrgList">
                                <el-option value="">请选择上级机构</el-option>
                                <el-option v-for="(item,index) in orgList" :key="index" :label="item.orgName" :value="item.orgId"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="orgName" label="机构名称">
                            <el-input v-model="orgForm.orgName"></el-input>
                        </el-form-item>
                        <el-form-item prop="orgName" label="行业类型">
                            <!--@click.native="queryIndustryCategoryList('industryCategory')"-->
                            <el-select  v-model="orgForm.industryCategory" placeholder="请选择行业类型" style="width: 100%" >
                                <el-option v-for="(item,index) in industryCategoryList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item  prop="county" label="所属区">
                            <!--@click.native="queryCountyList('county')"-->
                            <el-select disabled v-model="orgForm.county" placeholder="请选择所属区" style="width: 100%">
                                <el-option v-for="(item,index) in countyList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="所属街道/镇">
                            <el-select v-model="orgForm.street" placeholder="请选择所属街道/镇" style="width: 100%" @click.native="queryStreetList" @change="queryNeighborhoodList">
                                <el-option v-for="(item,index) in streetList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="所属居委会/村">
                            <el-select v-model="orgForm.neighborhood" placeholder="请选择所属居委会/村" style="width: 100%"  >
                                <el-option v-for="(item,index) in neighborhoodList" :key="index" :label="item.name" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="备注">
                            <el-input type="textarea" v-model="orgForm.remark"></el-input>
                        </el-form-item>
                    </el-form>

                    <span slot="footer" class="dialog-footer">
                                <el-button @click="addChildrenOrgVisible = false">取 消</el-button>
                                <el-button type="primary" @click="addChildrenOrgSave">确 定</el-button>
                            </span>
                </el-dialog>

                <!-- 用户弹窗-->
                <!-- 新增弹出框 -->
                <el-dialog title="新增" :visible.sync="addVisible" width="30%">
                    <el-form :model="userForm" :rules="userRules" ref="userRulesForm" label-width="80px">
                        <el-form-item prop="username" label="用户名称">
                            <el-input v-model="userForm.username"></el-input>
                        </el-form-item>
                        <el-form-item prop="name" label="真实姓名">
                            <el-input v-model="userForm.name"></el-input>
                        </el-form-item>
                        <el-form-item prop="password"  label="密码">
                            <el-input placeholder="请输入密码" v-model="userForm.password" show-password></el-input>
                        </el-form-item>
                        <!--<el-form-item prop="checkPassword" label="确认密码">
                            <el-input type="password" v-model="form.checkPassword" autocomplete="off"></el-input>
                        </el-form-item>-->
                        <el-form-item prop="sex" label="性别">
                            <el-radio v-model="radio" label="1">男</el-radio>
                            <el-radio v-model="radio" label="2">女</el-radio>
                        </el-form-item>
                        <!-- 使用<el-checkbox label="复选框 A"></el-checkbox>复选框不行-->
                        <el-form-item label="角色">
                            <el-checkbox-group v-model="checkedRoles">
                                <el-checkbox v-for="role in roles" :label="role.roleName" :key="role.roleId">
                                    {{role.roleName}}
                                </el-checkbox>
                            </el-checkbox-group>
                        </el-form-item>
                        <el-form-item prop="mobile" label="联系方式">
                            <el-input v-model="userForm.mobile"></el-input>
                        </el-form-item>
                        <el-form-item prop="email" label="邮箱">
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
                    <el-form :model="userForm" :rules="userRules" ref="userRulesForm" label-width="80px">
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
                                <el-checkbox v-for="role in roles" :label="role.roleName" :key="role.roleId">
                                    {{role.roleName}}
                                </el-checkbox>
                            </el-checkbox-group>
                        </el-form-item>
                        <el-form-item prop="mobile" label="联系方式">
                            <el-input v-model="userForm.mobile"></el-input>
                        </el-form-item>
                        <el-form-item prop="email" label="邮箱">
                            <el-input v-model="userForm.email"></el-input>
                        </el-form-item>
                    </el-form>

                    <span slot="footer" class="dialog-footer">
                                <el-button @click="editVisible = false">取 消</el-button>
                                <el-button type="primary" @click="saveEdit">确 定</el-button>
                            </span>
                </el-dialog>

            </el-container>
        </el-container>
    </div>
</template>

<script>
    import {
        getOrgTree, getUsersByOrgId, getUserAndRolesById, getRolesByOrgId,
        getOrgAndUsersByOrgId, editUser, addUser, delUserByUserId,resetPassword,getDictsByParentCode,
        getOrgByOrgId,addOrg,editOrg,delOrg,getOrgList,delUserByIds
    } from '@/api/userManage';

    export default {

        data() {
            /*var validatePass = (rule, value, callback) => {
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
            };*/
            //校验是否选择角色
            var validaCheckRoles = (rule, value, callback) => {
                if (this.checkedRoles.length == 0) {
                    callback(new Error('请选择角色'));
                }
            };
            //电话可以不填，填就要校验
            var checkPhone = (rule, value, callback) => {
                const phoneReg = /^1[3|4|5|7|8][0-9]{9}$/;
                if (value) {
                    if (phoneReg.test(value)) {
                        callback();
                    } else {
                        callback(new Error('电话号码格式不正确'));
                    }
                }
            };
            //邮箱可以不填，填就要校验
            var checkEmail = (rule, value, callback) => {
                const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
                if (value) {
                    if (mailReg.test(value)) {
                        callback();
                    } else {
                        callback(new Error('请输入正确的邮箱格式'));
                    }
                }
            };
            //校验是否选择角色
            var validaIndustryCategory = (rule, value, callback) => {
                if (this.industryCategoryList.length == 0) {
                    callback(new Error('请选择行业类型'));
                }
            };
            return {
                filterText: '',
                checkedRoles: [],
                roles: [],
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
                veiwOrgForm:{},
                orgForm: {},
                radio: '1',
                menuVisible: false,
                treeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'orgName'
                },
                editVisible: false,//添加用户编辑窗口
                addVisible: false,//添加用户添加窗口
                orgAddVisible:false,//添加机构添加窗口
                orgEditVisible:false,//添加机构编辑窗口
                addChildrenOrgVisible:false,//添加下级机构窗口
                orgList:[],
                industryCategoryList:[],//行业种类集合
                countyList:[],//区集合
                streetList:[],//街道/镇集合
                neighborhoodList:[],//居委会/村集合
                multipleSelection: [],
                ids:[],
                userRules: {
                    username: [{ required: true, message: '请输入用户名称', trigger: 'blur' }],
                    name: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
                    password: [{ required: true, message: '请输入用户密码', trigger: 'blur' }],
                    checkedRoles: [{ validator: validaCheckRoles, message: '请选择角色', trigger: 'blur' }],
                    email: [{ validator: checkEmail, trigger: 'blur' }],
                    mobile: [{ validator: checkPhone, trigger: 'blur' }]
                },
                orgRules : {
                    orgName: [{ required: true, message: '请输入机构名称', trigger: 'blur' }],
                    industryCategory: [{ required: true, message: '请选择行业类型', trigger: 'blur' }],
                    county: [{ required: true, message: '请选择所属区', trigger: 'blur' }],
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
                return data.orgName.indexOf(value) !== -1;
            },
            getTreeData() {
                getOrgTree().then(res => {
                    if (res.flag) {
                        this.treeData = res.data;
                    } else {
                        this.$message.error(res.message);
                    }
                });
                this.getOrgList();//获取机构列表
                this.queryCountyList('county');//区列表
                this.queryIndustryCategoryList('industryCategory');//行业类型
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
                //获取组织机构信息和角色列表
                getOrgAndUsersByOrgId(this.query.orgId).then(res => {
                    if (res.flag) {
                        this.veiwOrgForm = res.data.org;
                        this.tableData = res.data.users;
                    } else {
                        this.$message.error(res.message);
                    }
                });
            },
            rightClick(MouseEvent, object, Node, element) { // 鼠标右击触发事件
                this.query.orgId = object.orgId;
                this.menuVisible = false; // 先把模态框关死，目的是 第二次或者第n次右键鼠标的时候 它默认的是true
                this.menuVisible = true;  // 显示模态窗口，跳出自定义菜单栏
                var menu = document.querySelector('#menu');
                document.addEventListener('click', this.foo); // 给整个document添加监听鼠标事件，点击任何位置执行foo方法
                menu.style.display = 'block';

                let  menuColl = this.global.menuCollapse;//菜单收缩情况，为true是收缩
                if(menuColl){
                    menu.style.left = MouseEvent.clientX - 46 + 'px';
                    menu.style.top = MouseEvent.clientY - 80 + 'px';
                }else {
                    menu.style.left = MouseEvent.clientX  - 233 +'px';
                    menu.style.top = MouseEvent.clientY - 80 + 'px';
                }

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
                this.gettableData();
            },
            //新增窗口
            handleAdd() {
                this.userForm = {};
                if (!this.query.orgId) {
                    this.$message.error('请选择组织机构');
                    return;
                }
                getRolesByOrgId(this.query.orgId).then(res => {
                    if (res.flag) {
                        this.roles = res.data;
                        this.addVisible = true;
                    } else {
                        this.$message.error(res.message);
                    }
                });
            },
            //编辑窗口
            handleEdit(index, row) {
                this.idx = index;
                this.userForm = row;
                getUserAndRolesById({ userId: row.userId, orgId: this.query.orgId }).then(res => {
                    if (res.flag) {
                        this.userForm = res.data.user;
                        this.radio = res.data.user.sex + '';
                        this.roles = res.data.roles;
                        this.checkedRoles = res.data.checkedRoles;
                        this.addVisible = false;
                        this.editVisible = true;
                    } else {
                        this.$message.error(res.message);
                    }
                });
            },
            //新增操作
            saveAdd() {
                this.userForm.orgId = this.query.orgId;
                this.userForm.sex = this.radio;//设置性别
                this.userForm.checkedRoles = this.checkedRoles;
               /* this.$refs.userRulesForm.validate(valid => {
                    if (valid) {*/
               if(!this.validaData()){
                   return;
               }
               if(!this.userForm.password){
                   this.$message.error("请输入用户密码");
                   return false;
               }
               addUser(this.userForm).then(res => {
                    if (res.flag) {
                        this.$message.success(res.message);
                        this.addVisible = false;
                        this.gettableData();
                    } else {
                        this.$message.error(res.message);
                        console.log(res.message);
                        return false;
                    }
                });
                   /* } else {
                        this.$message.error('请填写表单信息');
                        return false;
                    }
                });*/
            },
            //编辑操作
            saveEdit() {
                this.userForm.orgId = this.query.orgId;
                this.userForm.sex = this.radio;//设置性别
                this.userForm.checkedRoles = this.checkedRoles;
                //表达校验失效this.$refs.userRulesForm.validate()，采用代码校验
                if(!this.validaData()){
                    return false;
                }
                /*this.$refs.userRulesForm.validate(valid => {
                    if (valid) {*/
                editUser(this.userForm).then(res => {
                    if (res.flag) {
                        this.$message.success(res.message);
                        this.editVisible = false;
                        this.gettableData();
                    } else {
                        this.$message.error(res.message);
                        console.log(res.message);
                        return false;
                    }
                });
                    /*} else {
                        this.$message.error('请填写表单信息');
                        return false;
                    }
                });*/
            },
            validaData(){
                if(!this.userForm.username){
                    this.$message.error("请输入用户名称")
                    return false;
                }
                if(!this.userForm.name){
                    this.$message.error("请输入真实姓名")
                    return false;
                }
                if(!this.userForm.sex){
                    this.$message.error("请选择性别")
                    return
                }
                if(!this.userForm.checkedRoles||this.userForm.checkedRoles.length==0){
                    this.$message.error("请选择角色")
                    return false;
                }
                return true;
            },
            handleDelete(index, row) {
                this.$confirm('确定要批量删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    delUserByUserId(row.userId).then(res => {
                        if (res.flag) {
                            this.$message.success('删除了' + row.username);
                            this.gettableData();
                        } else {
                            this.$message.error(res.message);
                        }
                    });
                }).catch(() => {
                });
            },
            //密码重置为123456
            resetPassword(index, row) {
                this.$confirm('确定要重置密码为123456？', '提示', {
                    type: 'warning'
                }).then(() => {
                    resetPassword(row.userId).then(res => {

                    });
                }).catch(() => {
                });
            },
            //查询行业类型下拉框
            queryIndustryCategoryList(code){
                getDictsByParentCode(code).then(res=>{
                    if(res.flag){
                        this.industryCategoryList = res.data;
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            //查询区下拉框
            queryCountyList(code){
                getDictsByParentCode(code).then(res=>{
                    if(res.flag){
                        this.countyList = res.data;
                    }else {
                        this.$message.error(res.message);
                    }
                })
            },
            //街道
            queryStreetList(){
                //当区被选择时，加载所选区的下级
                this.streetList = [];
                this.neighborhoodList = [];
                var parentCode = '';
                this.countyList.forEach((item)=>{
                    if(item.value==this.orgForm.county){
                        parentCode = item.code;
                    }
                });
                if(parentCode!=''){
                    getDictsByParentCode(parentCode).then(res=>{
                        if(res.flag){
                            this.streetList = res.data;
                        }else {
                            this.$message.error(res.message);
                        }
                    })
                }
            },
            //居委会/村
            queryNeighborhoodList(){
                //当街道或镇被选择时，加载所选街道或镇的下级
                this.neighborhoodList = [];
                var parentCode = '';
                this.streetList.forEach((item)=>{
                    if(item.value==this.orgForm.street){
                        parentCode = item.code;
                    }
                });
                if(parentCode!=''){
                    getDictsByParentCode(parentCode).then(res=>{
                        if(res.flag){
                            this.neighborhoodList = res.data;
                        }else {
                            this.$message.error(res.message);
                        }
                    })
                }
            },
            handleOrgAdd(){
                debugger
                this.orgForm = {};
                this.orgEditVisible = false;
                this.orgAddVisible = true;
            },
            handleOrgEdit(){
                this.orgAddVisible =false;
                this.orgEditVisible =true;
                getOrgByOrgId(this.query.orgId).then(res=>{
                    if(res.flag){
                        this.orgForm = res.data;
                    }else{
                        this.$message.error(res.message);
                    }
                })
            },
            addOrg(){
                if(!this.validaOrgData()){
                    return ;
                }
                addOrg(this.orgForm).then(res=>{
                    if(res.flag){
                        this.getTreeData();
                        this.gettableData();
                        this.orgAddVisible =false;
                    }else{
                        this.$message.error(res.message);
                    }
                })
            },
            editOrg(){
                if(!this.validaOrgData()){
                    return ;
                }
                editOrg(this.orgForm).then(res=>{
                    if(res.flag){
                        this.getTreeData();
                        this.gettableData();
                        this.orgEditVisible =false;
                    }else{
                        this.$message.error(res.message);
                    }
                })
            },
            addChildrenOrgSave(){
                if(!this.validaOrgData()){
                    return ;
                }
                addOrg(this.orgForm).then(res=>{
                    if(res.flag){
                        this.getTreeData();
                        this.gettableData();
                        this.addChildrenOrgVisible =false;
                    }else{
                        this.$message.error(res.message);
                    }
                })
            },
            delOrg(){
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    delOrg(this.query.orgId).then(res=>{
                        if(res.flag){
                            this.veiwOrgForm = {};
                            this.getTreeData();
                            this.gettableData();
                        }else{
                            this.$message.error(res.message);
                        }
                    })
                });
            },
            getOrgList(){
                getOrgList().then(res=>{
                    if(res.flag){
                        this.orgList = res.data;
                    }else{
                        this.$message.error(res.message);
                    }
                })
            },
            //添加下级机构
            addChildrenOrg(){
                this.orgForm={};
                this.orgForm.parentId = this.query.orgId;
                /*if(!this.validaOrgData()){
                  return ;
                }*/
                getOrgByOrgId(this.query.orgId).then(res=>{
                    this.orgForm.county = res.data.county;
                    this.queryCountyList('county')
                    this.addChildrenOrgVisible = true;
                });

            },
            //多选操作
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            delAllSelection() {
                const length = this.multipleSelection.length;
                let str = '';
                //this.delList = this.delList.concat(this.multipleSelection);
                for (let i = 0; i < length; i++) {
                    str += this.multipleSelection[i].username + ',';
                    this.ids[i] = this.multipleSelection[i].userId;
                }
                if (length>0){
                    //二次确认删除
                    this.$confirm('确定要批量删除吗？', '提示', {
                        type: 'warning'
                    }).then(() => {
                        delUserByIds(this.ids).then(res=>{
                            if(res.flag){
                                this.$message.success(`删除了${str}`);
                                this.multipleSelection = [];
                                this.gettableData();
                            }else {
                                this.$message.error(res.message);
                            }
                        });
                    }).catch(() => {});
                } else {
                    this.$message.error("请选择勾选要删除数据")
                }
            },
            validaOrgData(){
                if(!this.orgForm.orgName){
                    this.$message.error("请输入机构名称");
                    return false;
                }
                if(!this.orgForm.industryCategory){
                    this.$message.error("请选择行业类型");
                    return false;
                }
                if(!this.orgForm.county){
                    this.$message.error("请选择所属区");
                    return false;
                }
                return true;
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
