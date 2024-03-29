package com.exmple.umeng

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.example.umengannotation.UmengClassVisitor
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

public class UMengEventPlugin extends Transform implements Plugin<Project> {

    @Override
    String getName() {
        return "UMengEventPlugin.groovy"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void apply(Project project) {
        project.extensions.getByType(AppExtension).registerTransform(this)
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        transformInvocation.inputs.each {
            it.directoryInputs.each {
                //扫描所有文件
                if (it.file.isDirectory()) {
                    it.file.eachFileRecurse { file ->
                        def fileName=file.name
                        if (fileName != "R.class" && !fileName.startsWith("R\$")
                                && fileName != "BuildConfig.class" && fileName.endsWith(".class")) {
                            handle(file)
                        }
                    }
                }
                def dest = transformInvocation.outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(it.file, dest)
            }

            it.jarInputs.each { jarInput->
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def dest = transformInvocation.outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
    }

    void handle(File file) {
        def cr = new ClassReader(file.bytes)
        def cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        def umengCV = new UmengClassVisitor(Opcodes.ASM5, cw)
        cr.accept(umengCV, ClassReader.EXPAND_FRAMES)
        def bytes = cw.toByteArray()
        def fos = new FileOutputStream(file.getParentFile().absolutePath + File.separator + file.name)
        fos.write(bytes)
        fos.flush()
        fos.close()
    }
}