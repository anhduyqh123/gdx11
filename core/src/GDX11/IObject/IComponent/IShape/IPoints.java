package GDX11.IObject.IComponent.IShape;

import GDX11.IObject.IPos;
import GDX11.Util;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class IPoints extends IShape {
    public List<IPos> list = new ArrayList<>();
    public boolean close;
    public void Init()
    {
        list.add(new IPos());
        list.add(new IPos(0, GetActor().getHeight()));
        list.add(new IPos(GetActor().getWidth(), GetActor().getHeight()));
        list.add(new IPos(GetActor().getWidth(), 0));
    }
    protected List<Vector2> GetStagePoints()
    {
        List<Vector2> points = new ArrayList<>();
        Util.For(list,ip-> points.add(GetStagePos(ip)));
        return points;
    }

    @Override
    public void DrawShape(ShapeRenderer renderer) {
        Util.For(GetStagePoints(),pos-> renderer.circle(pos.x, pos.y,10));
    }

    @Override
    public void Connect() {
        Util.For(list,ip-> ip.SetIActor(GetIActor()));
    }
}
