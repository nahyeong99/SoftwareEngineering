package gachon.mpclass.seterm;

public class Manager {
    public FlowerListViewItem flower;
    public String address;
    public String email;
    public String latitude;
    public String license;
    public String name;
    public String phonenumber;
    public String longitude;
    public String nickname;
    public String time;
    public Manager(){
        //default
    }
    public Manager(String name,String email,String phonenumber,String license,String address,String time){
        this.name = name;
        this.email = email;
        this.address = address;
        this.license = license;
        this.phonenumber = phonenumber;
        this.time = time;
    }
    public Manager(String address,String email,String latitude,String license,String longitude,String name,String nickname,String phonenumber,String time){
        this.name = name;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nickname = nickname;
        this.address = address;
        this.license = license;
        this.phonenumber = phonenumber;
        this.time = time;
    }
    public String getUid(){
        return nickname;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

}
