package com.github.br.gdx.simple.animation.io;


import java.util.HashSet;

public class AnimationClassloader {

    //private final HashSet<String> loadedClasses = new HashSet<String>();
    private byte[] loadedClass;
    private String loadedClassName;

    public void setLoadedClass(byte[] loadedClass) {
        this.loadedClass = loadedClass;
    }

    public void setLoadedClassName(String loadedClassName) {
        this.loadedClassName = loadedClassName;
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(name);
            //return defineClass(loadedClassName, loadedClass, 0, loadedClass.length);
        } catch (Exception e) {
            throw new AnimationLoadingException(e);
        }
    }

}
