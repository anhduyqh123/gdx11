package Extend.Box2D;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.ArrayList;
import java.util.List;

public class IPlatform extends IBody{
    private final transient List<Fixture> fixtureList = new ArrayList<>();

    @Override
    public void OnBeginContact(IBody iBody, Fixture fixture, Contact contact) {
        Vector2[] points = contact.getWorldManifold().getPoints();
        int length = contact.getWorldManifold().getNumberOfContactPoints();
        if (CheckOneWay(iBody,points,length)){
            fixtureList.add(fixture);
            contact.setEnabled(false);
        }
        else super.OnBeginContact(iBody, fixture, contact);
    }

    @Override
    public void OnPostSolve(IBody iBody, Fixture fixture, Contact contact, ContactImpulse impulse) {
        if (fixtureList.contains(fixture)) contact.setEnabled(false);
        else super.OnPostSolve(iBody, fixture, contact, impulse);
    }

    @Override
    public void OnPreSolve(IBody iBody, Fixture fixture, Contact contact, Manifold oldManifold) {
        if (fixtureList.contains(fixture)) contact.setEnabled(false);
        else super.OnPreSolve(iBody, fixture, contact, oldManifold);
    }

    @Override
    public void OnEndContact(IBody iBody, Fixture fixture, Contact contact) {
        if (fixtureList.contains(fixture)){
            contact.setEnabled(true);
            fixtureList.remove(fixture);
        } else super.OnEndContact(iBody, fixture, contact);
    }

    private boolean CheckOneWay(IBody iBody,Vector2[] points,int length){
        for (int i = 0; i < length; i++) {
            Vector2 pointVelPlatform = body.getLinearVelocityFromWorldPoint(points[i]);
            Vector2 pointVelOther = iBody.body.getLinearVelocityFromWorldPoint(points[i]);
            Vector2 relativeVel = body.getLocalVector(new Vector2(pointVelOther).sub(pointVelPlatform));

            if ( relativeVel.y < -1 ) //if moving down faster than 1 m/s, handle as before
                return false;//point is moving into platform, leave contact solid and exit
            else if ( relativeVel.y < 1 ) { //if moving slower than 1 m/s
                //borderline case, moving only slightly out of platform
                Vector2 relativePoint = body.getLocalPoint(points[i]);
                float platformFaceY = 0.05f;//0.5f//front of platform, from fixture definition :(
                if ( relativePoint.y > platformFaceY - 0.05 )
                    return false;//contact point is less than 5cm inside front face of platfrom
            }
            else
                ;//moving up faster than 1 m/s
        }
        return true;
    }
}
