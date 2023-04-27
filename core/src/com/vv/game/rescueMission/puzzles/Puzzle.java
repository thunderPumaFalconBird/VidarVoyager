package com.vv.game.rescueMission.puzzles;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.io.File;

/**
 * This is the abstract puzzle class. It extends the actor class so that a puzzle can be added to a stage and the
 * overriden draw method will be called during rendering of the stage. It implements inputProcessor and each puzzle
 * will handle it's own input. The puzzle background will be the same for all puzzles.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public abstract class Puzzle extends Actor implements InputProcessor {
    private Texture background = new Texture("screens" + File.separator + "PuzzleBackgroundStars.png");
    protected boolean solved = false;

    public Puzzle(){

    }

    public abstract void update();

    public abstract void setStage(Stage stage);

    public abstract boolean isSolved();

    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.draw(background, 0,0);
    }

}
