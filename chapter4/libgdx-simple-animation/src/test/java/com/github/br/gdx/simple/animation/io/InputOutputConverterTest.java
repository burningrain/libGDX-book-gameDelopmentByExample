package com.github.br.gdx.simple.animation.io;

import com.github.br.gdx.simple.animation.io.dto.AnimationDto;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

public class InputOutputConverterTest {

    private final InputOutputConverter converter = new InputOutputConverter();

    @Test
    public void testReadingJson() {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/animation.json");
        String json = Utils.toString(resourceAsStream);
        AnimationDto from = converter.from(json);
        Assert.assertNotNull(from);
    }

    @Test
    public void testWritingJson() {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/animation.json");
        String json = Utils.toString(resourceAsStream);
        AnimationDto from = converter.from(json);
        String to = converter.to(from);

        Assert.assertNotNull(to);
        System.out.println(to);
    }


}
