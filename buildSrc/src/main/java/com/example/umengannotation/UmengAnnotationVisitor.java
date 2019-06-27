package com.example.umengannotation;

import org.objectweb.asm.AnnotationVisitor;

import java.util.HashMap;

/**
 * Created by zjy on 2019-05-11
 */
public class UmengAnnotationVisitor extends AnnotationVisitor {
    private AnnotationBean annotationBean;
    public UmengAnnotationVisitor(int api, AnnotationVisitor av) {
        super(api, av);
        annotationBean=new AnnotationBean();
    }

    public AnnotationBean getAnnotationBean() {
        return annotationBean;
    }

    @Override
    public void visit(String name, Object value) {
        super.visit(name, value);
        if(name.equals("UmengKey")){
            annotationBean.setUmengKey(value.toString());
        }
        if(name.equals("UmengContent")){
            annotationBean.setUmengContent(value.toString());
        }
        System.out.println("visit-->"+name+"-"+value);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        super.visitEnum(name, desc, value);
        if(name.equals("lifecycle")){
            annotationBean.setLifecycle(value);
        }
        System.out.println("visitEnum-->"+name+"-"+value);
    }
}
