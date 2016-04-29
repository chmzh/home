package com.cmz.web.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * @author jayson <786331@qq.com>
 *
 */
public class MD5Util {

	/**
	 * 
	 * @param data
	 * @return
	 */
	public final static String getMD5(String data) {
		return DigestUtils.md5Hex(data);
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public final static String getMD5(byte[] data) {
		return DigestUtils.md5Hex(data);
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public final static String getMD5Short(String data) {
		String md5 = DigestUtils.md5Hex(data);
		return StringUtils.substring(md5, 8, 24);
	}
	
}
