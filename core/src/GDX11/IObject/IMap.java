package GDX11.IObject;

import GDX11.GDX;
import GDX11.Json;
import GDX11.Reflect;
import GDX11.Util;
import com.badlogic.gdx.utils.JsonValue;

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
    public List<T> GetObjects()
    {
        return list;
    }
    public int Size()
    {
        return list.size();
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
    public T Get(int index)
    {
        return list.get(index);
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
        GDX.Try(()->onAdd.Run(child));
    }
    public void Remove(T child)
    {
        list.remove(child);
        GetMap().remove(child.name);
        GDX.Try(()->onRemove.Run(child));
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

    //For Json
    public JsonValue ToJson(Object object0)
    {
        JsonValue js = new JsonValue(JsonValue.ValueType.object);
        JsonValue jsArray = new JsonValue(JsonValue.ValueType.array);
        js.addChild("list",jsArray);
        IMap<T> iMap0 = (IMap<T>)object0;
        For(ob-> {
            JsonValue x = Json.ObjectToJson(ob,iMap0.Get(ob.name));
            if (!x.has("name")) x.addChild("name",new JsonValue(ob.name));
            jsArray.addChild(x);
        });
        return js;
    }
    public Object ToObject(JsonValue js)
    {
        Util.For(js.get("list"),i->{
            Object ob = Get(i.getString("name"));
            if (ob!=null) Json.JsonToObject(i,ob);
            else Add(Json.ToObject(i));
        });
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
