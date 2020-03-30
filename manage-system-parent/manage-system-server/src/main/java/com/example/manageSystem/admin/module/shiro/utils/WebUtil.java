package com.example.manageSystem.admin.module.shiro.utils;

import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WebUtil {

    /**
     * 输出JSON
     * @param response
     * @param jsonObject
     * @author SHANHY
     * @create 2017年4月4日
     */
    public static void sendJson(HttpServletResponse response, JSONObject jsonObject) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
                .getHeader("X-Requested-With")!= null && request
                .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
            return false;
        }
        return true;
    }
}
