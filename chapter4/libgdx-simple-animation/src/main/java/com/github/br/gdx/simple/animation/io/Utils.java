package com.github.br.gdx.simple.animation.io;

import com.github.br.gdx.simple.animation.io.dto.TransitionDto;

import java.io.*;
import java.nio.charset.Charset;

public final class Utils {

    public static String createClassName(TransitionDto transition) {
        return transition.getFrom() + "__" + transition.getTo() + "_Predicate";
    }

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

    public static String getFileExtension(String name) {
        StringBuilder builder = new StringBuilder();
        for(int i = name.length() - 1; i > 0; i--) {
            char c = name.charAt(i);
            if(c == '.') {
                break;
            }
            builder.append(c);
        }
        return builder.reverse().toString();
    }

}
