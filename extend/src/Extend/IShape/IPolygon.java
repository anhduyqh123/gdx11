package Extend.IShape;

import GDX11.Util;

public class IPolygon extends IPoints {
    @Override
    protected void DrawShape() {
        GetRenderer().polygon(Util.GetVertices(GetStagePoints()));
    }
}
