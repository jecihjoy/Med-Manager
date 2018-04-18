package com.example.jecihjoy.medmanager.model;

/**
 * Created by Jecihjoy on 4/11/2018.
 */

public class Users {
    private String usersId;
    private String pName, gName, pgName;

    public Users(String pname, String gname, String pgname, String email){
        usersId = email;
        pName = pname;
        gName = gname;
        pgName = pgname;
    }

    public String getUsersId() {
        return usersId;
    }

    public String getpName() {
        return pName;
    }

    public String getgName() {
        return gName;
    }

    public String getPgName() {
        return pgName;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public void setPgName(String pgName) {
        this.pgName = pgName;
    }
}
