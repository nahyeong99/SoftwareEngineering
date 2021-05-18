package gachon.mpclass.seterm;

public class Manager {
    public String email;
    public String name;
    public String phonenumber;
    public String address;
    public String license;
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
}
