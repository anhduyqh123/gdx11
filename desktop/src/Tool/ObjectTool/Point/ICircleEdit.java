package Tool.ObjectTool.Point;

import Extend.IShape.ICircle;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IPos;
import GDX11.Reflect;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ICircleEdit extends IPointsEdit{
    public ICircleEdit(IActor iActor) {
        super(iActor);
    }

    @Override
    protected void AddKeyEvent(int keyCode) {

    }
    public void SetData(ICircle iCircle) {
        IPos pos = Reflect.Clone(iCircle.pos);
        IPos dot = Reflect.Clone(pos);
        pos.SetIActor(iCircle.GetIActor());
        dot.SetIActor(iCircle.GetIActor());

        dot.x = (pos.GetPosition().x+iCircle.radius)+"";
        Vector2 dir = dot.GetPosition().sub(pos.GetPosition());

        Reflect.AddEvent(dot,"dot",vl->{
            iCircle.radius = dot.GetPosition().dst(pos.GetPosition());
            dir.set(dot.GetPosition().sub(pos.GetPosition()));
        });
        Reflect.AddEvent(pos,"pos",vl->{
            iCircle.pos.SetPosition(pos.GetPosition());
            dot.SetPosition(pos.GetPosition().add(dir));
            dot.Refresh();
        });
        SetData(Arrays.asList(pos,dot));
    }
}
