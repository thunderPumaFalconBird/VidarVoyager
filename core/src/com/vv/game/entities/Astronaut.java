package com.vv.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;
import java.util.Vector;

public class Astronaut  extends Actor {
    public enum STATE {
        idleFront,
        idleBack,
        idleSide,
        walkingFront,
        walkingBack,
        walkingSideLeft,
        walkingSideRight,
        runningFront,
        runningBack,
        runningSideLeft,
        RunningSideRight,
        stalled
    }
    private EnumMap<STATE, Animation<TextureRegion>> animations;
    private float stateTime = 0f;
    private STATE currentState = STATE.idleFront;
    private STATE previousState = STATE.idleFront;
    private TextureRegion currentFrame;
    //TODO add body and body methods. Add method for handling input.

    public Astronaut(Stage stage){
        setStage(stage);
        initAnimations();
    }

    public void update(float deltaTime){
        if(currentState != STATE.stalled) {
            stateTime += deltaTime;
            currentFrame = animations.get(currentState).getKeyFrame(stateTime, true);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
            batch.draw(currentFrame, getX(), getY());
    }

    public STATE getCurrentState() {
        return currentState;
    }

    public void setCurrentState(STATE currentState){
        previousState = this.currentState;
        this.currentState = currentState;
    }

    private void initAnimations(){
        animations = new EnumMap<>(STATE.class);

        Array<TextureRegion> temp = new Array<>();

        //TODO create and add textureRegions

    }
}
