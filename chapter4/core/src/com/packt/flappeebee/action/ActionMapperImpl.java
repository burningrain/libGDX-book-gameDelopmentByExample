package com.packt.flappeebee.action;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.github.br.simple.input.ActionConst;
import com.github.br.simple.input.ActionMapper;
import com.github.br.simple.input.processor.InputData;

public class ActionMapperImpl implements ActionMapper {

    @Override
    public int getAction(float deltaTime, InputData inputData, Controller controller) {
        boolean isJump = controller.getButton(controller.getMapping().buttonA) || Gdx.input.isKeyPressed(Input.Keys.UP);
        if (isJump) {
            return HeroActions.JUMP;
        }

        // moving
        float axis = controller.getAxis(controller.getMapping().axisLeftX);
        if (axis < 0 || Gdx.input.isKeyPressed(Input.Keys.A)) {
            return HeroActions.MOVE_LEFT;
        } else if (axis > 0 || Gdx.input.isKeyPressed(Input.Keys.D)) {
            return HeroActions.MOVE_RIGHT;
        }

        // attack
        if (controller.getButton(controller.getMapping().buttonB) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            return HeroActions.ATTACK;
        }

        // blink
        if(controller.getButton(controller.getMapping().buttonY) || Gdx.input.isKeyPressed(Input.Keys.O)) {
            return HeroActions.BLINK_ON;
        }
        if(controller.getButton(controller.getMapping().buttonX) || Gdx.input.isKeyPressed(Input.Keys.P)) {
            return HeroActions.BLINK_OFF;
        }

        return ActionConst.EMPTY_ACTION;
    }

}
