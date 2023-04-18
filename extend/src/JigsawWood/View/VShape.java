package JigsawWood.View;

import GDX11.IObject.IAction.IMove;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.IObject.IObject;
import GDX11.Util;
import JigsawWood.Model.Shape;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

public class VShape extends Group {
    private IGroup iGroup = IObject.Get("vShape").Clone();
    public Shape shape;
    public Runnable onClick;
    public VShape(Shape shape, Group parent)
    {
        this.shape = shape;
        ITable table = iGroup.FindITable("table");
        table.column = shape.width;
        table.clone = shape.width*shape.height;
        iGroup.SetActor(this);
        iGroup.SetIRoot(parent);
        iGroup.Refresh();
        setSize(table.GetTable().getPrefWidth(),table.GetTable().getPrefHeight());
        setOrigin(Align.center);
        setPosition(parent.getWidth()/2,parent.getHeight()/2,Align.center);
        setScale(Math.min(Util.GetFitScale(this,parent),1f));
        iGroup.iParam.Set("x0",getX());
        iGroup.iParam.Set("y0",getY());
        iGroup.iParam.Set("scale0",getScaleX());

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
    private void RefreshShape()
    {
        ITable table = iGroup.FindITable("table");
        shape.For(p-> table.Get(p).GetActor().setVisible(shape.Get(p)));
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
        iGroup.RunAction("back");
    }
    public void Drop(Vector2 vPos)
    {
        iGroup.iAction.FindIMul("drop").Find("move", IMove.class).iPos.SetPosition(vPos);
        iGroup.RunAction("drop");
    }
}
