package com.github.br.simple.input.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.*;

public class ControllerProxy implements Controller {

    public static final ControllerProxy INSTANCE = new ControllerProxy();

    private static class ControllerMappingImpl extends ControllerMapping {
        public ControllerMappingImpl(
                int axisLeftX,
                int axisLeftY,
                int axisRightX,
                int axisRightY,
                int buttonA,
                int buttonB,
                int buttonX,
                int buttonY,
                int buttonBack,
                int buttonStart,
                int buttonL1,
                int buttonL2,
                int buttonR1,
                int buttonR2,
                int buttonLeftStick,
                int buttonRightStick,
                int buttonDpadUp,
                int buttonDpadDown,
                int buttonDpadLeft,
                int buttonDpadRight
        ) {
            super(axisLeftX, axisLeftY, axisRightX, axisRightY, buttonA, buttonB, buttonX, buttonY, buttonBack,
                    buttonStart, buttonL1, buttonL2, buttonR1, buttonR2, buttonLeftStick, buttonRightStick,
                    buttonDpadUp, buttonDpadDown, buttonDpadLeft, buttonDpadRight);
        }
    }

    private static final ControllerMappingImpl EMPTY_MAPPING = new ControllerMappingImpl(
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
    );

    private final ControllerListener listener = new ControllerListener() {
        @Override
        public void connected(Controller controller) {
            bindController(controller);
        }

        @Override
        public void disconnected(Controller controller) {
            unbindController(controller);
        }

        @Override
        public boolean buttonDown(Controller controller, int buttonCode) {
            return false;
        }

        @Override
        public boolean buttonUp(Controller controller, int buttonCode) {
            return false;
        }

        @Override
        public boolean axisMoved(Controller controller, int axisCode, float value) {
            return false;
        }
    };

    private Controller target;

    public void dispose() {
        if(target == null) {
           return;
        }

        target.removeListener(listener);
    }

    public ControllerProxy() {
        this(Controllers.getCurrent());
    }

    public ControllerProxy(Controller target) {
        Controllers.addListener(new ControllerListener() {
            @Override
            public void connected(Controller controller) {
                bindController(controller);
            }

            @Override
            public void disconnected(Controller controller) {
                unbindController(controller);
            }

            @Override
            public boolean buttonDown(Controller controller, int buttonCode) {
                return false;
            }

            @Override
            public boolean buttonUp(Controller controller, int buttonCode) {
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisCode, float value) {
                return false;
            }
        });
    }

    private void bindController(Controller target) {
        this.target = target;
        target.addListener(listener);
        Gdx.app.log("ControllerProxy",
                "controller is connected. name=[" + target.getName() + "] id=[" + target.getUniqueId() + "]");
    }

    private void unbindController(final Controller controller) {
        controller.removeListener(listener);
        ControllerProxy.this.target = null;
        Gdx.app.log("ControllerProxy",
                "controller is disconnected. name=[" + controller.getName() + "] id=[" + controller.getUniqueId() + "]");
    }

    @Override
    public boolean getButton(int buttonCode) {
        if (target == null) {
            return false;
        }
        return target.getButton(buttonCode);
    }

    @Override
    public float getAxis(int axisCode) {
        if (target == null) {
            return 0;
        }
        return target.getAxis(axisCode);
    }

    @Override
    public String getName() {
        if (target == null) {
            return null;
        }
        return target.getName();
    }

    @Override
    public String getUniqueId() {
        if (target == null) {
            return null;
        }
        return target.getUniqueId();
    }

    @Override
    public int getMinButtonIndex() {
        if (target == null) {
            return 0;
        }
        return target.getMinButtonIndex();
    }

    @Override
    public int getMaxButtonIndex() {
        if (target == null) {
            return 0;
        }
        return target.getMaxButtonIndex();
    }

    @Override
    public int getAxisCount() {
        if (target == null) {
            return 0;
        }
        return target.getAxisCount();
    }

    @Override
    public boolean isConnected() {
        if (target == null) {
            return false;
        }
        return target.isConnected();
    }

    @Override
    public boolean canVibrate() {
        if (target == null) {
            return false;
        }
        return target.canVibrate();
    }

    @Override
    public boolean isVibrating() {
        if (target == null) {
            return false;
        }
        return target.isVibrating();
    }

    @Override
    public void startVibration(int duration, float strength) {
        if (target == null) {
            return;
        }
        target.startVibration(duration, strength);
    }

    @Override
    public void cancelVibration() {
        if (target == null) {
            return;
        }
        target.cancelVibration();
    }

    @Override
    public boolean supportsPlayerIndex() {
        if (target == null) {
            return false;
        }
        return target.supportsPlayerIndex();

    }

    @Override
    public int getPlayerIndex() {
        if (target == null) {
            return 0;
        }
        return target.getPlayerIndex();
    }

    @Override
    public void setPlayerIndex(int index) {
        if (target == null) {
            return;
        }
        target.setPlayerIndex(index);
    }

    @Override
    public ControllerMapping getMapping() {
        if (target == null) {
            return EMPTY_MAPPING;
        }
        return target.getMapping();
    }

    @Override
    public ControllerPowerLevel getPowerLevel() {
        if (target == null) {
            return null;
        }
        return target.getPowerLevel();
    }

    @Override
    public void addListener(ControllerListener listener) {
        if (target == null) {
            return;
        }
        target.addListener(listener);
    }

    @Override
    public void removeListener(ControllerListener listener) {
        if (target == null) {
            return;
        }
        target.removeListener(listener);
    }

}
