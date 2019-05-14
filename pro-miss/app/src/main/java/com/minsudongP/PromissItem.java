package com.minsudongP;


enum PromissType { FriendLIst  ,New_Appoint,Appoint_START, Time_Late, Accept,Cancel,Late_Member,Follow  }

public class PromissItem {
    private PromissType Type;
    private String ProfileImageURl;
    private String name;
    private String Date;
    private String Time;
    private String Money;
    private String Member; //지각한 멤버
    private String Place;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getMember() {
        return Member;
    }

    public void setMember(String member) {
        Member = member;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public PromissItem(PromissType Type, String Profileimage, String name)
    {
        this.Type=Type;
        this.ProfileImageURl=Profileimage;
        this.name=name;
    }

    public PromissItem(PromissType type,String date,String time,String place) //New Appoint
    {
        this.Date=date;
        this.Time=time;
        this.Place=place;
        this.Type=type;
    }

    public PromissType getType() {
        return Type;
    }

    public void setType(PromissType type) {
        Type = type;
    }

    public String getProfileImageURl() {
        return ProfileImageURl;
    }

    public void setProfileImageURl(String profileImageURl) {
        ProfileImageURl = profileImageURl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
