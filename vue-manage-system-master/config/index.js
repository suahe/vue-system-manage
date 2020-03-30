var path = require('path')

module.exports = {
  dev: {
    env: require('./dev.env'),
    port: 8088,
    autoOpenBrowser: true,
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    proxyTable: {
      '/api':{
        target:'http://127.0.0.1:8090',//本地
        changeOrigin:true,
      },
      /*'/sockjs':{
        // target:'http://192.168.102.164:8090/',
        target:'http://127.0.0.1:8090',
        changeOrigin:true,
        pathRewrite: {'^/sockjs' : ''}
      },*/
    },
    cssSourceMap: false
  }
}