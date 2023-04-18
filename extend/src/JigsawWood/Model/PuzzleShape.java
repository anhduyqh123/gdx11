package JigsawWood.Model;

import GDX11.Json;
import GDX11.Util;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class PuzzleShape implements Json.JsonObject {
    public Map<String,Shape> map = new HashMap<>();
    public Shape GetRandomShape()
    {
        int id = MathUtils.random(0,map.size());
        String key = (String) map.keySet().toArray()[id];
        return map.get(key);
    }

    @Override
    public JsonValue ToJson(Object object0) {
        JsonValue js = new JsonValue(JsonValue.ValueType.object);
        for (String key : map.keySet())
            js.addChild(key,map.get(key).ToJson(null));
        return js;
    }

    @Override
    public Object ToObject(JsonValue js) {
        Util.For(js,i-> map.put(i.name,(Shape) new Shape().ToObject(i)));
        return this;
    }
}
