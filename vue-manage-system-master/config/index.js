module.exports = {
    /*devServer: {
        proxy: {
            '/api': {
                target: 'http://localhost:9011',
                onProxyRes: function(proxyRes, req, res) { // 监听代理的返回结果
                    const location = proxyRes.headers['location']
                    const locationRE = /http(?:s)?:\/\/[0-9.:]+?(\/.*?[a-zA-Z]+)$/
                    if (location) {  // 后端重定向的话返回headers中会有location
                        const matched = location.match(locationRE)
                        const matchGroup = matched && matched[1]
                        proxyRes.headers['location'] = matchGroup ? `http://localhost:8080${matchGroup}` : location
                    }
                }
            }
        }
    }*/
}
