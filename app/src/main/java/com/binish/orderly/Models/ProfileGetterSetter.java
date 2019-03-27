package com.binish.orderly.Models;

public class ProfileGetterSetter {
    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getOrderitem() {
        return orderitem;
    }

    public void setOrderitem(String orderitem) {
        this.orderitem = orderitem;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    int orderid;
    String customername, orderitem, contactno;
}
