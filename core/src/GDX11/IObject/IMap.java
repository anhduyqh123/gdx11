package GDX11.IObject;

import GDX11.GDX;
import GDX11.Reflect;
import GDX11.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMap<T extends IObject> {

    public List<T> list = new ArrayList<>();
    public GDX.Runnable1<T> onAdd,onRemove;
    private GDX.Func<Map<String, T>> getMap;

    public Map<String, T> GetMap()
    {
        if (getMap==null) Install();
        return getMap.Run();
    }
    private void Install()
    {
        Map<String,T> map = new HashMap<>();
        Util.For(list, i->map.put(i.name,i));
        getMap = ()->map;
    }
    public boolean Contains(String name)
    {
        return GetMap().containsKey(name);
    }
    public T Find(String name)
    {
        if (GetMap().containsKey(name)) return Get(name);
        for (T i : list)
        {
            if (i.GetIMap()==null) continue;
            T ob = (T)i.GetIMap().Find(name);
            if (ob!=null) return ob;
        }
        return null;
    }
    public T Get(String name)
    {
        return GetMap().get(name);
    }
    public void For(GDX.Runnable1<T> cb)
    {
        Util.For(list,cb);
    }

    //ForEditor
    public void Clear()
    {
        list.clear();
        Install();
    }
    public void Add(T child)
    {
        list.add(child);
        GetMap().put(child.name,child);
        if (onAdd!=null) onAdd.Run(child);
    }
    public void Remove(T child)
    {
        list.remove(child);
        GetMap().remove(child.name);
        if (onRemove!=null) onRemove.Run(child);
    }
    public void Rename(String newName,T child)
    {
        child.name = newName;
        Install();
    }
    public void Move(T child, int dir)
    {
        int index = list.indexOf(child);
        int nIndex = index+dir;
        if (nIndex<0) nIndex = 0;
        if (nIndex>=list.size()) nIndex = list.size()-1;
        T nChild = list.get(nIndex);
        list.set(nIndex,child);
        list.set(index,nChild);
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
