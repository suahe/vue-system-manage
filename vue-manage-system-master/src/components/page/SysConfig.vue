<template>
    <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
        <el-tab-pane label="微信公众号配置" name="first">

        </el-tab-pane>
        <el-tab-pane label="短信配置" name="second">
            <div>短信配置内容</div>
        </el-tab-pane>
        <el-tab-pane label="邮箱配置" name="three">
            <div>邮箱配置内容</div>
        </el-tab-pane>
    </el-tabs>
</template>

<script>
    export default {
        data() {
            return {
                // 默认第一个Tab
                activeName: 'first',
                isFirst: true,
                isSecond: false,
                isThree:false
            }
        },
        methods: {
            handleClick(tab) {
                if (tab.name === 'first') {
                    this.isFirst = true;
                    this.isSecond = false;
                    this.isThree = false;
                } else if (tab.name === 'second') {
                    this.isFirst = false;
                    this.isSecond = true;
                    this.isThree = false;
                } else if (tab.name === 'three'){
                    this.isFirst = false;
                    this.isSecond = false;
                    this.isThree = true;
                }
            }
        }
    };
</script>

<style scoped>

</style>
