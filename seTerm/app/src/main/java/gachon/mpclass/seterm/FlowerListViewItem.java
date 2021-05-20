package gachon.mpclass.seterm;

public class FlowerListViewItem {
    private String flowername ;
    private String flowercolor;
    private String imgUrl;
    private String flowernumber;
    private String uid;
    private String fileName;

    FlowerListViewItem(){

    }

    public FlowerListViewItem(String flowername,String flowercolor,String imgUrl,String flowernumber,String uid,String fileName){
        this.flowername = flowername;
        this.flowercolor = flowercolor;
        this.flowernumber = flowernumber;
        this.imgUrl = imgUrl;
        this.uid = uid;
        this.fileName = fileName;
    }
    public String getFlowername(){return flowername;}
    public String getFlowercolor(){return flowercolor;}
    public String getFlowernumber(){return flowernumber;}
    public String getImgUrl(){return imgUrl;}
    public String getUid(){return uid;}
    public String getFileName(){return fileName;}

    public void setFlowername(String flowername){ this.flowername = flowername;}
    public void setFlowercolor(String flowercolor){ this.flowercolor = flowercolor;}
    public void setFlowernumber(String flowernumber){this.flowernumber = flowernumber;}
    public void setUid(){ this.uid = uid;}
    public void setImgUrl(String imgUrl){this.imgUrl=imgUrl;}

}