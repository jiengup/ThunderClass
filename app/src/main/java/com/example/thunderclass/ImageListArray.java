package com.example.thunderclass;


/**

 * Created by prize on 2018/4/11.

 */



public class ImageListArray {

    private String useName;
    private String nickName;
    private String url;
    private String className;

    public ImageListArray(String useName, String url,String nickName,String className){

        this.useName = useName;
        this.url = url;
        this.nickName=nickName;
        this.className=className;

    }

    public String getUseName() {
        return useName;
    }
    public String getNickName() {
        return nickName;
    }
    public String getUrl() {
        return url;
    }
    public String getClassName() {
        return className;
    }

}
