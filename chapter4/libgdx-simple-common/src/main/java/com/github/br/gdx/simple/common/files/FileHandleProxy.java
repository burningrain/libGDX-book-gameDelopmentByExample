package com.github.br.gdx.simple.common.files;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.br.gdx.simple.common.DirectoryManager;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.Properties;

public class FileHandleProxy extends FileHandle {

    private final FileHandle delegate;

    public FileHandleProxy(FileHandle delegate) {
        this.delegate = delegate;
    }

    public String path() {
        return delegate.path();
    }

    public String name() {
        return delegate.name();
    }

    public String extension() {
        return delegate.extension();
    }

    public String nameWithoutExtension() {
        return delegate.nameWithoutExtension();
    }

    public String pathWithoutExtension() {
        return delegate.pathWithoutExtension();
    }

    public Files.FileType type() {
        return delegate.type();
    }

    public File file() {
        return delegate.file();
    }

    public InputStream read() {
        return delegate.read();
    }

    public BufferedInputStream read(int bufferSize) {
        return delegate.read(bufferSize);
    }

    public Reader reader() {
        return delegate.reader();
    }

    public Reader reader(String charset) {
        return delegate.reader(charset);
    }

    public BufferedReader reader(int bufferSize) {
        return delegate.reader(bufferSize);
    }

    public BufferedReader reader(int bufferSize, String charset) {
        return delegate.reader(bufferSize, charset);
    }

    public String readString() {
        return delegate.readString();
    }

    public String readString(String charset) {
        return delegate.readString(charset);
    }

    public byte[] readBytes() {
        return delegate.readBytes();
    }

    public int readBytes(byte[] bytes, int offset, int size) {
        return delegate.readBytes(bytes, offset, size);
    }

    public ByteBuffer map() {
        return delegate.map();
    }

    public ByteBuffer map(FileChannel.MapMode mode) {
        return delegate.map(mode);
    }

    public OutputStream write(boolean append) {
        return delegate.write(append);
    }

    public OutputStream write(boolean append, int bufferSize) {
        return delegate.write(append, bufferSize);
    }

    public void write(InputStream input, boolean append) {
        delegate.write(input, append);
    }

    public Writer writer(boolean append) {
        return delegate.writer(append);
    }

    public Writer writer(boolean append, String charset) {
        return delegate.writer(append, charset);
    }

    public void writeString(String string, boolean append) {
        writeString(string, append, null);
    }

    public void writeString(String string, boolean append, String charset) {
        delegate.writeString(string, append, charset);
    }

    public void writeBytes(byte[] bytes, boolean append) {
        delegate.writeBytes(bytes, append);
    }

    public void writeBytes(byte[] bytes, int offset, int length, boolean append) {
        delegate.writeBytes(bytes, offset, length, append);
    }

    // TODO все было ради этого!!!
    public FileHandle[] list() {
        FileHandle child = child(DirectoryManager.FILELIST_TITLE);
        if (!child.exists()) {
            return null;
        }
        Properties prop = new Properties();
        try {
            prop.load(child.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Enumeration<?> enumeration = prop.keys();
        Array<String> names = new Array<String>();
        while (enumeration.hasMoreElements()) {
            names.add((String) enumeration.nextElement());
        }
        Object[] shrinkedNames = names.shrink();
        FileHandle[] result = new FileHandle[shrinkedNames.length];
        int i = 0;
        for (Object propertyName : shrinkedNames) {
            result[i] = new FileHandleProxy(delegate.child((String) propertyName));
            i++;
        }

        return result;
    }

    public FileHandle[] list(FileFilter filter) {
        FileHandle[] list = this.list();
        if (list == null || list.length == 0) {
            return null;
        }

        Array<FileHandle> result = new Array<FileHandle>();
        for (FileHandle fileHandle : list) {
            if (filter.accept(fileHandle.file())) {
                result.add(fileHandle);
            }
        }
        return result.items;
    }

    public FileHandle[] list(FilenameFilter filter) {
        FileHandle[] list = this.list();
        if (list == null || list.length == 0) {
            return null;
        }

        Array<FileHandle> result = new Array<FileHandle>();
        for (FileHandle fileHandle : list) {
            if (filter.accept(this.delegate.file(), fileHandle.name())) {
                result.add(fileHandle);
            }
        }
        return result.items;
    }

    public FileHandle[] list(String suffix) {
        FileHandle[] list = this.list();
        if (list == null || list.length == 0) {
            return null;
        }

        Array<FileHandle> result = new Array<FileHandle>();
        for (FileHandle fileHandle : list) {
            if (fileHandle.name().endsWith(suffix)) {
                result.add(fileHandle);
            }
        }
        return result.items;
    }

    public boolean isDirectory() {
        FileHandle child = child(DirectoryManager.FILELIST_TITLE);
        return child.exists();
    }
    //TODO все было ради этого!!!

    public FileHandleProxy child(String name) {
        return new FileHandleProxy(delegate.child(name));
    }

    public FileHandleProxy sibling(String name) {
        return new FileHandleProxy(delegate.sibling(name));
    }

    public FileHandleProxy parent() {
        return new FileHandleProxy(delegate.parent());
    }

    public void mkdirs() {
        delegate.mkdirs();
    }

    public boolean exists() {
        return delegate.exists();
    }

    public boolean delete() {
        return delegate.delete();
    }

    public boolean deleteDirectory() {
        return delegate.deleteDirectory();
    }

    public void emptyDirectory() {
        delegate.emptyDirectory();
    }

    public void emptyDirectory(boolean preserveTree) {
        delegate.emptyDirectory(preserveTree);
    }

    public void copyTo(FileHandle dest) {
        delegate.moveTo(dest);
    }

    public void moveTo(FileHandle dest) {
        delegate.moveTo(dest);
    }

    public long length() {
        return delegate.length();
    }

    public long lastModified() {
        return delegate.lastModified();
    }

    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public String toString() {
        return delegate.toString();
    }

}
