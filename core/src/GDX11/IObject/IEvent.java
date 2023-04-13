package GDX11.IObject;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Reflect;

import java.util.HashMap;
import java.util.Map;

public class IEvent {
    //static
    public static Map<String, GDX.Runnable1<IActor>> runMap = new HashMap<>();
    public static void SetGameRun(String name, GDX.Runnable1<IActor> cb)
    {
        runMap.put(name,cb);
    }
    public static GDX.Runnable1<IActor> GetGameRun(String name)
    {
        return runMap.get(name);
    }
    public static boolean GameHasRun(String name)
    {
        return runMap.containsKey(name);
    }
    private GDX.Func<Map> getRunMap;

    //Function
//    private GDX.Func<Map> getFuncMap;
//    public Map<String, GDX.Func> GetFuncMap()
//    {
//        if (getFuncMap==null)
//        {
//            Map<String, GDX.Func> map = new HashMap<>();
//            getFuncMap = ()->map;
//        }
//        return getFuncMap.Run();
//    }
//    public void SetFunc(String name, GDX.Func func)
//    {
//        GetFuncMap().put(name, func);
//    }
//    public GDX.Func GetFunc(String name)
//    {
//        return GetFuncMap().get(name);
//    }
//    public boolean HasFunc(String name)
//    {
//        return GetFuncMap().containsKey(name);
//    }

    //Runnable
    public Map<String, GDX.Runnable> GetRunMap()
    {
        if (getRunMap==null)
        {
            Map<String, GDX.Runnable> map = new HashMap<>();
            getRunMap = ()->map;
        }
        return getRunMap.Run();
    }
    public void SetRun(String name, GDX.Runnable run)
    {
        GetRunMap().put(name, run);
    }
    public GDX.Runnable GetRun(String name)
    {
        return GetRunMap().get(name);
    }
    public boolean HasRun(String name)
    {
        return GetRunMap().containsKey(name);
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
