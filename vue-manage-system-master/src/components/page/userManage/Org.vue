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
                <el-tabs v-model="activeName" type="card" @tab-click="handleClick" style="width: 80%">
                    <el-tab-pane label="组织机构信息" name="first">
                        <div>
                            <el-form :model="form" ref="ruleForm" label-width="20%">
                                <el-form-item prop="orgName" label="机构名称">
                                    <el-input v-model="form.orgName"></el-input>
                                </el-form-item>
                                <el-form-item prop="orgName" label="行业类型">
                                    <el-input v-model="form.industryCategory"></el-input>
                                </el-form-item>
                                <el-form-item prop="county" label="所属区">
                                    <el-input v-model="form.county"></el-input>
                                </el-form-item>
                                <el-form-item label="所属街道/镇">
                                    <el-input v-model="form.street"></el-input>
                                </el-form-item>
                                <el-form-item label="所属居委会/村">
                                    <el-input v-model="form.neighborhood"></el-input>
                                </el-form-item>
                                <el-form-item label="备注">
                                    <el-input v-model="form.remark"></el-input>
                                </el-form-item>
                            </el-form>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="用户列表" name="second">
                        <div class="handle-box">
                            <el-input v-model="query.username" placeholder="用户名称" class="handle-input mr10" style="width: 25%"></el-input>
                            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                            <el-button type="primary" icon="el-icon-add" class="el-icon-lx-roundaddfill" @click="handleAdd()">新增</el-button>
                        </div>
                        <el-table
                                :data="tableData"
                                border
                                class="table"
                                ref="multipleTable"
                                header-cell-class-name="table-header">
                            <el-table-column type="selection" width="55" align="center"></el-table-column>
                            <el-table-column prop="userId" label="ID" width="55" align="center"></el-table-column>
                            <el-table-column prop="name" label="真实姓名"  align="center"></el-table-column>
                            <el-table-column prop="username" label="用户名称"  align="center"></el-table-column>
                            <el-table-column prop="" label="角色"  align="center"></el-table-column>
                            <el-table-column prop="mobile" label="联系电话"  align="center"></el-table-column>
                            <el-table-column prop="email" label="邮箱地址"  align="center"></el-table-column>
                            <el-table-column label="操作" width="180" align="center">
                                <template slot-scope="scope">
                                    <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
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
                    </el-tab-pane>
                </el-tabs>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    import {getOrgTree,getUsersByOrgId} from '@/api/system'
    export default {
        data() {
            return {
                query: {
                    orgId:'',
                    username: '',
                    pageIndex: 1,
                    pageSize: 10
                },
                pageTotal: 0,
                tableData:[],
                activeName: 'first',
                form:{},
                menuVisible:false,
                treeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'orgName'
                },

            }
        },
        created(){
            this.getData();
        },
        methods: {
            getData(){
                getOrgTree().then(res=>{
                    if(res.flag){
                        this.treeData =  res.data;
                    }else {
                        this.$message.error(res.message)
                    }
                })
            },
            gettableData(){
                getUsersByOrgId(this.query).then(res=>{
                    if(res.flag){
                        this.tableData=res.data;
                    }else {
                        this.$message.error(res.message)
                    }
                })
            },
            handleClick(tab, event) {
                if(tab.name=='second'){
                    this.gettableData();
                }
                console.log(tab, event);
            },
            handleNodeClick(data) {
                console.log(data);
            },
            rightClick(MouseEvent, object, Node, element) { // 鼠标右击触发事件
                this.menuVisible = false // 先把模态框关死，目的是 第二次或者第n次右键鼠标的时候 它默认的是true
                this.menuVisible = true  // 显示模态窗口，跳出自定义菜单栏
                var menu = document.querySelector('#menu')
                document.addEventListener('click', this.foo) // 给整个document添加监听鼠标事件，点击任何位置执行foo方法
                menu.style.display = "block";
                menu.style.left = MouseEvent.clientX - 0 + 'px'
                menu.style.top = MouseEvent.clientY - 80 + 'px'
                console.log('右键被点击的event:', MouseEvent)
                console.log('右键被点击的object:', object)
                console.log('右键被点击的value:', Node)
                console.log('右键被点击的element:', element)
                console.log('鼠标点击了树形结构图')

            },
            foo() { // 取消鼠标监听事件 菜单栏
                this.menuVisible = false
                document.removeEventListener('click', this.foo) // 要及时关掉监听，不关掉的是一个坑，不信你试试，虽然前台显示的时候没有啥毛病，加一个alert你就知道了
            },
            //触发搜索按钮
            handleSearch() {
                this.$set(this.query, 'pageIndex', 1);
                this.gettableData();
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
</style>
