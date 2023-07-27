package Extend.Box2D;

import GDX11.IObject.IComponent.IComponent;
import GDX11.IObject.IComponent.IShape.ICircle;
import GDX11.IObject.IComponent.IShape.IPoints;
import GDX11.IObject.IComponent.IShape.IShape;
import GDX11.IObject.IPos;
import GDX11.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

public class IFixture extends IComponent {
    public String shape = "shape";
    public boolean isSensor;
    public float density = 1;
    public float friction = 0.2f;
    public float restitution = 0.2f;
    public int category=1,mark=-1;//mark=-1->collect with all category

    private transient Body body;
    private void CreateFixture(Shape shape)
    {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = (short)category;
        fixtureDef.filter.maskBits = (short) mark;
        fixtureDef.isSensor = isSensor;
        body.createFixture(fixtureDef);
    }
    public void Create(Body body){
        this.body = body;
        CreateShape(GetIComponent(shape));
    }

    private void CreateShape(IShape iShape){
        if (iShape instanceof ICircle) CreateCircleShape((ICircle) iShape);
        else CreatePolygonShape((IPoints) iShape);
    }
    private void CreateCircleShape(ICircle iCircle){
        CircleShape shape = new CircleShape();
        shape.setPosition(GetPhysicPoint(iCircle.pos));
        shape.setRadius(GBox2D.GameToPhysics(iCircle.radius));
        CreateFixture(shape);
    }
    private void CreatePolygonShape(IPoints iPoints){
        PolygonShape shape = new PolygonShape();
        List<Vector2> points = new ArrayList<>();
        Util.For(iPoints.list,iPos-> points.add(GetPhysicPoint(iPos)));

        if (IsConvexPolygon(points)){
            shape.set(points.toArray(new Vector2[0]));
            CreateFixture(shape);
        }
        else {
            Util.ForTriangles(points,arr->{
                shape.set(arr);
                CreateFixture(shape);
            });
        }
    }
    private Vector2 GetPhysicPoint(IPos iPos){
        Vector2 origin = GetIActor().GetOrigin();
        Vector2 pos = iPos.GetPosition().sub(origin).scl(GetActor().getScaleX(),GetActor().getScaleY());
        return GBox2D.GameToPhysics(pos);
    }
    private boolean IsConvexPolygon(List<Vector2> points) {
        if (points.size()<3||points.size()>8) return false;
        for(int i=0;i< points.size();i++)
            if (GetAngle(points,i)>=180) return false;
        return true;
    }
    private float GetAngle(List<Vector2> points, int index) {
        Vector2 p1 = GetValue(points, index-1);
        Vector2 p2 = GetValue(points, index);
        Vector2 p3 = GetValue(points, index+1);
        return Util.GetAngle(p1,p2,p3);
    }
    private Vector2 GetValue(List<Vector2> points,int index) {
        if (index<0) index = points.size()-1;
        if (index>= points.size()) index = 0;
        return points.get(index);
    }
}
