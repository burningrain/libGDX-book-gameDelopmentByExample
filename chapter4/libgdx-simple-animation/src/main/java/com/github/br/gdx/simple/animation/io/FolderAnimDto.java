package com.github.br.gdx.simple.animation.io;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ObjectMap;


public class FolderAnimDto {

    private TextureAtlas textureAtlas;
    private String fsm;
    private ObjectMap<String, byte[]> javaClasses = new ObjectMap<String, byte[]>();

    public void setTextureAtlas(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public void setFsm(String fsm) {
        this.fsm = fsm;
    }

    public String getFsm() {
        return fsm;
    }

    public void addJavaClass(String classname, byte[] readBytes) {
        javaClasses.put(classname, readBytes);
    }

    public ObjectMap<String, byte[]> getJavaClasses() {
        return javaClasses;
    }

}
