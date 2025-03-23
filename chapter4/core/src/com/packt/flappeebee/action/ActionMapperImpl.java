package com.packt.flappeebee.action;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.github.br.simple.input.ActionConst;
import com.github.br.simple.input.ActionMapper;
import com.github.br.simple.input.processor.InputData;

public class ActionMapperImpl implements ActionMapper {

    @Override
    public int getAction(float deltaTime, InputData inputData, Controller controller) {
        boolean isJump = controller.getButton(controller.getMapping().buttonA) || (Input.Keys.SPACE == inputData.inputKey.keyDown);
        if (isJump) {
            return HeroActions.JUMP;
        }

        // moving
        float axis = controller.getAxis(controller.getMapping().axisLeftX);
        if (axis < 0 || (Input.Keys.A == inputData.inputKey.keyDown)) {
            return HeroActions.MOVE_LEFT;
        } else if (axis > 0 || (Input.Keys.D == inputData.inputKey.keyDown)) {
            return HeroActions.MOVE_RIGHT;
        }

        // attack
        if (controller.getButton(controller.getMapping().buttonB) || (Input.Keys.ENTER == inputData.inputKey.keyDown)) {
            return HeroActions.ATTACK;
        }

        return ActionConst.EMPTY_ACTION;
    }

}
