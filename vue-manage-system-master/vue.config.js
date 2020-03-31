module.exports = {
    baseUrl: './',
    assetsDir: 'static',
    productionSourceMap: false,
    devServer: {
        port: 8085,
        https: false,
        hotOnly: false,
        disableHostCheck: true,
        proxy: {
             '/api':{
                 target:'http://127.0.0.1:9011',
                 changeOrigin:true,
                 pathRewrite:{
                     '/api':''
                 }
             }
        }
    }
};
