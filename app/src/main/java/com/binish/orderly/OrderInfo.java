package com.binish.orderly;

public class OrderInfo {
    private String orderitem;

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(String finishdate) {
        this.finishdate = finishdate;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    private String orderdate;
    private String finishdate;
    private String finishtime;

    public String getRemindbefore() {
        return remindbefore;
    }

    public void setRemindbefore(String remindbefore) {
        this.remindbefore = remindbefore;
    }

    private String remindbefore;

    public String getRemindbeforecustomer() {
        return remindbeforecustomer;
    }

    public void setRemindbeforecustomer(String remindbeforecustomer) {
        this.remindbeforecustomer = remindbeforecustomer;
    }

    private String remindbeforecustomer;
    private int companyid;
    private int customerid;

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    private int orderid;

    public String getOrderitem() {return orderitem;}
        public void setOrderitem(String orderitem){this.orderitem=orderitem;}




    public int getCompanyId(){
            return companyid;
    }

    public void setCompanyId(int companyid)
    {
        this.companyid = companyid;

    }

    public int getCustomerid()
    {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }
}
