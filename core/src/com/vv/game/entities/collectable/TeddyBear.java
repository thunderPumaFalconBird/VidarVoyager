package com.vv.game.entities.collectable;

import com.badlogic.gdx.physics.box2d.World;

public class TeddyBear extends Collectable{
    public enum COLOR {
        BLUE,
        GREEN,
        ORANGE,
        PINK,
        RED
    }

    public TeddyBear(World world) {
        super(world);
    }


}
