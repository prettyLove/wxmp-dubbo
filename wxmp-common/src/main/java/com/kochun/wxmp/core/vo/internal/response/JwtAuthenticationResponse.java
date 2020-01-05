package com.kochun.wxmp.core.vo.internal.response;

import com.kochun.wxmp.core.entity.system.SysUser;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Integer expiredTimeInMs;
    private SysUser user;

    public JwtAuthenticationResponse(String accessToken, Integer expiredTimeInMs,SysUser user) {
    	this.expiredTimeInMs = expiredTimeInMs;
        this.accessToken = accessToken;
        this.user= user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

	public Integer getExpiredTimeInMs() {
		return expiredTimeInMs;
	}

	public void setExpiredTimeInMs(Integer expiredTimeInMs) {
		this.expiredTimeInMs = expiredTimeInMs;
	}

}
