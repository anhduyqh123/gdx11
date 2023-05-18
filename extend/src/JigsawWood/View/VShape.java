package JigsawWood.View;

import GDX11.GAudio;
import GDX11.IObject.IAction.IMove;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.IObject.IObject;
import GDX11.Scene;
import GDX11.Util;
import JigsawWood.Model.Shape;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Align;

public class VShape extends Group {
    protected final IGroup iGroup = NewIGroup();
    public Group parent;
    public Shape shape;
    public Runnable onClick;
    public VShape(Shape shape,IGroup iBoard, Group parent)
    {
        this.parent = parent;
        this.shape = shape;
        ITable table = iGroup.FindITable("table");
        table.column = shape.width;
        table.clone = shape.width*shape.height;

        ITable table0 = iBoard.FindITable("table");
        if (table0!=null)
        {
            table.GetIActor("empty").iSize.width = table0.GetIActor("empty").iSize.width;
            table.GetIActor("empty").iSize.height = table0.GetIActor("empty").iSize.height;
        }
        InitData(iBoard);

        iGroup.SetActor(this);
        iGroup.SetIRoot(parent);
        iGroup.Refresh();

        table.GetTable().setSize(table.GetTable().getPrefWidth(),table.GetTable().getPrefHeight());
        setSize(table.GetTable().getPrefWidth(),table.GetTable().getPrefHeight());
        setPosition(parent.getWidth()/2,parent.getHeight()/2,Align.center);
        setOrigin(Align.center);
        setScale(0);

        RefreshShape();

        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer!=0) return false;
                onClick.run();
                return true;
            }
        });
    }
    public void SetScale0(float scale)
    {
        iGroup.iParam.Set("x0",getX());
        iGroup.iParam.Set("y0",getY());
        iGroup.iParam.Set("scale0",scale);
        iGroup.RunAction("pre");
    }
    protected void InitData(IGroup iBoard) {
    }
    protected IGroup NewIGroup()
    {
        return IObject.Get("vShape").Clone();
    }
    private void RefreshShape()
    {
        ITable table = iGroup.FindITable("table");
        shape.For(p->{
            Actor a = table.Get(p).GetActor();
            if (shape.Null(p)) a.getColor().a = 0;
            //if (shape.Get(p)>=0) a.setColor(Color.valueOf("#FFC779"));
            if (shape.HasValue(p)) a.setColor(Color.valueOf("#FFFFCC"));
        });
        shape.For(p-> table.Get(p).GetActor().setVisible(shape.HasValue(p)));
    }
    public Actor GetBlockView(Vector2 pos)
    {
        ITable table = iGroup.FindITable("table");
        return table.Get(pos).GetActor();
    }
    public void Drag(Vector2 pos, int align)//stage pos
    {
        getParent().stageToLocalCoordinates(pos);
        setPosition(pos.x,pos.y+100,align);
        setScale(1);
    }
    public void Back()
    {
        Scene.AddActorKeepTransform(this,parent);
        iGroup.RunAction("back");
    }
    public void Drop(Vector2 vPos)
    {
        GAudio.i.PlaySingleSound("drop_ball");
        iGroup.iAction.FindIMul("drop").Find("move", IMove.class).iPos.SetPosition(vPos);
        iGroup.RunAction("drop");
    }
    public void SetColor(Color color)
    {
        ITable table = iGroup.FindITable("table");
        shape.For(p->{
            Actor a = table.Get(p).GetActor();
            if (shape.HasValue(p)) a.setColor(color);
        });
    }
    public float GetBaseScale()
    {
        float scaleX = parent.getWidth()/getWidth();
        float scaleY = parent.getHeight()/getHeight();
        float scale = Math.min(scaleX,scaleY);
        return Math.min(scale,0.6f);
    }
}
