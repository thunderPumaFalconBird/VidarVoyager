package com.vv.game.utils;

import com.badlogic.gdx.physics.box2d.*;
import com.vv.game.entities.Astronaut;
import com.vv.game.level.Cannon;

/**
 * This is the collision handler class.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class CollisionHandler implements ContactListener {

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if(fixA.getBody().getUserData() instanceof Astronaut && fixB.getBody().getUserData() == Cannon.Type.RANGE){
            Cannon cannon = (Cannon) fixB.getUserData();
            cannon.fireCannon();
        }
        if(fixA.getBody().getUserData() == Cannon.Type.RANGE && fixB.getBody().getUserData() instanceof Astronaut ){
            Cannon cannon = (Cannon) fixA.getUserData();
            cannon.fireCannon();
        }
    }

    private void borderContact(Object border, Object obj) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
