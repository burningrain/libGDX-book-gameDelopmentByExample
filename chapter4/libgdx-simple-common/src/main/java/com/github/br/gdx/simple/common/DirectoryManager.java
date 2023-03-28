package com.github.br.gdx.simple.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public final class DirectoryManager {

    public static String FILELIST_TITLE = "filelist.txt";
    public static String DIRECTORY = "directory";
    public static String FILE = "file";

    private DirectoryManager() {
    }

    public static void main(String[] args) {
        File file = new File("D:\\projects\\libGDX-book-gameDelopmentByExample\\chapter4\\core\\assets");
        DirectoryManager.createFileList(file);
    }

    public static void createFileList(File folder) {
        StringBuilder builder = new StringBuilder();
        File[] files = folder.listFiles();
        if(files != null) {
            for (final File fileEntry : files) {
                if (fileEntry.isDirectory()) {
                    createFileList(fileEntry);
                }
                builder.append(fileEntry.getName()).append("=").append(fileEntry.isDirectory()? DIRECTORY : FILE).append("\n");
            }
        }
        createFile(new File(folder, FILELIST_TITLE),  builder.toString());
    }

    public static void createFile(File file, String body) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file, "UTF-8");
            writer.write(body);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(writer != null) {
                writer.close();
            }
        }
    }

}
