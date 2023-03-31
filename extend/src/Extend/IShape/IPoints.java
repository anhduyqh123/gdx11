package Extend.IShape;

import GDX11.IObject.IPos;
import GDX11.Util;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class IPoints extends IShape {
    public List<IPos> list = new ArrayList<>();

    {
        list.add(new IPos());
        list.add(new IPos(0, 100));
        list.add(new IPos(100, 0));
    }
    protected List<Vector2> GetStagePoints()
    {
        List<Vector2> points = new ArrayList<>();
        Util.For(list,ip-> {
            ip.SetIActor(GetIActor());
            points.add(GetStagePos(ip));
        });
        return points;
    }

    @Override
    protected void DrawShape() {
        GetRenderer().set(ShapeRenderer.ShapeType.Filled);
        Util.For(GetStagePoints(),pos-> GetRenderer().circle(pos.x, pos.y,10));
    }
}
