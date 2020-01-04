package com.kochun.wxmp.back.shiro;

import com.kochun.wxmp.core.entity.system.SysUser;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/***
 * 密码辅助工具
 * @author kochun
 * @date 2020/1/4 21:03
 **/
@Component
public class PasswordHelper {

	private static String algorithmName = "md5";
	private static int hashIterations = 1;

	private static String encryptSalt ;
	@Value("${system.config.encryptSalt}")
	public void setEncryptSalt(String value){
		encryptSalt = value;
	}

	public static SysUser encryptPassword(SysUser user) {
		String newPassword = new SimpleHash(algorithmName, user.getPassword(),  ByteSource.Util.bytes(encryptSalt), hashIterations).toHex();
		user.setPassword(newPassword);
		return user;
	}

	public static String encryptPassword(String userName, String password){
		return new SimpleHash(algorithmName, password,  ByteSource.Util.bytes(encryptSalt), hashIterations).toHex();
	}
}
