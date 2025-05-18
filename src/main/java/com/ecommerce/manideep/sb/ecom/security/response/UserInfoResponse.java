package com.ecommerce.manideep.sb.ecom.security.response;

import java.util.List;

public class UserInfoResponse {
    private Long id ;
    private String jwtToken ;
    private String username;
    private List<String> roles ;

    public UserInfoResponse(Long id , String jwtToken, String userName, List<String> roles) {
        this.id = id ;
        this.jwtToken = jwtToken;
        this.username = userName;
        this.roles = roles;
    }
    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
