package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2017/1/12.
 */

public class MessageEvent {
    private int the_type;
    private String the_content;

    public MessageEvent(int the_type, String the_content) {
        this.the_type = the_type;
        this.the_content = the_content;
    }

    public int getThe_type() {
        return the_type;
    }

    public String getThe_content() {
        return the_content;
    }

    public void setThe_type(int the_type) {
        this.the_type = the_type;
    }

    public void setThe_content(String the_content) {
        this.the_content = the_content;
    }
}
