package Extend;

import GDX11.Config;
import GDX11.GDX;
import GDX11.Reflect;
import GDX11.Util;

import java.util.HashMap;
import java.util.Map;

public class XItem {
    private static final Map<String,XItem> map = new HashMap<>();
    public String name;
    public int value;
    private final Map<String, GDX.Runnable2<Integer,Integer>> changeMap = new HashMap<>();//old,new
    //public GDX.Func1<String,Integer> zero = vl->vl<=0?"+":vl+"";
    public GDX.Func<Boolean> onUse = ()->true;
    public Runnable outValue = ()->{};

    public XItem addItem;//add value when out value

    public XItem(){}
    public XItem(String name)
    {
        this(name, Config.Get(name));
    }
    public XItem(String name, int value0) {
        this.name = name;
        value = GDX.i.GetPrefInteger(name,value0);
    }
    public void Use(int useValue,Runnable cb) {
        if (value<useValue) outValue.run();
        else{
            if (onUse.Run())
            {
                Add(-useValue);
                cb.run();
            }
        }
    }
    public void Use(int useValue) {
        Use(useValue,()->{});
    }
    public void Use() {
        Use(1);
    }
    public void SetValue(int value) {
        GDX.i.SetPrefInteger(name,value);
        Util.For(changeMap.values(),cb->cb.Run(this.value,value));
        this.value = value;
    }
    public void Add()
    {
        Add(addItem.value);
    }
    public void Add(int add) {
        int old = value;
        value+=add;
        GDX.i.SetPrefInteger(name,value);
        Util.For(changeMap.values(),cb->cb.Run(old,value));
    }
    public XItem SetAddItem(int add) {
        addItem = New(name,add);
        return this;
    }
    public void SetChangeEvent(String name, GDX.Runnable2<Integer,Integer> cb) {
        changeMap.put(name,cb);
        cb.Run(value,value);
    }
    //static
    public static void CollectItem(XItem item)
    {
        Get(item.name).Add(item.value);
    }
    public static XItem InitItem(XItem item) {
        map.put(item.name,item);
        return item;
    }
    public static XItem New(String name,int value) {
        XItem xItem = Reflect.Clone(Get(name));
        xItem.value = value;
        return xItem;
    }
    public static XItem Get(String name) {
        return map.get(name);
    }
}
