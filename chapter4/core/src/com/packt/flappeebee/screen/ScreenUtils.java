package com.packt.flappeebee.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public final class ScreenUtils {

    public static final String FONT_CHARS = new StringBuilder().append(FreeTypeFontGenerator.DEFAULT_CHARS)
        .append("\u0410").append("\u0430") // Аа
        .append("\u0411").append("\u0431") // Бб
        .append("\u0412").append("\u0432") // Вв
        .append("\u0413").append("\u0433") // Гг
        .append("\u0414").append("\u0434") // Дд
        .append("\u0415").append("\u0435") // Ее
        .append("\u0401").append("\u0451") // Ёё
        .append("\u0416").append("\u0436") // Жж
        .append("\u0417").append("\u0437") // Зз
        .append("\u0418").append("\u0438") // Ии
        .append("\u0419").append("\u0439") // Йй
        .append("\u041A").append("\u043A") // Кк
        .append("\u041B").append("\u043B") // Лл
        .append("\u041C").append("\u043C") // Мм
        .append("\u041D").append("\u043D") // Нн
        .append("\u041E").append("\u043E") // Оо
        .append("\u041F").append("\u043F") // Пп
        .append("\u0420").append("\u0440") // Рр
        .append("\u0421").append("\u0441") // Сс
        .append("\u0422").append("\u0442") // Тт
        .append("\u0423").append("\u0443") // Уу
        .append("\u0424").append("\u0444") // Фф
        .append("\u0425").append("\u0445") // Хх
        .append("\u0426").append("\u0446") // Цц
        .append("\u0427").append("\u0447") // Чч
        .append("\u0428").append("\u0448") // Шш
        .append("\u0429").append("\u0449") // Щщ
        .append("\u042A").append("\u044A") // Ъъ
        .append("\u042B").append("\u044B") // Ыы
        .append("\u042C").append("\u044C") // Ьь
        .append("\u042D").append("\u044D") // Ээ
        .append("\u042E").append("\u044E") // Юю
        .append("\u042F").append("\u044F") // Яя
        .toString();

    private ScreenUtils() {
    }

    public static Label createLabel(String text, Label.LabelStyle labelStyle) {
        return createLabel(text, labelStyle, Align.left, Align.left);
    }

    public static Label createLabel(String text, Label.LabelStyle labelStyle, int labelAlign, int lineAlign) {
        Label result = new Label(text, labelStyle);
        result.setAlignment(labelAlign, lineAlign);
        result.pack();

        return result;
    }

    public static String wrapStringForTextLabel(String text, int symbolWidth, int virtualWidth) {
        int charWrap = Gdx.graphics.getWidth() * symbolWidth / virtualWidth;
        return ScreenUtils.wrapString(text, charWrap);
    }

    public static String wrapString(String text, int charWrap) {
        if (charWrap == 0) {
            return text;
        }

        int lastBreak = 0;
        int nextBreak = charWrap;
        if (text.length() > charWrap) {
            StringBuilder setString = new StringBuilder();
            do {
                while (text.charAt(nextBreak) != ' ' && nextBreak > lastBreak) {
                    nextBreak--;
                }
                if (nextBreak == lastBreak) {
                    nextBreak = lastBreak + charWrap;
                }
                setString.append(text.substring(lastBreak, nextBreak).trim()).append("\n");
                lastBreak = nextBreak;
                nextBreak += charWrap;

            } while (nextBreak < text.length());
            setString.append(text.substring(lastBreak).trim());
            return setString.toString();
        } else {
            return text;
        }
    }

    public static BitmapFont generateBitmapFont(FileHandle fileHandle, Color color, int size) {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(fileHandle);
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = size;
        fontParameter.color = color;
        fontParameter.characters = FONT_CHARS;

        return fontGenerator.generateFont(fontParameter);
    }


}
