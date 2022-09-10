package com.vv.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.vv.game.VidarVoyager;
import java.util.EnumMap;

public class Astronaut  extends Actor {
    public enum STATE {
        idleFront,
        idleBack,
        idleSide,
        walkingFront,
        walkingBack,
        walkingSideLeft,
        walkingSideRight,
        stalled
    }
    private EnumMap<STATE, Animation<TextureRegion>> animations;
    private float stateTime = 0f;
    private STATE currentState = STATE.idleFront;
    private STATE previousState = STATE.idleFront;
    private TextureRegion currentFrame;
    private final World world;
    private Body body;

    public Astronaut(Stage stage, World world){
        this.world = world;
        setStage(stage);
        initAnimations();
        setBounds(currentFrame.getRegionX(), currentFrame.getRegionY(),
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        setX(100);
        setY(100);
        defineAstronautBody();
    }

    public void defineAstronautBody() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() / VidarVoyager.PPM, getY() / VidarVoyager.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(currentFrame.getRegionWidth(), currentFrame.getRegionWidth());
        //TODO add maskBits for contactListener
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    public void destroyAstronautBody(){ world.destroyBody(body);}

    public void update(float deltaTime){
        if(currentState != STATE.stalled) {
            stateTime += deltaTime;
            currentFrame = animations.get(currentState).getKeyFrame(stateTime, true);
        }
        else if(previousState == STATE.stalled){
            stateTime = 0; // TODO this will need to be changed.
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
        TextureAtlas textureAtlas = new TextureAtlas("astronaut/Astronaut.txt");

        Array<TextureRegion> atlasTemp = new Array<TextureRegion>(textureAtlas.getRegions());
        Array<TextureRegion> animationTemp = new Array<>();

        //ADDING TEXTURE REGIONS TO ANIMATIONS ENUM MAP

        // adding Side facing idle
        animationTemp.addAll(atlasTemp, 0, 10);
        animations.put(STATE.idleSide, new Animation<>(.2f, animationTemp));
        animationTemp.clear();

        // adding Back facing idle
        animationTemp.addAll(atlasTemp, 10, 9);
        animations.put(STATE.idleBack, new Animation<>(.2f, animationTemp));
        animationTemp.clear();

        // adding Front facing idle
        animationTemp.addAll(atlasTemp, 19, 10);
        animations.put(STATE.idleFront, new Animation<>(.2f, animationTemp));
        animationTemp.clear();

        // adding Side facing Walking
        animationTemp.addAll(atlasTemp, 29, 12);
        animations.put(STATE.walkingSideLeft, new Animation<>(.2f, animationTemp));
        animationTemp.clear();

        // adding Back facing Walking
        animationTemp.addAll(atlasTemp, 41, 12);
        animations.put(STATE.walkingBack, new Animation<>(.2f, animationTemp));
        animationTemp.clear();

        // adding Front facing Walking
        animationTemp.addAll(atlasTemp, 53, 12);
        animations.put(STATE.walkingFront, new Animation<>(.2f, animationTemp));
        animationTemp.clear();

        //SET CURRENT FRAME TO CURRENT STATE
        currentFrame.setRegion(animations.get(currentState).getKeyFrame(stateTime, true));
    }
}
