package com.github.br.gdx.simple.common.files;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FilesProxy implements Files {

    @Override
    public FileHandle getFileHandle(String path, FileType type) {
        return Gdx.files.getFileHandle(path, type);
    }

    @Override
    public FileHandle classpath(String path) {
        return new FileHandleProxy(Gdx.files.classpath(path));
    }

    @Override
    public FileHandle internal(String path) {
        return new FileHandleProxy(Gdx.files.internal(path));
    }

    @Override
    public FileHandle external(String path) {
        return new FileHandleProxy(Gdx.files.external(path));
    }

    @Override
    public FileHandle absolute(String path) {
        return new FileHandleProxy(Gdx.files.absolute(path));
    }

    @Override
    public FileHandle local(String path) {
        return new FileHandleProxy(Gdx.files.local(path));
    }

    @Override
    public String getExternalStoragePath() {
        return Gdx.files.getExternalStoragePath();
    }

    @Override
    public boolean isExternalStorageAvailable() {
        return Gdx.files.isExternalStorageAvailable();
    }

    @Override
    public String getLocalStoragePath() {
        return Gdx.files.getLocalStoragePath();
    }

    @Override
    public boolean isLocalStorageAvailable() {
        return Gdx.files.isLocalStorageAvailable();
    }
}
