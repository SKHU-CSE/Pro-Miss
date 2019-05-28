package com.minsudongP;


public class PromissItem {
    private PromissType Type;
    private int user_id;
    private String ProfileImageURl;
    private String Name;
    private String address;
    private String jibun;
    private String positionX;
    private String positionY;

    public PromissItem(PromissType Type,int id,String Profileimage,String Name) //FrendList
    {
        this.user_id=id;
        this.Type=Type;
        this.ProfileImageURl=Profileimage;
        this.Name=Name;
    }
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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


    public String getJibun() {
        return jibun;
    }

    public void setJibun(String jibun) {
        this.jibun = jibun;
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
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public PromissItem(PromissType type, String addressORdate, String jibunORtime, String positionXORPlace, String positionYORmoneyORnameORMember)
    {

        if(type==PromissType.SearchList) {
            this.Type = type;
            this.jibun = jibunORtime;
            this.address = addressORdate;
            this.positionX = positionXORPlace;
            this.positionY = positionYORmoneyORnameORMember;
        }else if(type==PromissType.Time_Late)
        {
            this.Date=addressORdate;
            this.Time=jibunORtime;
            this.Place=positionXORPlace;
            this.Type=type;
            this.Money=positionYORmoneyORnameORMember;
        }
        else if(type==PromissType.Accept||type==PromissType.Cancel)
        {
            this.Name=positionYORmoneyORnameORMember;
            this.Date=addressORdate;
            this.Time=jibunORtime;
            this.Place=positionXORPlace;
            this.Type=type;
        }else if(type==PromissType.Late_Member)
        {
            this.Type=type;
            this.Date=addressORdate;
            this.Time=jibunORtime;
            this.Place=positionXORPlace;
            this.Member=positionYORmoneyORnameORMember;
        }
        else if(type==PromissType.Follow)
        {
            this.Type=type;
            this.Name=positionYORmoneyORnameORMember;
        }


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
    public PromissItem(PromissType type, String date,String time,String place) //New Appoint, Appoint_Start
    {
        if(type==PromissType.New_Appoint) {
            this.Date = date;
            this.Time = time;
            this.Place = place;
            this.Type = type;
        }
        else {
            this.Date = date;
            this.Time = time;
            this.Place = place;
            this.Type = type;
        }
    }



}
