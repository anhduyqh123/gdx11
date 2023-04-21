package JigsawWood.Model;

import GDX11.Json;
import GDX11.Util;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class ShapeData implements Json.JsonObject {
    public List<Shape> shapes = new ArrayList<>();
    public Shape GetRandomShape()
    {
        int id = MathUtils.random(0,shapes.size()-1);
        return new Shape(GetShape(id));
    }
    public Shape GetShape(int index){
        return shapes.get(index);
    }
    @Override
    public JsonValue ToJson(Object object0) {
        JsonValue js = new JsonValue(JsonValue.ValueType.array);
        Util.For(shapes,shape->js.addChild(shape.ToJson()));
        return js;
    }

    @Override
    public Object ToObject(JsonValue js) {
        Util.For(js, i-> shapes.add(new Shape().ToObject(i)));
        return this;
    }
}
