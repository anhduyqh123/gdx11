package Extend.IShape;

import com.badlogic.gdx.math.Vector2;

public class ICircle extends IShape {
    public Vector2 pos = new Vector2();
    public float radius = 100;

    @Override
    protected void DrawShape() {
        Vector2 p = GetActor().localToStageCoordinates(new Vector2(pos));
        GetRenderer().circle(p.x, p.y, radius);
    }
}
