package org.bjtuse.egms.shiro;

import java.io.Serializable;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.bjtuse.egms.repository.entity.Administrator;
import org.bjtuse.egms.repository.entity.Student;
import org.bjtuse.egms.repository.entity.Teacher;
import org.bjtuse.egms.service.AdminManager;
import org.bjtuse.egms.service.StudentManager;
import org.bjtuse.egms.service.TeacherManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自实现用户与权限查询. 演示关系，密码用明文存储，因此使用默认 的SimpleCredentialsMatcher.
 */
public class ShiroDbRealm extends AuthorizingRealm {

	@Autowired
	private StudentManager studentManager;
	
	@Autowired
	private AdminManager adminManager;
	
	@Autowired
	private TeacherManager teacherManager;
	
	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		if(token != null && token.getUsername() != null){
			String[] unameAndRole = token.getUsername().split(",");
			String loginName = unameAndRole[0];
			String roleCode = unameAndRole[1];
			String password = null;
			
			if("admin".equals(roleCode)){
				Administrator admin =  adminManager.findAdminUserByLoginName(loginName);
				password = admin.getPassword();
			}else if("student".equals(roleCode)){
				Student student = studentManager.findUserByLoginNameAndStatus(loginName, 1);
				password = student.getPassword();
			}else if("teacher".equals(roleCode)){
				Teacher teacher = teacherManager.findTeacherByLoginNameAndStatus(loginName, 1);
				password = teacher.getPassword();
			}
			if (password != null) {
				return new SimpleAuthenticationInfo(new ShiroUser(
						token.getUsername(), ""),
						password, getName());
			}
		}
		
		return null;
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.fromRealm(getName()).iterator().next();
		String[] unameAndRole = shiroUser.getLoginName().split(",");
		String loginName = unameAndRole[0];
		String roleCode = unameAndRole[1];
		String password = null;
		if("admin".equals(roleCode)){
			Administrator admin =  adminManager.findAdminUserByLoginName(loginName);
			password = admin.getPassword();
		}else if("student".equals(roleCode)){
			Student student = studentManager.findUserByLoginName(loginName);
			password = student.getPassword();
		}else if("teacher".equals(roleCode)){
			Teacher teacher = teacherManager.findTeacherByLoginName(loginName);
			password = teacher.getPassword();
		}
		if (password != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 基于Permission的权限信息
			info.addRole(roleCode);
			return info;
		} else {
			return null;
		}
	}

	
	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -17486023829637114L;
		private String loginName;
		private String name;

		public ShiroUser(String loginName, String name) {
			this.loginName = loginName;
			this.name = name;
		}

		public String getLoginName() {
			return loginName;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return loginName;
		}

		public String getName() {
			return name;
		}
	}
}
