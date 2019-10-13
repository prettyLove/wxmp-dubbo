package com.kochun.wxmp.back.shiro;

import com.kochun.wxmp.core.entity.system.SysUser;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordHelper {

	private static String algorithmName = "md5";
	private static int hashIterations = 1;

	private static final String SLAT = "GUXUE2019";


	private static String encryptSalt ;
	@Value("${encryptSalt}")
	public void setEncryptSalt(String value){
		encryptSalt = value;
	}

	public static SysUser encryptPassword(SysUser user) {
//		String newPassword = new SimpleHash(algorithmName, user.getPassword(),  ByteSource.Util.bytes(user.getName()), hashIterations).toHex();
		String newPassword = new SimpleHash(algorithmName, user.getPassword(),  ByteSource.Util.bytes(encryptSalt), hashIterations).toHex();
		user.setPassword(newPassword);
		return user;
	}

	public static String encryptPassword(String userName, String password){
//		return new SimpleHash(algorithmName, password,  ByteSource.Util.bytes(userName), hashIterations).toHex();
		return new SimpleHash(algorithmName, password,  ByteSource.Util.bytes(encryptSalt), hashIterations).toHex();
	}
}
