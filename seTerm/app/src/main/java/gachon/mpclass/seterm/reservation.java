package gachon.mpclass.seterm;

public class reservation {
    public String customerUid;
    public String flowerUid;
    public String num;

    public reservation() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public reservation(String customerUid,String flowerUid,String num) {
        this.customerUid = customerUid;
        this.num = num;
        this.flowerUid =flowerUid;

    }
public String getCustomerUid(){
        return customerUid;
    }
    public String getFlowerUid(){
        return flowerUid;
    }
    public String getNum(){
        return num;
    }
}
