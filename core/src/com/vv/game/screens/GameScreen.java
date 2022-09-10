package com.vv.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vv.game.VidarVoyager;
import com.vv.game.entities.Astronaut;
import com.vv.game.entities.Level;
import com.vv.game.utils.CollisionHandler;

/**
 * This is the Game Screen Class.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class GameScreen extends AbstractScreen {
    private final Stage stage;
    private final OrthographicCamera cam;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;
    private final Astronaut player;
    public boolean over = false;

    public GameScreen(Level level){
        super();

        cam = new OrthographicCamera(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT);
        stage = new Stage(new FitViewport(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT, this.cam));
        cam.setToOrtho(false);
        stage.getViewport().apply();
        cam.update();

        map = level.getMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        player = new Astronaut(stage, world);
        stage.addActor(player);

        world.setContactListener(new CollisionHandler());
    }

    @Override
    public void update(float deltaTime) {
        world.step(1f / VidarVoyager.APP_FPS, VidarVoyager.VELOCITY_ITERATIONS,
                VidarVoyager.POSITION_ITERATIONS);
        player.update(deltaTime);

        if(player.getCurrentState() != Astronaut.STATE.dead){
            cam.position.x = player.getBody().getPosition().x*VidarVoyager.PPM;
            cam.position.y = player.getBody().getPosition().y*VidarVoyager.PPM;
        }

        cam.update();
        mapRenderer.setView(cam);
        stage.act(deltaTime);
    }

    @Override
    public void render(float deltaTime){
        super.render(deltaTime);

        batch.begin();

        mapRenderer.render();
        stage.draw();

        batch.end();

        b2dr.render(world, cam.combined.cpy().scl(VidarVoyager.PPM));
    }

    @Override
    public void show() {
        batch.setProjectionMatrix(cam.combined);
    }

    @Override
    public void dispose(){
        super.dispose();
        world.dispose();
        b2dr.dispose();
        mapRenderer.dispose();
        map.dispose();
    }

    public Stage getStage(){ return this.stage; }

    public void setMap(TiledMap map){ this.map = map; }

    public boolean isGameOver(){ return over;}

    public void setGameOver(boolean over){ this.over = over; }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }
}

