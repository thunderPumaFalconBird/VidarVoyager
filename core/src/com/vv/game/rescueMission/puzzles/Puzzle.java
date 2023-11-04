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
 * will handle its own input. The puzzle background will be the same for all puzzles.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public abstract class Puzzle extends Actor implements InputProcessor {
    private Texture background;
    protected boolean solved = false;

    public Puzzle(){
        background = new Texture("screens" + File.separator + "PuzzleBackground.png");
    }

    public abstract void update();

    public abstract void setStage(Stage stage);

    public abstract boolean isSolved();

    public abstract boolean isActive();

    public abstract void setActive(boolean active);

    /**
     * This method is called by the subclass to draw a simple background.
     * @param batch
     * @param parentAlpha The parent alpha, to be multiplied with this actor's alpha, allowing the parent's alpha to affect all
     *           children.
     */
    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.draw(background, 0,0);
    }
}
