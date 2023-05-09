package com.vv.game.rescueMission.puzzles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static com.vv.game.VidarVoyager.debugging;

/**
 * The is a test puzzle class. It is only used for test purposes to ensure the puzzle logic is implemented properly.
 *
 */
public class TestPuzzle extends Puzzle {
    private Stage stage;
    private boolean active = false;


    @Override
    public void update() {

    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch, parentAlpha);
        //batch.draw(texture, 0,0);
    }

    @Override
    public boolean isSolved(){ return solved; }

    @Override
    public boolean isActive() { return active; }

    @Override
    public void setActive(boolean active) { this.active = active; }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.addActor(this);
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
        //solved = true;
        if(debugging) {
            System.out.println("solved");
        }
        System.out.println("x: " + screenX + "  y:" + screenY);
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
