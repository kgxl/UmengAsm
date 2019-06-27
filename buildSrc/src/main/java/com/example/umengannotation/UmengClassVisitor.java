package com.example.umengannotation;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import javax.naming.Name;

/**
 * Created by zjy on 2019-05-10
 */
public class UmengClassVisitor extends ClassVisitor {

    private String mName;

    public UmengClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.mName = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        mv = new AdviceAdapter(api, mv, access, name, desc) {
            boolean useUmeng = false;
            UmengAnnotationVisitor umengAnnotationVisitor;

            @Override
            protected void onMethodEnter() {
                System.out.println("onMethodEnter-->" + name);
                if (useUmeng && umengAnnotationVisitor != null && umengAnnotationVisitor.getAnnotationBean().getLifecycle().equals(Lifecycle.BEFORE.name())) {
                    AnnotationBean annotationBean = umengAnnotationVisitor.getAnnotationBean();
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitLdcInsn(annotationBean.getUmengKey());
                    mv.visitLdcInsn(annotationBean.getUmengContent());
                    mv.visitMethodInsn(INVOKESTATIC, "com/umeng/analytics/MobclickAgent", "onEvent", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)V", false);

                    Label l0 = new Label();
                    mv.visitLabel(l0);
                    mv.visitInsn(RETURN);
                    Label l2 = new Label();
                    mv.visitLabel(l2);

                    mv.visitLocalVariable("this", "L" + mName + ";", null, l0, l2, 0);
                }
            }

            @Override
            protected void onMethodExit(int opcode) {
                if (useUmeng && umengAnnotationVisitor != null && umengAnnotationVisitor.getAnnotationBean().getLifecycle().equals(Lifecycle.AFTER.name())) {
                    System.out.println("onMethodExit--->" + umengAnnotationVisitor.getAnnotationBean().toString());
                }
            }

            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                System.out.println("AdviceAdapter visitAnnotation--->" + desc);
                if (Type.getDescriptor(Umeng.class).equals(desc)) {
                    useUmeng = true;
                    if (umengAnnotationVisitor == null) {
                        AnnotationVisitor av = mv.visitAnnotation(desc, visible);
                        umengAnnotationVisitor = new UmengAnnotationVisitor(api, av);
                    }
                    return umengAnnotationVisitor;
                }
                return super.visitAnnotation(desc, visible);
            }
        };
        return mv;
    }
}
