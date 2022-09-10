package com.vv.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class LevelController {
    private Array<Level> levels;
    private int currentLevel;

    public LevelController(){
        levels = new Array<>();
        levels.add(new Level(1));
        currentLevel = 1;
    }

    public Level getLevel(){ return levels.get(currentLevel - 1); }

}
