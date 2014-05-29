package org.bjtuse.egms.util;

/**
 * 
 * @description   证书状态
 * @version currentVersion(1.0)  
 */
public class CertificateStatus {

	/**
	 * 证书状态：待检查
	 */
	public static final Integer TOBECHECKED = 1;
	
	/**
	 * 证书状态：查验通过
	 */
	public static final Integer PASSCHECKED = 2;
	
	/**
	 * 证书状态：查验不通过
	 */
	public static final Integer NOPASSCHECKED = 3;
	
	/**
	 * 证书状态： 一审通过
	 */
	public static final Integer  ONCECHECK = 4;
	
	/**
	 * 证书状态： 已归档
	 */
	public static final Integer ARCHIVED = 5;
	
	/**
	 * 证书状态：从教务处系统导入
	 */
	public static final Integer IMPORT = 6;
	
}
