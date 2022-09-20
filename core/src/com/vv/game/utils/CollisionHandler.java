package com.vv.game.utils;

import com.badlogic.gdx.physics.box2d.*;

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

    }

    private void borderContact(Object border, Object obj) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
