package Tool.ObjectToolV2.Point;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IComponent.IShape.ICircle;
import GDX11.IObject.IComponent.IShape.IPoints;
import GDX11.IObject.IPos;
import GDX11.Reflect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

public class ICircleEdit extends IPointsEdit {
    private ICircle iCircle;
    public ICircleEdit(IActor iActor) {
        super(iActor);
    }

    @Override
    protected void AddKeyEvent(int keyCode) {

    }
    @Override
    protected void drawDebugChildren(ShapeRenderer shapes) {
        if (iCircle==null) return;
        Vector2 pos = iCircle.pos.GetPosition();
        shapes.set(ShapeRenderer.ShapeType.Line);
        shapes.circle(pos.x,pos.y,iCircle.radius);
    }
    public void SetData(ICircle iCircle) {
        this.iCircle = iCircle;
        IPos pos = iCircle.pos;
        IPos dot = Reflect.Clone(pos);
        dot.SetIActor(iCircle.GetIActor());

        dot.x = (pos.GetPosition().x+iCircle.radius)+"";
        Vector2 dir = dot.GetPosition().sub(pos.GetPosition());

        Reflect.AddEvent(dot,"dot",vl->{
            iCircle.radius = dot.GetPosition().dst(pos.GetPosition());
            dir.set(dot.GetPosition().sub(pos.GetPosition()));
        });
        Reflect.AddEvent(pos,"pos",vl->{
            map.get(dot).SetPosition(pos.GetPosition().add(dir));
            Reflect.OnChange(map.get(dot));
        });
        IPoints iPoints = new IPoints();
        iPoints.list = Arrays.asList(pos,dot);
        SetData(iPoints);
    }
}
