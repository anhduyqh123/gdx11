package Tool.ObjectTool.Point;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IPos;
import GDX11.Scene;
import GDX11.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import java.util.List;

public class IPointsEdit extends Group {
    private IActor iActor;
    private List<IPos> points;
    public IPointsEdit(IActor iActor)
    {
        Vector2 pos = iActor.GetStagePosition(Align.bottomLeft);
        Actor actor = iActor.GetActor();
        //setSize(actor.getWidth(),actor.getHeight());
        setPosition(pos.x,pos.y);
        Scene.i.ui.addActor(this);
        setDebug(true);
    }
    public void SetData(List<IPos> points)
    {
        this.points = points;
        Util.For(points,this::NewPoint);
    }
    protected void NewPoint(IPos iPos)
    {
        IImage iImage = new IImage();
        iImage.iSize.width = "20";
        iImage.iSize.height = "20";
        iImage.iSize.origin = "center";
        iImage.iPos = iPos;
        iImage.SetIRoot(this);
        iImage.Refresh();

        //iPos.AddChangeEvent("point", iPos::Refresh);
    }
}
