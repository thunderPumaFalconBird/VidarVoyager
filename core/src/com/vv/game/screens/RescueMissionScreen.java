package com.vv.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vv.game.VidarVoyager;
import com.vv.game.rescueMission.entities.collectable.Collectable;
import com.vv.game.rescueMission.entities.movable.Astronaut;

import java.io.File;

/**
 * This is the Game Screen Class. It handles rendering the map and stage. It also renders box2d shapes when debugging.
 * It also updates the camera position based on the player's location.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class RescueMissionScreen extends AbstractScreen {
    private final float inventoryOffsetX = 96;
    private final float inventoryOffsetY = 60;
    private final float oxygenOffsetX = 796;
    private final float oxygenOffsetY = 85;
    private final float oxygenBarWidth = 15;
    private final Stage stage;
    private final OrthographicCamera cam;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Texture inventoryOxygenBar;
    private final Texture oxygenBarTexture;
    private final Texture highlight;
    private Array<TextureRegion> inventoryTextures;
    private float inventoryX, inventoryY, oxygenX, oxygenY;
    private int oxygenBarsNumber = 10;
    private int inventoryHighlightX = 0;

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
        oxygenBarTexture = new Texture("screens" + File.separator + "OxygenLevelBar.png");
        highlight = new Texture("screens" + File.separator + "Highlight2.png");
        inventoryTextures = new Array<>();
    }

    public Stage getStage(){ return this.stage; }

    public void updateCam(float x, float y){
        cam.position.set(x * VidarVoyager.PPM, y * VidarVoyager.PPM, 0);
        cam.update();
    }

    @Override
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    @Override
    public void setGameWon(boolean gameWon) { this.gameWon = gameWon; }

    @Override
    public void update(float deltaTime) {
        cam.update();
        mapRenderer.setView(cam);
        stage.act(deltaTime);

        Array<Actor> temp = stage.getActors();
        inventoryTextures.clear();
        for(int i = 0; i < temp.size; i++){
            if(temp.get(i) instanceof Astronaut){
                Astronaut player = (Astronaut) ((Astronaut) temp.get(i)).getBody().getUserData();

                Array<Collectable> inventory = player.getInventory();
                for (int j = 0; j < inventory.size; j++){
                    inventoryTextures.add(inventory.get(j).getTextureRegion());
                }

                float oxygenLevel = player.getOxygenLevel();
                oxygenBarsNumber = ((int) oxygenLevel / 10) + 1;

                inventoryHighlightX = player.getCurrentItem();
            }
        }

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
        for(int i = 0; i < inventoryTextures.size; i++){
            batch.draw(inventoryTextures.get(i),
                    inventoryX + i*inventoryOffsetX + inventoryOffsetY,
                    inventoryY + inventoryOffsetY
                    );
        }
        for(int i = 0; i < oxygenBarsNumber; i++){
            batch.draw(oxygenBarTexture,
                    inventoryX + i*oxygenBarWidth + oxygenOffsetX,
                    inventoryY + oxygenOffsetY);
        }

        batch.draw(highlight,
                inventoryX + inventoryHighlightX*100,
                inventoryY);

        batch.end();
    }

    @Override
    public void show() { batch.setProjectionMatrix(cam.combined); }

    @Override
    public void dispose(){
        super.dispose();
        inventoryOxygenBar.dispose();
        mapRenderer.dispose();
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }
}

