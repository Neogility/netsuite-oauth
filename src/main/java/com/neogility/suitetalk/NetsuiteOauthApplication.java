package com.neogility.suitetalk;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.neogility.suitetalk.oauth2.netsuite.SSLUtil;

@SpringBootApplication
public class NetsuiteOauthApplication {

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
		SSLUtil.turnOffSslChecking();
		SpringApplication.run(NetsuiteOauthApplication.class, args);
	}

}
