package com.gbm.vo;

import java.io.Serializable;

/**
 * Created by Vinoth on 9/6/2017.
 */

public class UserVO implements Serializable {

    private long id;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private String createdDt;

    public UserVO() {

    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the createdDt
     */
    public String getCreatedDt() {
        return createdDt;
    }

    /**
     * @param createdDt the createdDt to set
     */
    public void setCreatedDt(String createdDt) {
        this.createdDt = createdDt;
    }

}
