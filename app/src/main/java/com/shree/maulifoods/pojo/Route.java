package com.shree.maulifoods.pojo;

public class Route {

    private String sr_No, route_ID,route_Name,route_Desc, assign_Emp, designation,mobile_No;

    public Route(String sr_No, String route_ID, String route_Name, String route_Desc, String assign_Emp, String designation, String mobile_No) {
        this.sr_No = sr_No;
        this.route_ID = route_ID;
        this.route_Name = route_Name;
        this.route_Desc = route_Desc;
        this.assign_Emp = assign_Emp;
        this.designation = designation;
        this.mobile_No = mobile_No;
    }

    public String getSr_No() {
        return sr_No;
    }

    public void setSr_No(String sr_No) {
        this.sr_No = sr_No;
    }

    public String getRoute_ID() {
        return route_ID;
    }

    public void setRoute_ID(String route_ID) {
        this.route_ID = route_ID;
    }

    public String getRoute_Name() {
        return route_Name;
    }

    public void setRoute_Name(String route_Name) {
        this.route_Name = route_Name;
    }

    public String getRoute_Desc() {
        return route_Desc;
    }

    public void setRoute_Desc(String route_Desc) {
        this.route_Desc = route_Desc;
    }

    public String getAssign_Emp() {
        return assign_Emp;
    }

    public void setAssign_Emp(String assign_Emp) {
        this.assign_Emp = assign_Emp;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMobile_No() {
        return mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        this.mobile_No = mobile_No;
    }
}
