package com.vv.game.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Vector;

/**
 * This is the GameInput Class. It is only responsible for inputs that can effect the player in the game. All other
 * inputs will be handled by the UI input processor.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class GameInput implements InputProcessor {
    private Array<Integer> keyInputs = new Array<>();
    private Vector3 mouseInputs = new Vector3();
    private static final GameInput instance = new GameInput();

    private GameInput(){}

    public static GameInput getInstance(){ return instance;}
    public Array<Integer> getKeyInputs() { return keyInputs; }
    public Vector3 getMouseInputs() { return mouseInputs; }

    @Override
    public boolean keyDown(int keycode) {
        boolean returnValue = false;
        if(keycode == Input.Keys.UP || keycode == Input.Keys.DOWN
                || keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT){
            keyInputs.add(keycode);
            returnValue = true;
        }
        return returnValue;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean returnValue = false;
        if(keycode == Input.Keys.UP || keycode == Input.Keys.DOWN
                || keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT){
            keyInputs.removeValue(keycode, true);
            returnValue = true;
        }
        return returnValue;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mouseInputs.set(screenX,screenY,button);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mouseInputs.setZero();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
