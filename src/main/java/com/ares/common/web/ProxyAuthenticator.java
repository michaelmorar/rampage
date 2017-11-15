package com.ares.common.web; 

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyAuthenticator extends Authenticator {
    private String username;
    private String password;
    public ProxyAuthenticator(String username, String password){
        this.username = username;
        this.password = password;
    }
    public PasswordAuthentication getPasswordAuthentication () {
        return new PasswordAuthentication (username, password.toCharArray());
    }

}
