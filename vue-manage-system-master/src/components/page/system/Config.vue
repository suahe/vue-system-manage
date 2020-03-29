<template>
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
        <el-tab-pane label="公众号配置" name="first">
            <el-form :model="form" label-width="10%">
                <el-form-item label="微信appId">
                    <el-input  placeholder="请输入微信appId" v-model="form.appId"></el-input>
                </el-form-item>
                <el-form-item label="微信appSecret">
                    <el-input placeholder="请输入微信appSecret" v-model="form.appSecret"></el-input>
                </el-form-item>
                <el-form-item prop="value" label="消息加密字符">
                    <el-input placeholder="请输入消息加密字符" v-model="form.encodingAesKey"></el-input>
                </el-form-item>
                <el-form-item label="授权域名">
                    <el-input placeholder="请输入授权域名" v-model="form.authDomain" ></el-input>
                </el-form-item>
            </el-form>
            <span style="padding-left: 50%">
                <el-button type="primary" @click="save">保 存</el-button>
            </span>
        </el-tab-pane>
        <el-tab-pane label="小程序配置" name="second">配置管理</el-tab-pane>
        <el-tab-pane label="邮箱配置" name="third">角色管理</el-tab-pane>
        <!--<el-tab-pane label="定时任务补偿" name="fourth">定时任务补偿</el-tab-pane>-->
    </el-tabs>
</template>
<script>

    import {getConfigs,saveConfig} from '@/api/system'
    export default {
        data() {
            return {
                activeName: 'first',
                form:{
                },
            };
        },
        created() {
            this.getData();
        },
        methods: {
            handleClick(tab, event) {
                console.log(tab, event);
            },
            getData(){
                getConfigs().then(res=>{
                    //有值再进行赋值，不然会不能输入
                    debugger
                    if(res){
                        this.form = res;
                    }
                });
            },
            save(){
                saveConfig(this.form).then(res=>{
                    if(res.flag){
                        this.$message.success("保存成功");
                        this.getData();
                    }else {
                        this.$message.error(res.message);
                    }
                })
            }
        }
    };
</script>