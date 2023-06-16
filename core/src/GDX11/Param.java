package GDX11;

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
        if (Has(name)) {
            Object vl0 = Get(name);
            if (vl0 instanceof Integer && value instanceof Float)//ICount useful
                vl = ((Float) value).intValue();
        }
        GetMap().put(name, vl);
    }
}
