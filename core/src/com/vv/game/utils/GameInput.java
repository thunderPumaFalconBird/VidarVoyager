package com.vv.game.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.Vector;

/**
 * This is the GameInput Class. It is only responsible for inputs that can effect the player in the game. All other
 * inputs will be handled by the UI input processor.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class GameInput implements InputProcessor {
    private Vector<Integer> keyInputs = new Vector<>();
    private static GameInput instance = new GameInput();

    private GameInput(){}

    public static GameInput getInstance(){ return instance;}
    public Vector<Integer> getKeyInputs() { return keyInputs; }

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
            keyInputs.remove((Integer) keycode);
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
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
