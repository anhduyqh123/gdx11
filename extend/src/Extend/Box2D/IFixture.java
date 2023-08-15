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
    public enum ShapeType{
        None,
        Chain
    }

    public String shape = "shape";//shape(0-9)->(0->9)
    public ShapeType shapeType = ShapeType.None;
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
        Util.CreateValue(shape,vl->CreateShape(GetIComponent(vl)));
    }

    private void CreateShape(IShape iShape){
        if (iShape instanceof ICircle) CreateCircleShape((ICircle) iShape);
        else{
            if (shapeType==ShapeType.Chain) CreateChainShape((IPoints) iShape);
            else CreatePolygonShape((IPoints) iShape);
        }
    }
    private void CreateCircleShape(ICircle iCircle){
        CircleShape shape = new CircleShape();
        shape.setPosition(GetPhysicPoint(iCircle.pos));
        shape.setRadius(GBox2D.GameToPhysics(iCircle.radius));
        CreateFixture(shape);
    }
    private void CreateChainShape(IPoints iPoints){
        ChainShape shape = new ChainShape();
        Vector2[] arr = GetPhysicPoints(iPoints).toArray(new Vector2[0]);
        if (iPoints.close) shape.createLoop(arr);
        else shape.createChain(arr);
        CreateFixture(shape);
    }
    private void CreatePolygonShape(IPoints iPoints){
        PolygonShape shape = new PolygonShape();
        List<Vector2> points = GetPhysicPoints(iPoints);

        if (isConvex(points)){
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
    private List<Vector2> GetPhysicPoints(IPoints iPoints){
        List<Vector2> points = new ArrayList<>();
        Util.For(iPoints.list,iPos-> points.add(GetPhysicPoint(iPos)));
        return points;
    }
    private Vector2 GetPhysicPoint(IPos iPos){
        Vector2 origin = GetIActor().GetOrigin();
        Vector2 pos = iPos.GetPosition().sub(origin).scl(GetActor().getScaleX(),GetActor().getScaleY());
        return GBox2D.GameToPhysics(pos);
    }
    private boolean isConvex(List<Vector2> vertices) {
        int n = vertices.size();
        if (n > 8) return false;
        if (n < 4) return true;
        boolean sign = false;
        for(int i = 0; i < n; i++) {
            Vector2 d1 = new Vector2(vertices.get((i + 2) % n)).sub(vertices.get((i + 1) % n));
            Vector2 d2 = new Vector2(vertices.get(i)).sub(vertices.get((i + 1) % n));
            float zcrossproduct = d1.x * d2.y - d1.y * d2.x;
            if (i == 0) sign = zcrossproduct > 0;
            else if (sign != (zcrossproduct > 0)) return false;
        }
        return true;
    }
}
