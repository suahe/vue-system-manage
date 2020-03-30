var path = require('path')

module.exports = {
  devServer: {
    proxy: {
      '/': {
        target: 'http://127.0.0.1:9011',
        changeOrigin: true,
      }
    }
  }
}