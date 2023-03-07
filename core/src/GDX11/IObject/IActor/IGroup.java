package GDX11.IObject.IActor;

import GDX11.GDX;
import GDX11.Util;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IGroup extends IActor {

    public Map<String, IActor> map = new HashMap<>();
    public List<String> list = new ArrayList<>();

    //IActor
    @Override
    protected Actor NewActor() {
        return new Group();
    }

    @Override
    public void SetIRoot(Group group) {
        super.SetIRoot(group);
        ForIChild(this::ConnectChild);
    }
    @Override
    public void Refresh() {
        super.Refresh();
        ForIChild(IActor::Refresh);
    }

    //IGroup
    protected void ConnectChild(String name,IActor iActor)
    {
        iActor.SetIParent(this);
        iActor.SetName(name);
    }
    public void AddChild(String name,IActor iActor)
    {
        list.add(name);
        map.put(name,iActor);
        ConnectChild(name,iActor);
    }
    public void AddChildAndRefresh(String name,IActor iActor)
    {
        AddChild(name,iActor);
        iActor.Refresh();
    }
    public void Remove(String name) {
        Actor child = FindActor(name);
        if (child!=null) child.remove();
        map.remove(name);
        list.remove(name);
    }
    public void Rename(String oldName,String newName)
    {
        IActor child = map.get(oldName);
        map.remove(oldName);
        map.put(newName,child);
        int index = list.indexOf(oldName);
        list.set(index,newName);
        child.SetName(newName);
    }
    public void Move(String childName, int dir)
    {
        int index = list.indexOf(childName);
        int nIndex = index+dir;
        if (nIndex<0) nIndex = 0;
        if (nIndex>=list.size()) nIndex = list.size()-1;
        String nName = list.get(nIndex);
        list.set(nIndex,childName);
        list.set(index,nName);
    }

    public boolean Contains(String name)
    {
        return map.containsKey(name);
    }
    public <T extends IActor> T FindIActor(String name)
    {
        if (Contains(name)) return GetIActor(name);
        for(IActor ic : map.values())
            if (ic instanceof IGroup)
            {
                IActor result = ((IGroup)ic).FindIActor(name);
                if (result!=null) return (T)result;
            }
        return null;
    }
    public <T extends Actor> T FindActor(String name)
    {
        return FindIActor(name).GetActor();
    }
    public <T extends IActor> T GetIActor(String name)
    {
        return (T)map.get(name);
    }
    public void ForIChild(GDX.Runnable1<IActor> cb)
    {
        Util.For(list,name->cb.Run(map.get(name)));
    }
    public void ForIChild(GDX.Runnable2<String,IActor> cb)
    {
        Util.For(list,name->cb.Run(name,map.get(name)));
    }
    public List<IActor> GetIChildren()
    {
        List<IActor> children = new ArrayList<>();
        ForIChild((n,i)->children.add(i));
        return children;
    }

    //extend
    public void Runnable(GDX.Runnable1<IActor> cb)
    {
        super.Runnable(cb);
        ForIChild(i->i.Runnable(cb));
    }

}
