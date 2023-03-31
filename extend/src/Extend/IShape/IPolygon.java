package Extend.IShape;

import GDX11.Util;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class IPolygon extends IPoints {
    public ShapeRenderer.ShapeType shapeType = ShapeRenderer.ShapeType.Line;
    @Override
    protected void DrawShape() {
        GetRenderer().set(shapeType);
        GetRenderer().polygon(Util.GetVertices(GetStagePoints()));
    }
}
