package com.vv.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.EnumMap;
import java.util.Vector;

public class Astronaut  extends Actor {
    public enum STATE {
        idleFront,
        idleBack,
        idleSide,
        walkingFront,
        walkingBack,
        walkingSide,
        runningFront,
        runningBack,
        runningSide,
        stalled
    }
    private EnumMap<STATE, Vector<TextureRegion>> animations;
    private EnumMap<STATE, Integer> animationsIndexes;
    private STATE currentState = STATE.idleFront;
    private STATE previousState = STATE.idleFront;
    //TODO add body and body methods. Add method for handling input.

    public Astronaut(Stage stage){
        setStage(stage);
        initAnimations();
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if(currentState == STATE.stalled){
            batch.draw(animations.get(previousState).get(animationsIndexes.get(previousState)), getX(), getY());
        }
        else {
            Integer temp = animationsIndexes.get(currentState);
            batch.draw(animations.get(currentState).get(temp++), getX(), getY());
            animationsIndexes.put(currentState, temp);
        }
    }

    public STATE getCurrentState() {
        return currentState;
    }

    public void setCurrentState(STATE currentState){
        if(currentState == STATE.stalled) {
            previousState = this.currentState;
            this.currentState = currentState;
        }
        else{
            previousState = this.currentState;
            this.currentState = currentState;
            animationsIndexes.put(currentState, 0);
        }
    }

    private void initAnimations(){
        animations = new EnumMap<STATE, Vector<TextureRegion>>(STATE.class);
        animationsIndexes = new EnumMap<STATE, Integer>(STATE.class);

        Vector <TextureRegion> temp = new Vector<>();
        //TODO create and add textureRegions
        animations.put(STATE.idleFront, temp);
    }
}
