package com.vv.game.rescueMission.puzzles.minesweeper;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.File;

/**
 * This is the Square class. It is used in the MineSweeper class. The Squares make up the minesweeper board. The Type
 * indicates how many mines are nearby.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class Square {
    public enum TYPE {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, MINE, EXPLODED,
    }
    private final TextureAtlas textureAtlas;
    private TextureRegion texture;
    private TYPE type = TYPE.ZERO;
    private int x;
    private int y;
    private boolean revealed = false;
    private boolean flagged = false;

    public Square(int x, int y){
        this.x = x;
        this.y = y;
        textureAtlas = new TextureAtlas("minesweeper" + File.separator + "mineSweeper.txt");
        texture = textureAtlas.findRegion("unmarked");
    }

    public void draw(Batch batch){
        batch.draw(texture, x, y);
    }

    public boolean isMine(){ return type == TYPE.MINE; }

    public boolean isZero() { return type == TYPE.ZERO; }

    public void setType(TYPE type){ this.type = type; }

    public boolean isFlagged() { return flagged; }

    public void setFlagged() {
        flagged = !flagged;
        if(flagged) {
            texture = textureAtlas.findRegion("FlaggedBomb");
        }
        else{
            texture = textureAtlas.findRegion("unmarked");
        }
    }

    public boolean isRevealed(){return revealed; }

    public void explodeMine(){
        texture = textureAtlas.findRegion("explosion");
        type = TYPE.EXPLODED;
    }

    public void setTexture(){
        revealed = true;
        switch(type){
            case ZERO :
                texture = textureAtlas.findRegion("zero");
                break;
            case ONE :
                texture = textureAtlas.findRegion("1");
                break;
            case TWO :
                texture = textureAtlas.findRegion("2");
                break;
            case THREE :
                texture = textureAtlas.findRegion("3");
                break;
            case FOUR :
                texture = textureAtlas.findRegion("4");
                break;
            case FIVE:
                texture = textureAtlas.findRegion("5");
                break;
            case SIX:
                texture = textureAtlas.findRegion("6");
                break;
            case SEVEN:
                texture = textureAtlas.findRegion("7");
                break;
            case EIGHT:
                texture = textureAtlas.findRegion("8");
                break;
            case MINE:
                texture = textureAtlas.findRegion("mine");
                break;
        }
    }

    public void addMineNearBy(){
        switch(type){
            case ZERO :
                type = TYPE.ONE;
                break;
            case ONE :
                type = TYPE.TWO;
                break;
            case TWO :
                type = TYPE.THREE;
                break;
            case THREE :
                type = TYPE.FOUR;
                break;
            case FOUR :
                type = TYPE.FIVE;
                break;
            case FIVE:
                type = TYPE.SIX;
                break;
            case SIX:
                type = TYPE.SEVEN;
                break;
            case SEVEN:
                type = TYPE.EIGHT;
                break;
        }
    }
}
