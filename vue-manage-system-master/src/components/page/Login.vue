<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">后台管理系统</div>
            <el-form :model="params" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input v-model="params.username" placeholder="username">
                        <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                        type="password"
                        placeholder="password"
                        v-model="params.password"
                        @keyup.enter.native="submitForm()"
                    >
                        <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()">登录</el-button>
                </div>
                <!--<p class="login-tips">Tips : 用户名和密码随便填。</p>-->
            </el-form>
        </div>
    </div>
</template>

<script>
import * as sysApi from '../../api/userManage.js'
export default {
    data: function() {
        return {
            params: {
                username: '',
                password: '',
                rememberMe:'false'
            },
            rules: {
                username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
            },
        };
    },
    methods: {
        submitForm() {
            this.$refs.login.validate(valid => {
                if (valid) {
                    sysApi.login(this.params).then(res=>{
                        if (res.flag) {
                            this.$message.success("登录成功");
                            this.init(this.params.username);
                            localStorage.setItem('ms_username', this.params.username);
                            this.$router.push('/');
                        }else {
                            this.$message.error(res.message);
                            console.log(res.message);
                            return false;
                        }
                    })
                    /*this.$message.success('登录成功');
                    localStorage.setItem('ms_username', this.params.username);
                    this.$router.push('/');*/
                } else {
                    this.$message.error('请输入账号和密码');
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        //webSocket 设置 begin
        init: function (usename) {
            if(typeof(WebSocket) === "undefined"){
                console.log("您的浏览器不支持socket")
            }else{
                // 实例化socket
                this.socket = new WebSocket("ws://localhost:9011/imserver/"+usename);
                // 监听socket连接
                this.socket.onopen = this.open
                // 监听socket错误信息
                this.socket.onerror = this.error
                // 监听socket消息
                this.socket.onmessage = this.getMessage
            }
        },
        open: function () {
            console.log("socket连接成功")
        },
        error: function () {
            console.log("连接错误")
        },
        getMessage: function (msg) {
            if("登录过期，请重新登录！"==msg.data){
                this.$message.error({
                    message:"登录过期，请重新登录！",
                    showClose:true,//是否显示关闭
                    duration:60000, //显示时间毫秒
                });
                this.destroyed();
                localStorage.removeItem('ms_username');
                this.$router.push('/login');
            }
            console.log(msg.data)
        },
        send: function () {
            this.socket.send(params)
        },
        close: function () {
            console.log("socket已经关闭")
        },
        destroyed () {
            // 销毁监听
            this.socket.onclose = this.close
        }
        //webSocket 设置 end
    },
};
</script>

<style scoped>
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-image: url(../../assets/img/login-bg.jpg);
    background-size: 100%;
}
.ms-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: #fff;
    border-bottom: 1px solid #ddd;
}
.ms-login {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 350px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    background: rgba(255, 255, 255, 0.3);
    overflow: hidden;
}
.ms-content {
    padding: 30px 30px;
}
.login-btn {
    text-align: center;
}
.login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}
.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
}
</style>
