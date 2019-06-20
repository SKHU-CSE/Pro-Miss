package com.minsudongP.Model;


public class PromissItem {
    private PromissType Type;
    private int user_id;
    private int Notification_id;
    private int Appointment_id;
    private int Notification_send;
    private int isFollowing;
    private String Notification_date;
    private String ProfileImageURl;
    private String Name;
    private String Email;
    private String address;
    private String jibun;
    private String positionX;
    private String positionY;
    private int Appointment_status;

    public int getAppointment_status() {
        return Appointment_status;
    }

    public void setAppointment_status(int appointment_status) {
        Appointment_status = appointment_status;
    }

    public PromissItem(PromissType type, int appointment_id, int notification_id) {
        this.Type = type;
        this.Appointment_id = appointment_id;
        this.Notification_id = notification_id;
    }

    public PromissItem(PromissType type, int notification_id) {
        this.Type = type;
        this.Notification_id = notification_id;
    }

    public PromissItem(PromissType type, int id, String date, String time, String place, int status) {
        if (type == PromissType.Attend_Appoint) {
            this.Appointment_status = status;
            this.Appointment_id = id;
            this.Date = date;
            this.Time = time;
            this.Name = place;
            this.Type = type;
        } else if (type == PromissType.UserList||type==PromissType.MEMBER_ADD) {
            this.Type = type;
            this.user_id = id;
            this.Email = date;
            this.ProfileImageURl = time;
            this.Name = place;
            this.isFollowing = status;
        }
    }

    public int getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(int i) {
        isFollowing = i;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String s) {
        Email = s;
    }


    public int getAppointment_id() {
        return Appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        Appointment_id = appointment_id;
    }

    public int getNotification_send() {
        return Notification_send;
    }

    public void setNotification_send(int notification_send) {
        Notification_send = notification_send;
    }

    public String getNotification_date() {
        return Notification_date;
    }

    public void setNotification_date(String notification_date) {
        Notification_date = notification_date;
    }

    public String getPtcmember() {
        return ptcmember;
    }

    public void setPtcmember(String ptcmember) {
        this.ptcmember = ptcmember;
    }

    public String getScsmember() {
        return scsmember;
    }

    public void setScsmember(String scsmember) {
        this.scsmember = scsmember;
    }

    public String getMymoney() {
        return mymoney;
    }

    public void setMymoney(String mymoney) {
        this.mymoney = mymoney;
    }

    public String getAllmoney() {
        return allmoney;
    }

    public void setAllmoney(String allmoney) {
        this.allmoney = allmoney;
    }

    public int getNotification_id() {
        return Notification_id;
    }

    public void setNotification_id(int notification_id) {
        Notification_id = notification_id;
    }

    // type, int, string, string
    public PromissItem(PromissType Type, int id, String Profileimage, String Name) { //FrendList
        this.user_id = id;
        this.Type = Type;
        this.ProfileImageURl = Profileimage;
        this.Name = Name;
    }

    private String Date;
    private String Time;
    private String Money;
    private String Member; //지각한 멤버
    private String Place;
    private String ptcmember; //참여한 인원
    private String scsmember; //달성한 인원
    private String mymoney; //내 기부금
    private String allmoney; //총 기부금


    // type, string, string, string, string, string
    public PromissItem(PromissType type, String addressORdate, String jibunORtime, String positionXORPlace, String positionYORmoneyORnameORMember) {
        if (type == PromissType.SearchList) {
            this.Type = type;
            this.jibun = jibunORtime;
            this.address = addressORdate;
            this.positionX = positionXORPlace;
            this.positionY = positionYORmoneyORnameORMember;

        } else if(type==PromissType.MEMBER_LIST) {
            this.Type=type;
            this.ProfileImageURl=addressORdate;
            this.positionX=jibunORtime;
            this.positionY=positionXORPlace;
            this.Name=positionYORmoneyORnameORMember;
        }else if (type == PromissType.Time_Late) {
            this.Date = addressORdate;
            this.Time = jibunORtime;
            this.Place = positionXORPlace;
            this.Type = type;
            this.Money = positionYORmoneyORnameORMember;
        } else if (type == PromissType.Accept || type == PromissType.Cancel) {
            this.Name = positionYORmoneyORnameORMember;
            this.Date = addressORdate;
            this.Time = jibunORtime;
            this.Place = positionXORPlace;
            this.Type = type;
        } else if (type == PromissType.Late_Member) {
            this.Type = type;
            this.Date = addressORdate;
            this.Time = jibunORtime;
            this.Place = positionXORPlace;
            this.Member = positionYORmoneyORnameORMember;
        } else if (type == PromissType.Follow) {
            this.Type = type;
            this.Name = positionYORmoneyORnameORMember;
        }
    }

    // type, string, string, string
    public PromissItem(PromissType type, String date, String time, String place) { //New Appoint, Appoint_Start, Attend_Appoint
        if (type == PromissType.New_Appoint) {
            this.Date = date;
            this.Time = time;
            this.Place = place;
            this.Type = type;
        } else if (type == PromissType.Attend_Appoint) {
            this.Date = date;
            this.Time = time;
            this.Name = place;
            this.Type = type;
        } else {
            this.Date = date;
            this.Time = time;
            this.Place = place;
            this.Type = type;
        }
    }

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

    // type, int, int, string, string, string, string
    public PromissItem(PromissType type, int notification_id, int notification_send, String notification_date, String addressORdate, String jibunORtime, String positionXORPlace, String positionYORmoneyORnameORMember) {
        this.Notification_id = notification_id;
        this.Notification_send = notification_send;
        this.Notification_date = notification_date;
        if (type == PromissType.Time_Late) {
            this.Date = addressORdate;
            this.Time = jibunORtime;
            this.Place = positionXORPlace;
            this.Type = type;
            this.Money = positionYORmoneyORnameORMember;
        } else if (type == PromissType.Accept || type == PromissType.Cancel) {
            this.Name = positionYORmoneyORnameORMember;
            this.Date = addressORdate;
            this.Time = jibunORtime;
            this.Place = positionXORPlace;
            this.Type = type;
        } else if (type == PromissType.Late_Member) {
            this.Type = type;
            this.Date = addressORdate;
            this.Time = jibunORtime;
            this.Place = positionXORPlace;
            this.Member = positionYORmoneyORnameORMember;
        } else if (type == PromissType.Follow) {
            this.Type = type;
            this.Name = positionYORmoneyORnameORMember;
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

    // type, int, int, int, string, string, string, string
    public PromissItem(PromissType type, int notification_id, int notification_send, int appointment_id, String notification_date, String date, String time, String place) { //New Appoint, Appoint_Start
        this.Notification_date = notification_date;
        this.Notification_send = notification_send;
        this.Appointment_id = appointment_id;
        if (type == PromissType.New_Appoint) {
            this.Notification_id = notification_id;
            this.Date = date;
            this.Time = time;
            this.Place = place;
            this.Type = type;
        } else {
            this.Notification_id = notification_id;
            this.Date = date;
            this.Time = time;
            this.Place = place;
            this.Type = type;
        }
    }

    public PromissItem(PromissType type, String place, String date, String ptcmember, String scsmember, String mymoney, String allmoney) { //Past_Appoint
        if (type == PromissType.Past_Appoint) {
            this.Type = type;
            this.Name = place;
            this.Date = date;
            this.ptcmember = ptcmember;
            this.scsmember = scsmember;
            this.mymoney = mymoney;
            this.allmoney = allmoney;
        }
    }
}
