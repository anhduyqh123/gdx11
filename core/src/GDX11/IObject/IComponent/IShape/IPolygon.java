package GDX11.IObject.IComponent.IShape;

import GDX11.Util;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class IPolygon extends IPoints {
    @Override
    public void DrawShape(ShapeRenderer renderer) {
        if (shapeType== ShapeRenderer.ShapeType.Filled) DrawFillShape(renderer);
        else renderer.polygon(Util.GetVertices(GetStagePoints()));
    }
    private void DrawFillShape(ShapeRenderer renderer)
    {
        Util.ForTriangles(GetStagePoints(),arr->
                renderer.triangle(arr[0].x,arr[0].y,arr[1].x,arr[1].y,arr[2].x,arr[2].y));
    }
}
