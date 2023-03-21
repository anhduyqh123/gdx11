package Extend.IShape;

import GDX11.IObject.IPos;
import com.badlogic.gdx.math.Vector2;

public class ICircle extends IShape {
    public IPos pos= new IPos();
    public float radius = 100;

    @Override
    protected void DrawShape() {
        pos.SetIActor(GetIActor());
        Vector2 p = pos.GetPosition();
        GetActor().localToStageCoordinates(p);
        GetRenderer().circle(p.x, p.y, radius);
    }
}
