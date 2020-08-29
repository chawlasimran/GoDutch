package com.example.khushbhu.go_dutch;

public class Balance {

    private String desc;
    private String bal;
    String balid;

    public Balance(){

    }

    public Balance(String balid,String desc,String bal){
        this.balid=balid;
        this.desc=desc;
        this.bal=bal;
    }

    public String getBalid() {
        return balid;
    }

    public String getDesc() {
        return desc;
    }

    public String getBal() {
        return bal;
    }
}
