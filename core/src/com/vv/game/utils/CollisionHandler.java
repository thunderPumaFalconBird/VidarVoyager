package com.vv.game.utils;

import com.badlogic.gdx.physics.box2d.*;
import com.vv.game.entities.movable.Astronaut;
import com.vv.game.entities.immovable.Cannon;
import com.vv.game.entities.immovable.Door;

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
        if(fixA.getBody().getUserData() instanceof Astronaut && fixB.getBody().getUserData() instanceof Cannon){
            Cannon cannon = (Cannon) fixB.getBody().getUserData();
            cannon.fireCannon();
        }
        if(fixA.getBody().getUserData() instanceof Cannon && fixB.getBody().getUserData() instanceof Astronaut ){
            Cannon cannon = (Cannon) fixA.getBody().getUserData();
            cannon.fireCannon();
        }
        if(fixA.getBody().getUserData() instanceof Door && fixB.getBody().getUserData() instanceof Astronaut ) {
            Door door = (Door) fixA.getBody().getUserData();
            door.setActive(true);
        }
        if(fixA.getBody().getUserData() instanceof Astronaut && fixB.getBody().getUserData() instanceof Door){
            Door door = (Door) fixA.getBody().getUserData();
            door.setActive(true);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
