package JigsawWood.Model;

import GDX11.Util;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class Board extends Shape{
    public List<Shape> shapes = new ArrayList<>();
    public Board() {
        super();
    }

    public Board(int width, int height) {
        super(width, height);
    }

    @Override
    public JsonValue ToJson(Object object0) {
        JsonValue js = super.ToJson(object0);
        JsonValue arr = new JsonValue(JsonValue.ValueType.array);
        Util.For(shapes,shape->arr.addChild(shape.ToJson()));
        js.addChild("shapes",arr);
        return js;
    }

    @Override
    public Shape ToObject(JsonValue js) {
        super.ToObject(js);
        Util.For(js.get("shapes"),i->shapes.add(new Shape().ToObject(i)));
        return this;
    }
}
