package com.example.umengasmdemo;

/**
 * Created by zjy on 2019-05-11
 */
public class AnnotationBean {
    private String umengKey;
    private String umengContent;
    private String lifecycle;

    public String getUmengKey() {
        return umengKey;
    }

    public void setUmengKey(String umengKey) {
        this.umengKey = umengKey;
    }

    public String getUmengContent() {
        return umengContent;
    }

    public void setUmengContent(String umengContent) {
        this.umengContent = umengContent;
    }

    public String getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(String lifecycle) {
        this.lifecycle = lifecycle;
    }

    @Override
    public String toString() {
        return umengKey+"-"+umengContent+"-"+lifecycle;
    }
}
