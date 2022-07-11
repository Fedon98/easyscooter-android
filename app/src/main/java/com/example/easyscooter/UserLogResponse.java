package com.example.easyscooter;

import java.io.Serializable;

public class UserLogResponse implements Serializable {

    private int idTrotinette;
    private String firstname, lastname;

    public UserLogResponse() {

    }

    public int getIdTrotinette() {
        return idTrotinette;
    }

    public void setIdTrotinette(int idTrotinette) {
        this.idTrotinette = idTrotinette;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public UserLogResponse(int idTrotinette, String firstname, String lastname) {
        this.idTrotinette = idTrotinette;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
