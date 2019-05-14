package com.minsudongP;


enum PromissType { FriendLIst ,SearchList  }

public class PromissItem {
    private PromissType Type;
    private String ProfileImageURl;
    private String name;
    private String address;
    private String jibun;
    private String positionX;
    private String positionY;

    public PromissItem(PromissType Type,String Profileimage,String name)//FrendList
    {
        this.Type=Type;
        this.ProfileImageURl=Profileimage;
        this.name=name;
    }

    public String getJibun() {
        return jibun;
    }

    public void setJibun(String jibun) {
        this.jibun = jibun;
    }

    public PromissItem(PromissType type, String address, String jibun, String positionX, String positionY)
    {
        this.Type=type;
        this.jibun=jibun;
        this.address=address;
        this.positionX=positionX;
        this.positionY=positionY;
    }

    public String getAddress() {
        return address;
    }

    public String getPositionX() {
        return positionX;
    }

    public void setPositionX(String positionX) {
        this.positionX = positionX;
    }

    public String getPositionY() {
        return positionY;
    }

    public void setPositionY(String positionY) {
        this.positionY = positionY;
    }

    public void setAddress(String address) {
        this.address = address;
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
