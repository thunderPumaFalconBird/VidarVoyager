package com.vv.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vv.game.VidarVoyager;

import java.io.File;

/**
 * This is the Game Screen Class. It handles rendering the map and stage. It also renders box2d shapes when debugging.
 * It also updates the camera position based on the player's location.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class RescueMissionScreen extends AbstractScreen {
    private final Stage stage;
    private final OrthographicCamera cam;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Texture inventoryOxygenBar;
    private float inventoryX, inventoryY;

    public RescueMissionScreen(TiledMap map, World world){
        super();

        this.world = world;
        cam = new OrthographicCamera(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT);
        stage = new Stage(new FitViewport((float) VidarVoyager.APP_WIDTH,
                (float)VidarVoyager.APP_HEIGHT,
                this.cam));
        cam.setToOrtho(false);
        stage.getViewport().apply();
        cam.update();

        mapRenderer = new OrthogonalTiledMapRenderer(map);
        inventoryOxygenBar = new Texture("screens" + File.separator + "InventoryOxygenBar.png");
    }

    public Stage getStage(){ return this.stage; }

    public void updateCam(float x, float y){
        cam.position.set(x * VidarVoyager.PPM, y * VidarVoyager.PPM, 0);
        cam.update();
    }

    @Override
    public void update(float deltaTime) {
        cam.update();
        mapRenderer.setView(cam);
        stage.act(deltaTime);

        inventoryX = cam.position.x - ((float)VidarVoyager.APP_HEIGHT/2);
        inventoryY = cam.position.y - ((float)VidarVoyager.APP_HEIGHT/2);
    }

    @Override
    public void render(float deltaTime){
        super.render(deltaTime);

        batch.begin();

        mapRenderer.render();

        batch.draw(inventoryOxygenBar, inventoryX, inventoryY);

        stage.draw();
        if(VidarVoyager.debugging){
            b2dr.render(world, cam.combined.cpy().scl(VidarVoyager.PPM));
        }

        batch.end();
    }

    @Override
    public void show() {
        batch.setProjectionMatrix(cam.combined);
    }

    @Override
    public void dispose(){
        super.dispose();
        mapRenderer.dispose();
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }
}

