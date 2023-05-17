package Extend;

import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.Reflect;
import GDX11.Util;

import java.util.HashMap;
import java.util.Map;

public class XItem {
    private static Map<String,XItem> map = new HashMap<>();
    public static GDX.Runnable1<XItem> outItem = item->{};
    public static GDX.Runnable1<XItem> useEvent = item->{};

    public String name,txtName;
    public int value;
    private final Map<String, GDX.Runnable2<Integer,Integer>> changeMap = new HashMap<>();//old,new
    public GDX.Func1<String,Integer> zero = vl->vl<=0?"+":vl+"";
    public GDX.Runnable1<GDX.Runnable> onUse;//Run(done)
    public XItem addItem;//add value when out value

    public XItem(){}
    public XItem(String name)
    {
        this(name, Config.Get(name));
    }
    public XItem(String name, int value0)
    {
        this.name = name;
        this.txtName = name;
        value = GDX.i.GetPrefInteger(name,value0);
    }
    public XItem(String name, String txtName, int value0)
    {
        this.name = name;
        this.txtName = txtName;
        value = GDX.i.GetPrefInteger(name,value0);
    }
    public void Use()
    {
        if (value<=0) outItem.Run(this);
        else onUse.Run(()->{
            useEvent.Run(this);
            Add(-1);
        });
    }
    public void SetView(IGroup iGroup)
    {
        AddChangeEvent("view",(o,n)->{
            iGroup.FindIImage("icon").name = txtName;
            iGroup.FindIImage("icon").Refresh();
            iGroup.FindILabel("lb").ReplaceText(zero.Run(value));
        });
    }
    public void SetValue(int value)
    {
        GDX.i.SetPrefInteger(name,value);
        Util.For(changeMap.values(),cb->cb.Run(this.value,value));
        this.value = value;
    }
    public void Add(int add)
    {
        int old = value;
        value+=add;
        GDX.i.SetPrefInteger(name,value);
        Util.For(changeMap.values(),cb->cb.Run(old,value));
    }
    public XItem SetAddItem(int add)
    {
        addItem = New(name,add);
        return this;
    }
    public XItem SetEvent(GDX.Runnable1<GDX.Runnable> onUse)
    {
        this.onUse = onUse;
        return this;
    }
    public void AddChangeEvent(String name, GDX.Runnable2<Integer,Integer> cb)
    {
        changeMap.put(name,cb);
        cb.Run(value,value);
    }
    public XItem SetZero(GDX.Func1<String,Integer> zero)
    {
        this.zero = zero;
        return this;
    }
    //static
    public static void CollectItem(XItem item)
    {
        Get(item.name).Add(item.value);
    }
    public static XItem InitItem(XItem item)
    {
        map.put(item.name,item);
        return item;
    }
    public static XItem New(String name,int value)
    {
        XItem xItem = Reflect.Clone(Get(name));
        xItem.value = value;
        return xItem;
    }
    public static XItem New(String name,String txtName,int value)
    {
        XItem xItem = New(name, value);
        xItem.txtName = txtName;
        return xItem;
    }
    public static XItem Get(String name)
    {
        return map.get(name);
    }
}
