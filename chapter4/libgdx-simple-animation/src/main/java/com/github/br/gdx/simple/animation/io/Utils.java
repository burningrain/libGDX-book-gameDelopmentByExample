package com.github.br.gdx.simple.animation.io;

import java.io.*;
import java.nio.charset.Charset;

public final class Utils {

    public static String toString(InputStream inputStream) {
        try {
            StringBuilder textBuilder = new StringBuilder();
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            try {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }
            } finally {
                reader.close();
            }

            return textBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
