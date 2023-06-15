package com.vv.game.rescueMission;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.io.File;

public class OxygenBar {
    private final float OXYGEN_OFFSET_X = 796;
    private final float OXYGEN_OFFSET_Y = 86;
    private final float OXYGEN_BAR_WIDTH = 15;
    private final Texture oxygenBarTexture;
    private int oxygenBarsNumber = 10;

    public OxygenBar(){
        oxygenBarTexture = new Texture("screens" + File.separator + "OxygenLevelBar.png");
    }

    public void draw(Batch batch, float x, float y){
        for(int i = 0; i < oxygenBarsNumber; i++){
            batch.draw(oxygenBarTexture,
                    x + i* OXYGEN_BAR_WIDTH + OXYGEN_OFFSET_X,
                    y + OXYGEN_OFFSET_Y);
        }
    }

    public void updateOxygenLevel(float oxygenLevel){
        oxygenBarsNumber = ((int) oxygenLevel / 10) + 1;
    }
}
