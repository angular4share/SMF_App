package com.shree.maulifoods.pojo;

public class Expense {

    private String srNo,expense_No,expense_Dt,expense_ID,expense_Name,expense_Amount,user_Name,remark;

    public Expense(String srNo, String expense_No, String expense_Dt, String expense_ID, String expense_Name, String expense_Amount, String user_Name, String remark) {
        this.srNo = srNo;
        this.expense_No = expense_No;
        this.expense_Dt = expense_Dt;
        this.expense_ID = expense_ID;
        this.expense_Name = expense_Name;
        this.expense_Amount = expense_Amount;
        this.user_Name = user_Name;
        this.remark = remark;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getExpense_No() {
        return expense_No;
    }

    public void setExpense_No(String expense_No) {
        this.expense_No = expense_No;
    }

    public String getExpense_Dt() {
        return expense_Dt;
    }

    public void setExpense_Dt(String expense_Dt) {
        this.expense_Dt = expense_Dt;
    }

    public String getExpense_ID() {
        return expense_ID;
    }

    public void setExpense_ID(String expense_ID) {
        this.expense_ID = expense_ID;
    }

    public String getExpense_Name() {
        return expense_Name;
    }

    public void setExpense_Name(String expense_Name) {
        this.expense_Name = expense_Name;
    }

    public String getExpense_Amount() {
        return expense_Amount;
    }

    public void setExpense_Amount(String expense_Amount) {
        this.expense_Amount = expense_Amount;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
