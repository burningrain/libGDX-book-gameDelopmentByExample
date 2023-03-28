package com.github.br.gdx.simple.animation.generator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.AnimationExtensions;
import com.github.br.gdx.simple.animation.io.JsonConverter;
import com.github.br.gdx.simple.animation.io.Utils;
import com.github.br.gdx.simple.animation.io.dto.AnimationDto;
import com.github.br.gdx.simple.animation.io.dto.FsmDto;
import com.github.br.gdx.simple.animation.io.dto.TransitionDto;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

import static com.github.br.gdx.simple.animation.io.Utils.createClassName;

/**
 * Именно этот класс завязан на JDK. Поэтому выделен в отдельную библиотеку для редактора
 */
public class TransitionJavaClassGenerator {

    public static final TransitionJavaClassGenerator INSTANCE = new TransitionJavaClassGenerator();

    private TransitionJavaClassGenerator() {
    }

    //TODO !!! сначала вызывается генератор классов, потом уже DirectoryManager для создания списка файликов
    public static void main(String[] args) {
        INSTANCE.generateClasses(
                Utils.toString(TransitionJavaClassGenerator.class.getResourceAsStream("/animation.json")),
                "D:\\projects\\libGDX-book-gameDelopmentByExample\\chapter4\\core\\assets",
                "1.5",
                "1.5"
        );

    }

    public void generateClasses(String json, String destination, String sourceVersion, String targetVersion) {
        try {
            generate(json, destination, sourceVersion, targetVersion);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void generate(String json, String destination, String sourceVersion, String targetVersion) throws IOException {
        JsonConverter jsonConverter = new JsonConverter();
        AnimationDto animationDto = jsonConverter.from(json);

        String packageName = animationDto.getName();

        Array<JavaFileObject> javaClasses = new Array<JavaFileObject>();
        FsmDto fsm = animationDto.getFsm();
        for (TransitionDto transition : fsm.getTransitions()) {
            String className = createClassName(transition);
            javaClasses.add(
                    createJavaFileObject(
                            className,
                            createJavaFileBody(packageName, className, transition.getFsmPredicate()))
            );
        }

        ObjectMap<String, FsmDto> subFsm = animationDto.getSubFsm();
        if(subFsm != null) {
            for (FsmDto value : subFsm.values()) {
                for (TransitionDto transition : value.getTransitions()) {
                    String className = createClassName(transition);
                    javaClasses.add(
                            createJavaFileObject(
                                    className,
                                    createJavaFileBody(packageName, className, transition.getFsmPredicate()))
                    );
                }
            }
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, Charset.forName("UTF-8"));

        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                fileManager,
                null,
                Arrays.asList( new String[] {
                        "-d", destination,
                        "-source", sourceVersion,
                        "-target", targetVersion
                }),
                null,
                javaClasses
        );
        task.call();

        fileManager.close();
    }

    private JavaFileObject createJavaFileObject(String className, String javaClazz) {
        return new JavaSourceFromString(className, javaClazz);
    }


    private String createJavaFileBody(String packageName, String className, String predicateBody) {
        StringBuilder builder = new StringBuilder();
        builder
                .append("package ").append(AnimationExtensions.ANIMATION_PACKAGE).append(packageName).append(";").append("\n")
                .append("import com.github.br.gdx.simple.animation.fsm.FsmContext;\n")
                .append("import com.github.br.gdx.simple.animation.fsm.FsmPredicate;\n")
                .append("public class ").append(className).append(" implements FsmPredicate {\n")
                .append("@Override\n")
                .append("public boolean predicate(FsmContext context) {\n")
                .append(predicateBody)
                .append("}\n")
                .append("}\n");

        return builder.toString();
    }

}
