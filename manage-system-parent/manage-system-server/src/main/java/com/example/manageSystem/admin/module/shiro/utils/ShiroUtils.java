package com.example.manageSystem.admin.module.shiro.utils;

import com.example.manageSystem.admin.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;


/**
 * Shiro工具类
 */
public class ShiroUtils {

	public static Session getSession() {
		Subject subject= SecurityUtils.getSubject();
		if(subject!=null) {
			return subject.getSession();
		}
		return null;
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static User getUserEntity() {
		Subject subject= SecurityUtils.getSubject();
		if(subject!=null) {
			return subject.getPrincipal()!=null?(User)subject.getPrincipal():null;
		}
		return null;
	}

	public static Integer getUserId() {
		User user=getUserEntity();
		if(user!=null) {
			return user.getUserId();
		}
		return null;
	}

	public static String getUsername() {
		User user=getUserEntity();
		if(user!=null) {
			return user.getUsername();
		}
		return "";
	}

	public static String getName() {
		User user=getUserEntity();
		if(user!=null) {
			return user.getName();
		}
		return "";
	}


	public static void setSessionAttribute(Object key, Object value) {
		Session Session=getSession();
		if(Session!=null) {
			Session.setAttribute(key, value);
		}
	}

	public static void removeSessionAttribute(Object key) {
		Session Session=getSession();
		if(Session!=null) {
			Session.removeAttribute(key);
		}
	}

	public static Object getSessionAttribute(Object key) {
		Session Session=getSession();
		if(Session!=null) {
			return Session.getAttribute(key);
		}
		return null;
	}

	public static boolean isLogin() {
		Subject subject= SecurityUtils.getSubject();
		if(subject!=null) {
			return subject.getPrincipal()!=null;
		}
		return false;
	}

	public static void logout() {
		Subject subject= SecurityUtils.getSubject();
		if(subject!=null) {
			subject.logout();
		}		
	}
	
	public static String getKaptcha(String key) {
		String kaptcha=null;
		Object obj=getSessionAttribute(key);
		if(obj!=null) {
			kaptcha=obj.toString();
		}
		Session session=getSession();
		if(session!=null) {
			session.removeAttribute(key);
		}				
		return kaptcha;
		
	}

}
