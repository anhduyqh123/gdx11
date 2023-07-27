package GDX11.IObject.IComponent.IShape;

import GDX11.IObject.IPos;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class ICircle extends IShape {
    public IPos pos= new IPos();
    public float radius = 100;

    @Override
    public void DrawShape(ShapeRenderer renderer) {
        Vector2 p = pos.GetPosition();
        GetActor().localToStageCoordinates(p);
        renderer.circle(p.x, p.y, radius);
    }

    @Override
    public void Connect() {
        pos.SetIActor(GetIActor());
    }
}
