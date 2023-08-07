package GDX11.IObject.IActor;

import GDX11.GDX;
import GDX11.IObject.IParam;
import GDX11.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.*;

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
    public void RefreshContent() {
        super.RefreshContent();
        RefreshLayout();
    }

    @Override
    protected void RefreshCore() {
        super.RefreshCore();
        if (clone>0) FillChildrenByClone(clone);
        else FillChildren(iMap.GetObjects());
    }

    @Override
    public void Refresh() {
        RefreshCore();
        InitEvent();
    }

    @Override
    public void RunAction(String name) {
        RunEventAction(name);
        ForActor(i->GetIActor(i).RunAction(name));
    }

    //ITable
    public Table GetTable()
    {
        return GetActor();
    }
    private void FillChildrenByClone(int clone)
    {
        iMap.Get(0).Refresh();
        List<IActor> list = new ArrayList<>();
        Util.Repeat(clone,()-> list.add(Clone(0)));
        FillChildren(list);
    }
    private <T extends IActor> void FillChildren(Collection<T> children)
    {
        List<Actor> actors = new ArrayList<>();
        Util.For(children,iActor->{
            iActor.Refresh();
            actors.add(iActor.GetActor());
        });
        RefreshGrid(actors);
    }
    public void RefreshLayout()
    {
        List<Actor> list = new ArrayList<>();
        Util.For(iMap.GetObjects(),ia->list.add(ia.GetActor()));
        RefreshGrid(list);
    }
    public void RefreshGrid(){
        RefreshGrid(Arrays.asList(GetTable().getChildren().toArray()));
    }
    public void RefreshGrid(List<Actor> children) {
        Table table = GetActor();
        table.clearChildren();

        int i=0;
        NewRow(table);
        for(Actor actor : children)
        {
            if (!actor.isVisible()) continue;
            Cell cell = table.add(actor);
            if (childWidth==0) cell.width(actor.getWidth());
            if (childHeight==0) cell.height(actor.getHeight());
            if (column==0) continue;
            i++;
            if (i%column==0) NewRow(table);
        }
        table.pad(padTop,padLeft,padBot,padRight);
        table.align(IParam.GetAlign(contentAlign));
        table.layout();

        if (reverse)
        {
            Collections.reverse(children);
            Util.ForIndex(children,x->children.get(x).setZIndex(x));
        }
        Util.For(children,a->GetIActor(a).InitParam0());
    }
    private void NewRow(Table table)
    {
        Cell cell = table.row().spaceRight(spaceX).spaceTop(spaceY);
        cell.align(IParam.GetAlign(rowAlign));
        if (childWidth!=0) cell.width(childWidth);
        if (childHeight!=0) cell.height(childHeight);
    }
    public void ForActor(GDX.Runnable1<Actor> cb)
    {
        for (Actor actor : GetTable().getChildren())
            cb.Run(actor);
    }
    public <T extends IActor> T NewChildFrom(int index)
    {
        T iActor = Clone(index);
        iActor.Refresh();
        List<Actor> list = new ArrayList<>();
        Util.For(iMap.GetObjects(),ia->list.add(ia.GetActor()));
        list.add(iActor.GetActor());
        RefreshGrid(list);
        return iActor;
    }
    //Clone
    public <T extends IActor> List<T> CloneChild(int amount)
    {
        List<T> iActors = new ArrayList<>();
        Util.Repeat(amount,()->iActors.add(Clone(0)));
        FillChildren(iActors);
        return iActors;
    }
    public <T,E extends IActor> List<E> CloneChild(List<T> list, GDX.Runnable2<T,E> cb)
    {
        return CloneChild(list,(i,t,e)->cb.Run(t,e));
    }
    public <T,E extends IActor> List<E> CloneChild(List<T> list, GDX.Runnable3<Integer,T,E> cb)
    {
        List<E> iActors = CloneChild(list.size());
        for(int i=0;i<list.size();i++)
            cb.Run(i,list.get(i),iActors.get(i));
        return iActors;
    }

    //util
    public <T extends IActor> T Get(int index){
        return Get(index%column,index/column);
    }
    public <T extends IActor> T Get(int x,int y)//origin of bottomLeft
    {
        int row = GetTable().getChildren().size/column;
        int index = (row-1-y)*column+x;
        Actor actor = GetTable().getChild(index);
        return IActor.GetIActor(actor);
    }
    public <T extends IActor> T Get(Vector2 cell)//origin of bottomLeft
    {
        return Get((int)cell.x,(int)cell.y);
    }
    public Vector2 GetCell(Actor actor)
    {
        int row = GetTable().getChildren().size/column;
        int index= actor.getZIndex();
        int x = index%column;
        int y = index/column;
        return new Vector2(x,row-1-y);
    }
}
