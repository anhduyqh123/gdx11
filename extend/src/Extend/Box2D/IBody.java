package Extend.Box2D;

import GDX11.GDX;
import GDX11.IObject.IComponent.IComponent;
import GDX11.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

public class IBody extends IComponent {
    public BodyDef.BodyType type = BodyDef.BodyType.StaticBody;
    public String category = "object";
    public String fixture = "fixture";//fixture(0-9)
    public float linearDamping,angularDamping,gravityScale=1;
    public boolean fixedRotation,bullet,allowSleep=true,active=true;
    public transient Body body;
    private final transient List<IBodyListener> listeners = new ArrayList<>();

    @Override
    public void Update(float delta) {
        if (body==null) return;
        if (IsPhysicUpdate()) UpdatePhysic();
        else UpdateGame();
        for (IBodyListener e : listeners)
            e.OnUpdate(delta);
    }

    @Override
    public void Refresh() {
        listeners.clear();
        if (body!=null) GetGBox2D().Destroy(body);
        body = NewBody();
        UpdateGame();
    }

    @Override
    public void Remove() {
        GetGBox2D().Destroy(body);
        body=null;
    }
    //Update
    private void UpdateGame(){
        Vector2 pos = GetActor().localToStageCoordinates(GetIActor().GetOrigin());
        float rotate = GetIActor().GetStageRotation();
        body.setTransform(GBox2D.GameToPhysics(pos),(float) Math.toRadians(rotate));
    }
    private void UpdatePhysic(){
        Vector2 pos = GBox2D.PhysicsToGame(body.getPosition());//stage origin position
        Vector2 pos0 = GetActor().localToStageCoordinates(GetIActor().GetOrigin());
        Vector2 dir = pos.sub(pos0);Vector2 nPos = GetIActor().GetPosition().add(dir);
        GetIActor().SetPosition(nPos);
        GetIActor().SetStageRotation((float) Math.toDegrees(body.getAngle()));
    }
    private boolean IsPhysicUpdate(){
        return GetGBox2D().isVisible()
                && body.isActive() && body.getType()!=BodyDef.BodyType.StaticBody;
    }
    private GBox2D GetGBox2D(){
        return GDX.Try(()->GetIActor().GetIRoot().IRootFind("box2d").GetActor(), GBox2D::new);
    }

    private BodyDef GetBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.linearDamping = linearDamping;
        bodyDef.angularDamping = angularDamping;
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.bullet = bullet;
        bodyDef.allowSleep = allowSleep;
        bodyDef.gravityScale = gravityScale;
        bodyDef.active = active;
        return bodyDef;
    }
    private Body NewBody(){
        BodyDef bodyDef = GetBodyDef();
        Body body = GetGBox2D().NewBody(bodyDef);
        body.setUserData(this);
        Util.CreateValue(fixture,vl->GetIComponent(vl, IFixture.class).Create(body));
        return body;
    }

    //EventContact
    public void AddEvent(IBodyListener event){
        listeners.add(event);
    }
    public void OnBeginContact(IBody iBody, Fixture fixture, Contact contact){
        for (IBodyListener e : listeners)
            e.OnBeginContact(iBody,fixture,contact);
    }
    public void OnEndContact(IBody iBody, Fixture fixture, Contact contact){
        for (IBodyListener e : listeners)
            e.OnEndContact(iBody,fixture,contact);
    }
    public void OnPreSolve(IBody iBody, Fixture fixture, Contact contact, Manifold oldManifold){
        for (IBodyListener e : listeners)
            e.OnPreSolve(iBody,fixture,contact,oldManifold);
    }
    public void OnPostSolve(IBody iBody, Fixture fixture,Contact contact,ContactImpulse impulse){
        for (IBodyListener e : listeners)
            e.OnPostSolve(iBody,fixture,contact,impulse);
    }
    public void OnRayCast(String name){
        for (IBodyListener e : listeners)
            e.OnRayCast(name);
    }
}
