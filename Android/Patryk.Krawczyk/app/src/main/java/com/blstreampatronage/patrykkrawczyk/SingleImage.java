package com.blstreampatronage.patrykkrawczyk;

public class SingleImage {

    private String title = "";
    private String desc  = "";
    private String url   = "";

    public SingleImage() { }
    public SingleImage(String t, String d, String u) {
        title = t;
        desc  = d;
        url   = u;
    }

    public String getTitle()       { return title; }
    public String getDesc()        { return desc;  }
    public String getUrl()         { return url;   }

    public void setTitle(String t) { title = t; }
    public void setDesc(String d)  { desc  = d; }
    public void setUrl(String u)   { url   = u; }
}
