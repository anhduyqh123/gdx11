package Extend.Box2D;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public abstract class IBodyListener {

    public void OnBeginContact(IBody iBodyB, Fixture fixtureB, Contact contact) {}
    public void OnEndContact(IBody iBodyB, Fixture fixtureB, Contact contact) {}
    public void OnPreSolve(IBody iBodyB, Fixture fixtureB, Contact contact, Manifold oldManifold){}
    public void OnPostSolve(IBody iBodyB, Fixture fixtureB, Contact contact,ContactImpulse impulse){}
    public void OnRayCast(String name){}
    public void OnUpdate(float delta){}
}
