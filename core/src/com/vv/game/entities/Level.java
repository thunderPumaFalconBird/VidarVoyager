package com.vv.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Level {
    private int levelNumber;
    private TmxMapLoader mapLoader;
    private TiledMap map;

    public Level(int levelNumber){
        this.levelNumber = levelNumber;
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(("maps/Level" + levelNumber + ".tmx"));

        for (int i = 0; i < map.getLayers().size(); i++) {
            String layername = map.getLayers().get(i).getName();
            //TODO add objects
        }

    }

    public TiledMap getMap() { return map; }
}
