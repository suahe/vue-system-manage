package com.example.common.entity.response;

/**
 * 返回码
 */
public class StatusCode {

    public static final int OK=20000;//成功
    public static final int ERROR =20001;//失败
    public static final int LOGINERROR =20002;//用户名或密码错误
    public static final int ACCESSERROR =20003;//权限不足
    public static final int REMOTEERROR =20004;//远程调用失败
    public static final int REPERROR =20005;//重复操作
    public static final int SELECTERROR = 20006;//查询失败
    public static final int ADDERROR = 20007;//新增失败
    public static final int EDITERROR = 20008;//编辑失败
    public static final int DELETEERROR = 20009;//删除失败
    public static final int PARAMSERROR = 20010;//参数错误
    public static final int ADDPERMSERROR = 20011;//添加权限失败
    public static final int RESETERROR  = 20012;//重置失败
    public static final int LOGININVALID = 20013;//登录过期
    public static final int NOPERMISSION = 20014;//没有权限

}
