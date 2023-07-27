package Tool.ObjectTool.Point;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IComponent.IShape.IPoints;
import GDX11.IObject.IPos;
import GDX11.Reflect;
import GDX11.Scene;
import GDX11.Util;
import Tool.ObjectToolV2.Core.Event;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IPointsEdit extends Group {
    private static int size = 20;
    private Map<IPos,IPos> map = new HashMap<>();
    private IActor iActor;
    private IPoints iPoints;
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
    public void SetData(IPoints iPoints)
    {
        this.iPoints = iPoints;
        Util.For(iPoints.list,this::NewPoint);
    }
    protected void NewPoint(IPos iPos0)
    {
        IPos iPos = Reflect.Clone(iPos0);//tạo thay thế, tránh khi ipos0 di chuyển làm image di chuyển theo,trường hợp: draw shape có Ipos.SetIActor
        map.put(iPos0,iPos);
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
                RemovePoint(iPos0);
                iImage.GetActor().remove();
            }
        });

        Reflect.AddEvent(iPos,"edit",vl->{//cập nhật lại vị trí iPos0 khi iPos di chuyển
            iPos0.SetPosition(iPos.GetPosition());
            iPos.Refresh();
        });
        //Reflect.AddEvent(iPos0,"edit",vl->iPos0.Refresh());
    }
    private void AddLeft(IPos iPos)
    {
        List<IPos> points = iPoints.list;
        int index = points.indexOf(iPos);
        if (index<=0) return;
        int index0 = index-1;
        AddNewPoint(points.get(index0),iPos);
    }
    private void AddRight(IPos iPos)
    {
        List<IPos> points = iPoints.list;
        int index = points.indexOf(iPos);
        if (index>=points.size()-1) return;
        int index0 = index+1;
        AddNewPoint(iPos,points.get(index0));
    }
    private void AddAt(IPos iPos)
    {
        List<IPos> points = iPoints.list;
        IPos newIPos = Reflect.Clone(iPos);
        NewPoint(newIPos);
        points.add(points.indexOf(iPos),newIPos);
        onDataChange.run();
    }
    private void AddNewPoint(IPos iPos1,IPos iPos2)
    {
        List<IPos> points = iPoints.list;
        Vector2 mid = Util.GetMidPos(iPos1.GetPosition(),iPos2.GetPosition());
        IPos newIPos = Reflect.Clone(iPos1);
        newIPos.SetIActor(iPos1.GetIActor());
        newIPos.SetPosition(mid);
        NewPoint(newIPos);
        points.add(points.indexOf(iPos2),newIPos);
        onDataChange.run();
    }
    private void RemovePoint(IPos iPos)
    {
        List<IPos> points = iPoints.list;
        points.remove(iPos);
        onDataChange.run();
    }
    private void Resize(int del)
    {
        List<IPos> points = iPoints.list;
        size+=del;
        if (size<1) size=1;
        Util.For(points,ip->{
            map.get(ip).GetActor().setSize(size,size);
        });
    }

    @Override
    protected void drawDebugChildren(ShapeRenderer shapes) {
        if (iPoints==null) return;
        List<IPos> points = iPoints.list;
        List<Vector2> list = new ArrayList<>();
        Util.For(points,i->list.add(i.GetPosition()));
        if (iPoints.close) list.add(points.get(0).GetPosition());
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
