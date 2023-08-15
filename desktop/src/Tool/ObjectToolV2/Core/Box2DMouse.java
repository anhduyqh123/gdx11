package Tool.ObjectToolV2.Core;

import Extend.Box2D.GBox2D;
import Extend.Box2D.IBody;
import GDX11.IObject.IActor.IActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Box2DMouse extends Event.DragActorListener {
    private Actor actor;
    private Body body;
    private GBox2D box2D;
    private MouseJoint mouseJoint;
    public Box2DMouse(IActor iActor)
    {
        super(iActor);
        IBody iBody = iActor.iComponents.GetIComponent("body");
        if (iBody==null || iBody.body==null || !iBody.IsPhysicUpdate()) return;
        actor = iActor.GetActor();
        body = iBody.body;
        box2D = iBody.GetGBox2D();
    }

    private Body NewEdge()
    {
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-500,-200,500,-200);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = box2D.NewBody(bodyDef);
        body.createFixture(edgeShape,0);
        edgeShape.dispose();
        return body;
    }
    private MouseJointDef MouseJointDef()
    {
        MouseJointDef jointDef = new MouseJointDef();
        jointDef.bodyA = NewEdge();
        jointDef.bodyB = body;
        jointDef.frequencyHz = 30;
        jointDef.dampingRatio = 0.8f;
        jointDef.maxForce = 140;
        jointDef.collideConnected = true;
        jointDef.target.set(GetMousePos());
        return jointDef;
    }
    private Vector2 GetMousePos(){
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        actor.getStage().screenToStageCoordinates(mousePos);
        return GBox2D.GameToPhysics(mousePos);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (body==null) return false;
        mouseJoint = (MouseJoint) box2D.CreateJoint(MouseJointDef());
        return true;
    }
    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if (mouseJoint==null) return;
        mouseJoint.setTarget(GetMousePos());
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (mouseJoint==null) return;
        box2D.Destroy(mouseJoint);
    }
}
