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

public class IMap<T extends IObject> implements Json.JsonObject {

    public List<T> list = new ArrayList<>();
    public GDX.Runnable1<T> onAdd,onRemove;
    private GDX.Func<Map<String, T>> getMap;

    public IMap(){}
    public IMap(IMap iMap)
    {
        list.addAll(iMap.list);
    }

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
    public boolean Has(String name)
    {
        return GetMap().containsKey(name);
    }
    public List<T> GetObjects()//remove
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
    public void Put(T child){
        if (GetMap().containsKey(child.name)){
            int index = list.indexOf(Get(child.name));
            list.set(index,child);
            GetMap().put(child.name,child);
        }
        else Add(child);
    }
    public void Add(T child)
    {
        list.add(child);
        GetMap().put(child.name,child);
        GDX.Try(()->onAdd.Run(child));
    }
    public void Remove(T child)
    {
        if (child==null) return;
        list.remove(child);
        GetMap().remove(child.name);
        GDX.Try(()->onRemove.Run(child));
    }
    public void Remove(String name)
    {
        Remove(Get(name));
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
    @Override
    public JsonValue ToJson(Object object0)//Không thêm object, chỉ thay đổi prefab, chỉ được chỉnh sửa
    {
        JsonValue js = new JsonValue(JsonValue.ValueType.object);
        JsonValue jsArray = new JsonValue(JsonValue.ValueType.array);
        js.addChild("list",jsArray);
        IMap<T> iMap0 = (IMap<T>)object0;
        For(ob-> {
            Object ob0 = iMap0.Get(ob.name);
            if (ob.equals(ob0)) return;
            JsonValue x = Json.ToJson(ob,ob0);
            if (!x.has("name")) x.addChild("name",new JsonValue(ob.name));
            jsArray.addChild(x);
        });
        return js;
    }
    @Override
    public Object ToObject(JsonValue js)//prefab thay đổi thì object thay đổi
    {
        Util.For(js.get("list"),i->{
            Object ob = Get(i.getString("name"));
            if (ob!=null) Json.JsonToObject(i,ob);
            else Add(Json.ToObject(i));
        });
        return this;
    }
//    @Override
//    public Object ToObject(JsonValue js)
//    {
//        List list1 = new ArrayList();
//        Util.For(js.get("list"),i->{
//            Object ob = Get(i.getString("name"));
//            if (ob!=null) Json.JsonToObject(i,ob);
//            else ob = Json.ToObject(i);
//            list1.add(ob);
//        });
//        list.clear();
//        Util.For(list1,this::Add);
//        return this;
//    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
