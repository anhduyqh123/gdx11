package GDX11.IObject.IActor;

import GDX11.GDX;
import GDX11.IObject.IParam;
import GDX11.Util;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ITable extends IGroup{
    public float childWidth,childHeight;
    public String contentAlign = "center",rowAlign = "center";
    public int column = 0,clone;
    public float spaceX,spaceY;
    public float padLeft,padRight,padTop,padBot;
    public boolean reverse;

    //IActor
    @Override
    protected Actor NewActor() {
        return new Table();
    }

    @Override
    protected void RefreshCore() {
        super.RefreshCore();
        if (clone>0) CloneChild(clone);
        else FillChildren(column,iMap.list);
    }

    @Override
    public void Refresh() {
        RefreshCore();
        InitEvent();
    }

    @Override
    public void RunAction(String name) {
        ForActor(i->GetIActor(i).RunAction(name));
    }

    //ITable
    public Table GetTable()
    {
        return GetActor();
    }
    private void CloneChild(int clone)
    {
        List<IActor> list = new ArrayList<>();
        Util.Repeat(clone,()-> list.add(Clone(0)));
        FillChildren(column,list);
    }
    private void FillChildren(int column, Collection<IActor> children)
    {
        Table table = GetActor();
        table.clearChildren();

        int i=0;
        NewRow(table);
        for(IActor iActor : children)
        {
            iActor.Refresh();
            Actor actor = iActor.GetActor();
            Cell cell = table.add(actor);
            if (childWidth==0) cell.width(actor.getWidth());
            if (childHeight==0) cell.height(actor.getHeight());
            if (column==0) continue;
            i++;
            if (i%column==0) NewRow(table);
        }
        table.pad(padTop,padLeft,padBot,padRight);
        table.align(IParam.GetAlign(contentAlign));
        table.validate();

        if (reverse)
        {
            List<IActor> list = new ArrayList<>(children);
            Collections.reverse(list);
            Util.ForIndex(list,x->list.get(x).GetActor().setZIndex(x));
        }
    }
    private void NewRow(Table table)
    {
        Cell cell = table.row().spaceRight(spaceX).spaceTop(spaceY);
        cell.align(IParam.GetAlign(rowAlign));
        if (childWidth!=0) cell.width(childWidth);
        if (childHeight!=0) cell.height(childHeight);
    }
    private void ForActor(GDX.Runnable1<Actor> cb)
    {
        for (Actor actor : GetTable().getChildren())
            cb.Run(actor);
    }

}
