package com.minsudongP;


enum PromissType { FriendLIst  }

public class PromissItem {
    private PromissType Type;
    private String ProfileImageURl;
    private String name;

    public PromissItem(PromissType Type,String Profileimage,String name)
    {
        this.Type=Type;
        this.ProfileImageURl=Profileimage;
        this.name=name;
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
