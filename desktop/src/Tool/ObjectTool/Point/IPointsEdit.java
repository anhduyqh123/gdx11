package Tool.ObjectTool.Point;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IPos;
import GDX11.Reflect;
import GDX11.Scene;
import GDX11.Util;
import Tool.ObjectTool.Data.Event;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

public class IPointsEdit extends Group {
    private static int size = 20;
    private IActor iActor;
    private List<IPos> points;
    public Runnable onDataChange;
    public IPointsEdit(IActor iActor)
    {
        Vector2 pos = iActor.GetStagePosition(Align.bottomLeft);
        setPosition(pos.x,pos.y);
        Scene.i.ui.addActor(this);
        Event.AddKeyEvent("IPointsEdit",keyCode->{
            if (keyCode== Input.Keys.EQUALS) Resize(1);//+
            if (keyCode== Input.Keys.MINUS) Resize(-1);//-
            AddKeyEvent(keyCode);
        });
    }
    protected void AddKeyEvent(int keyCode)
    {
        if (Event.dragIActor ==null) return;
        if (keyCode== Input.Keys.NUM_1) AddLeft(Event.dragIActor.iPos);
        if (keyCode== Input.Keys.NUM_2) AddRight(Event.dragIActor.iPos);
        if (keyCode== Input.Keys.SPACE) AddAt(Event.dragIActor.iPos);
    }
    public void SetData(List<IPos> points)
    {
        this.points = points;
        Util.For(points,this::NewPoint);
    }
    protected void NewPoint(IPos iPos)
    {
        IImage iImage = new IImage();
        iImage.name = "edit";
        iImage.iSize.width = size+"";
        iImage.iSize.height = size+"";
        iImage.iPos = iPos;
        iImage.SetIRoot(this);
        iImage.Refresh();

        iImage.GetActor().addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Event.dragIActor = iImage;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (button!=1) return;
                RemovePoint(iPos);
                iImage.GetActor().remove();
            }
        });

        Reflect.AddEvent(iPos,"edit",vl->iPos.Refresh());
        //iPos.AddChangeEvent("point", iPos::Refresh);
    }
    private void AddLeft(IPos iPos)
    {
        int index = points.indexOf(iPos);
        if (index<=0) return;
        int index0 = index-1;
        AddNewPoint(points.get(index0),iPos);
    }
    private void AddRight(IPos iPos)
    {
        int index = points.indexOf(iPos);
        if (index>=points.size()-1) return;
        int index0 = index+1;
        AddNewPoint(iPos,points.get(index0));
    }
    private void AddAt(IPos iPos)
    {
        IPos newIPos = Reflect.Clone(iPos);
        NewPoint(newIPos);
        points.add(points.indexOf(iPos),newIPos);
        onDataChange.run();
    }
    private void AddNewPoint(IPos iPos1,IPos iPos2)
    {
        Vector2 mid = Util.GetMidPosition(iPos1.GetPosition(),iPos2.GetPosition());
        IPos newIPos = Reflect.Clone(iPos1);
        newIPos.SetPosition(mid);
        NewPoint(newIPos);
        points.add(points.indexOf(iPos2),newIPos);
        onDataChange.run();
    }
    private void RemovePoint(IPos iPos)
    {
        points.remove(iPos);
        onDataChange.run();
    }
    private void Resize(int del)
    {
        size+=del;
        if (size<1) size=1;
        Util.For(points,ip->{
            ip.GetActor().setSize(size,size);
        });
    }

    @Override
    protected void drawDebugChildren(ShapeRenderer shapes) {
        if (points==null) return;
        List<Vector2> list = new ArrayList<>();
        Util.For(points,i->list.add(i.GetPosition()));
        shapes.set(ShapeRenderer.ShapeType.Filled);
        for (int i=0;i<list.size()-1;i++)
            shapes.rectLine(list.get(i),list.get(i+1),size*0.1f);
    }

    @Override
    public boolean remove() {
        Event.keyEvent.remove("IPointsEdit");
        return super.remove();
    }
}
