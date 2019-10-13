package com.kochun.wxmp.core.vo.internal.response;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Integer expiredTimeInMs;

    public JwtAuthenticationResponse(String accessToken, Integer expiredTimeInMs) {
    	this.expiredTimeInMs = expiredTimeInMs;
        this.accessToken = accessToken;
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
