package Tool.ObjectTool.Point;

import Extend.IShape.ICircle;
import GDX11.IObject.IPos;

public class ICircleEdit extends IPoints{

    private ICircle iCircle;

    public void SetData(ICircle iCircle)
    {
        this.iCircle = iCircle;
        IPos pos = new IPos();
        pos.SetPosition(iCircle.pos.GetPosition().add(iCircle.radius,0));
        new IPoint(iCircle.pos);
    }
}
