/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.github.angel.entity;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 *
 * @author aguero
 */
public class User implements Serializable {
    @Serial
    private static final Long serialVersionUID = -1725152912519263139L;
    private String email;
    @JsonIgnore
    private String password;
   
    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
      
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    

   

}
