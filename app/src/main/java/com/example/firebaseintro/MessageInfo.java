package com.example.firebaseintro;

public class MessageInfo {
   String msg;

    public MessageInfo()
    {
        msg="";
    }

    public MessageInfo(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
