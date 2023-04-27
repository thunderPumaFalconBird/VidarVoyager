package com.vv.game.rescueMission.puzzles.minesweeper;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.vv.game.rescueMission.puzzles.Puzzle;

import java.util.HashMap;

public class MineSweeper extends Puzzle {
    private Stage stage;
    private HashMap<Vector2, Square> squares;
    private boolean active = false;

    public MineSweeper(){
        squares = new HashMap<>();
    }


    public void setStage(Stage stage){
        //TODO add textures
        this.stage = stage;
        stage.addActor(this);
    }

    @Override
    public void update(){
        //check input new vec2 - x%100 - y%100 to get vec2 of square.
        //set Texture based on type of square.
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch, parentAlpha);
        //batch.draw(texture, 0,0);
    }

    @Override
    public boolean isSolved(){
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //TODO check x and y is within the mineboard
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //TODO check x and y is within the mineboard
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