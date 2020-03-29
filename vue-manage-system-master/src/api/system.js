import http from './public'
import querystring from 'querystring'
let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.apiUrl;

//..........................数据字典管理..........................
/**根据字典父code查询子列表*/
export const getDictsByParentCode = (parentCode) =>{
    return http.requestQuickGet(apiUrl+'/sys/dict/getDictsByParentCode?parentCode='+parentCode);
};

/**根据字典父id查询子列表*/
export const getDictsByParentId = (parentId) =>{
    return http.requestQuickGet(apiUrl+'/sys/dict/getDictsByParentId?parentId='+parentId);
};

/**根据id查询字典*/
export const gerDictById = (id) =>{
    return http.requestQuickGet(apiUrl+'/sys/dict/findById?id='+id);
};

/**新增字典*/
export const addDict = (params) =>{
    return http.requestPost(apiUrl+'/sys/dict/add',params);
};

/**编辑字典*/
export const editDict = (params) =>{
    return http.requestPost(apiUrl+'/sys/dict/edit',params);
};

/**批量删除字典*/
export const delDictById = (id) =>{
    return http.requestQuickGet(apiUrl+'/sys/dict/del?id='+id);
};

/**批量删除字典*/
export const delDictsByIds = (ids) =>{
    return http.requestQuickGet(apiUrl+'/sys/dict/delDictsByIds?ids='+ids);
};
/**字典搜索列表*/
export const getDictList = (params) =>{
    let paramsString =  querystring.stringify(params);
    return http.requestQuickGet(apiUrl+'/sys/dict/findPage?'+paramsString);
};

//..........................系统配置信息管理..........................
/**获取系统配置信息列表*/
export const getConfigs = ()=>{
    return http.requestQuickGet(apiUrl+'/sys/config/findAll');
};

/**保存系统配置信息*/
export const saveConfig = (params)=>{
    debugger
    return http.requestPost(apiUrl+'/sys/config/saveConfig',params);
};