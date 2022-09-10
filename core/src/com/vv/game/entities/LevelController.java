package com.vv.game.entities;

import com.badlogic.gdx.utils.Array;

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
