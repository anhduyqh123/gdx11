package GDX11;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Map;

public interface Param {

    Map<String, Object> GetMap();

    Map<String, Runnable> GetEventMap();

    default void RunEvent(String name) {
        Map<String, Runnable> map = GetEventMap();
        if (map == null || !map.containsKey(name)) return;
        GetEventMap().get(name).run();
    }

    default void SetChangeEvent(String param, Runnable cb) {
        GetEventMap().put(param, cb);
    }

    default <T> T Get(String name) {
        return (T) GetMap().get(name);
    }

    default <T> T Get(String name, T value0) {
        return Get(name) != null ? Get(name) : value0;
    }

    default <T> T Get(String name, Class<T> type) {
        return Get(name);
    }

    default boolean Has(String name) {
        return GetMap().containsKey(name);
    }

    //Runnable
    default Runnable GetRun(String name) {
        return Get(name);
    }

    default void Run(String name) {
        GetRun(name).run();
    }

    default void SetRun(String name, Runnable run) {
        GetMap().put(name, run);
    }

    default void Set(String name, Object value) {
        GDX.Try(() -> {
            Put(name, value);
            RunEvent(name);
        });
    }

    default void Put(String name, Object value) {
        Object vl = value;
        //if (Has(value+"")) vl = Get(value+"");//nếu value có trong param thì thay thế
        if (Has(name)) {
            Object vl0 = Get(name);
            if (vl0 instanceof Integer && value instanceof Float)//ICount useful
                vl = ((Float) value).intValue();
            if (vl0 instanceof String) vl=value+"";//new
            if (vl0 instanceof Integer) vl = Integer.parseInt(value+"");//new
            if (vl0 instanceof Float) vl = Float.parseFloat(value+"");//new
        }
        GetMap().put(name, vl);
    }
    static Object ToBaseType(String key,String stValue) {
        return GDX.Try(()->{
            if (key.startsWith("i_")) return Integer.parseInt(stValue);
            if (key.startsWith("f_")) return Float.parseFloat(stValue);
            if (key.startsWith("v2_") || key.startsWith("v3_")) return ParseVector(stValue);
            if (key.startsWith("v4_")) return new GDX.Vector4(stValue);
            if (key.startsWith("cl_")) return Color.valueOf(stValue);
            return Json.ToBaseType(stValue);
        },()->stValue);
    }
    //vector
    static <T extends Vector> T ParseVector(String value){//(1,2)
        value = value.replace("(","").replace(")","");
        String[] arr = value.split(",");
        if (arr.length==2) return (T)new Vector2(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]));
        return (T)new Vector3(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]),Float.parseFloat(arr[2]));
    }
}
