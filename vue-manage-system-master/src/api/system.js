import http from './public'
import querystring from 'querystring'
let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.apiUrl;

/*登录*/
export const login = params => {
  //将params对象数据拼装成key/value串
  let paramsString =  querystring.stringify(params);
  return http.requestPost(apiUrl+'/login'+"?"+paramsString);
};

/*获取角色列表*/
export const getRoleList = query => {
  //将params对象数据拼装成key/value串
  let queryString =  querystring.stringify(query);
  return http.requestQuickGet(apiUrl+'/sys/role/findPage'+"?"+queryString);
};

/*根据id获取角色*/
export const addRole = params => {
  return http.requestPost(apiUrl+'/sys/role/add',params);
};

/*根据id获取角色*/
export const getRoleById = id => {
  return http.requestQuickGet(apiUrl+'/sys/role/findById'+"?id="+id);
};

/*编辑角色*/
export const editRole = params => {
  return http.requestPost(apiUrl+'/sys/role/edit',params);
};

/*删除角色*/
export const delRole = id => {
  return http.requestQuickGet(apiUrl+'/sys/role/del'+"?id="+id);
};

/*批量删除角色*/
export const delRoleByIds = ids => {
  return http.requestQuickGet(apiUrl+'/sys/role/delByIds'+"?ids="+ids);
};

/*根据角色id查询权限树形结构*/
export const getMenuTreeByRoleId = roleId =>{
  return http.requestQuickGet(apiUrl+'/sys/menu/getMenuTreeByRoleId'+"?roleId="+roleId);
};

/*获取菜单列表*/
export const getMenuList =() =>{
  return http.requestQuickGet(apiUrl+'/sys/menu/findAll');
};
/*根据id获取菜单*/
export const getMenuById = id => {
  return http.requestQuickGet(apiUrl+'/sys/menu/findById?id='+id);
};

/**新增菜单*/
export const addMenu = params =>{
  return http.requestPost(apiUrl+'/sys/menu/add',params);
};

/**编辑菜单*/
export const editMenu = params =>{
  return http.requestPost(apiUrl+'/sys/menu/edit',params);
};

/**添加勾选权限**/
export const saveRolePerms = params =>{
  return http.requestPost(apiUrl+'/sys/role/saveRolePerms',params);
};

/**获取组织树形结构*/
export const getOrgTree = () =>{
  return http.requestQuickGet(apiUrl+'/sys/org/getOrgTree');
};
/**根据组织id获取用户列表*/
export const getUsersByOrgId = (params) =>{
  //将params对象数据拼装成key/value串
  let paramsString =  querystring.stringify(params);
  return http.requestQuickGet(apiUrl+'/sys/user/getUsersByOrgId?'+paramsString);
};

/**根据用户id获取用户*/
export const getUserById = (userId) =>{
  return http.requestQuickGet(apiUrl+'/sys/user/getUserById?userId'+userId);
};

/**增加用户*/
export const addUser = (params) =>{
  return http.requestQuickGet(apiUrl+'/sys/user/getUserById?userId'+userId);
};

/**编辑用户*/
export const editUser = (params) =>{
  return http.requestPost(apiUrl+'/sys/user/editUser?userId'+userId);
};