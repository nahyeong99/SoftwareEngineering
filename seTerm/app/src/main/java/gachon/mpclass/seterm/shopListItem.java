package gachon.mpclass.seterm;

public class shopListItem {
    private String name;
    private String detailAddress;
    private String distance;
    private String uid;

    public void setName(String name){
        this.name = name;
    }
    public void setDetailAddress(String detailAddress){
        this.detailAddress = detailAddress;
    }
    public void setDistance(String distance) {
        this.distance = distance ;
    }
    public void setUid(String uid){this.uid = uid;}


    public String getName() {
        return this.name;
    }
    public String getDetailAddress(){
        return this.detailAddress;
    }
    public String getDistance() {
        return this.distance;
    }
    public String getUid() {
        return this.uid;
    }
}
