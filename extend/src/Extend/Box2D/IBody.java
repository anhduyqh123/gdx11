package Extend.Box2D;

import GDX11.IObject.IComponent.IComponent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class IBody extends IComponent {
    public BodyDef.BodyType type = BodyDef.BodyType.StaticBody;
    public String category = "object";
    public String fixture = "fixture";//fixture1,fixture2...
    public float linearDamping,angularDamping,gravityScale=1;
    public boolean fixedRotation,bullet,allowSleep=true,active=true;
    public transient Body body;

    @Override
    public void Update(float delta) {
        if (IsPhysicUpdate()) UpdatePhysic();
        else UpdateGame();
    }

    @Override
    public void Refresh() {
        if (body!=null) GetGBox2D().Destroy(body);
        body = NewBody();
        UpdateGame();
    }

    @Override
    public void Remove() {
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
        return GetIActor().GetIRoot().IRootFind("box2d").GetActor();
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
        NewFixture(fixture,body);
        return body;
    }
    private void NewFixture(String name,Body body){
        if (name.contains(",")){
            for (String n : name.split(","))
                NewFixture(n,body);
            return;
        }
        IFixture iFixture = GetIComponent(name);
        iFixture.Create(body);
    }
}
