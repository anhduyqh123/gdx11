package GDX11.IObject.IActor;

import GDX11.GDX;
import GDX11.IObject.IMap;
import GDX11.Util;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IGroup extends IActor {
    public IMap<IActor> iMap = new IMap<>();
    {
        iMap.onAdd = this::OnAddChild;
    }

    //IActor
    @Override
    protected Actor NewActor() {
        return new Group();
    }

    @Override
    public void SetIRoot(Group group) {
        super.SetIRoot(group);
        ForIChild(i->i.SetIParent(this));
    }

    @Override
    public void SetIParent(IGroup iParent) {
        super.SetIParent(iParent);
        ForIChild(i->i.SetIParent(this));
    }

    @Override
    public void Refresh() {
        super.Refresh();
        ForIChild(IActor::Refresh);
    }

    @Override
    public void RunAction(String name) {
        super.RunAction(name);
        ForIChild(i->i.RunAction(name));
    }

    //IGroup
    protected void OnAddChild(IActor iActor)
    {
        iActor.SetIParent(this);
    }

    public boolean Contains(String name)
    {
        return iMap.Contains(name);
    }
    public <T extends IActor> T FindIActor(String name)
    {
        return (T)iMap.Find(name);
    }
    public <T extends Actor> T FindActor(String name)
    {
        return FindIActor(name).GetActor();
    }
    public <T extends IActor> T GetIActor(String name)
    {
        return (T)iMap.Get(name);
    }
    public void ForIChild(GDX.Runnable1<IActor> cb)
    {
        iMap.For(cb);
    }

    //extend
    public void Runnable(GDX.Runnable1<IActor> cb)
    {
        super.Runnable(cb);
        ForIChild(i->i.Runnable(cb));
    }

}
