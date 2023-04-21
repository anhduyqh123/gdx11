package JigsawWood.Model;

import GDX11.Json;
import GDX11.Util;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;

import java.util.LinkedHashMap;
import java.util.Map;

public class PuzzleShape implements Json.JsonObject {
    public Map<String,Shape> map = new LinkedHashMap<>();
    public Shape GetRandomShape()
    {
        int id = MathUtils.random(0,map.size()-1);
        String key = (String) map.keySet().toArray()[id];
        return new Shape(map.get(key));
    }
    public Shape GetShape(String name){
        return map.get(name);
    }

    @Override
    public JsonValue ToJson(Object object0) {
        JsonValue js = new JsonValue(JsonValue.ValueType.object);
        for (String key : map.keySet())
            js.addChild(key,map.get(key).ToJson());
        return js;
    }

    @Override
    public Object ToObject(JsonValue js) {
        Util.For(js,i-> map.put(i.name,new Shape().ToObject(i)));
        return this;
    }
}
