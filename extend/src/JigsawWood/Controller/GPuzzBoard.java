package JigsawWood.Controller;

import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Json;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import JigsawWood.Screen.GameScreen;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class GPuzzBoard extends GJigsawBoard{
    @Override
    protected GameScreen NewScreen() {
        return new GameScreen("PuzzGame");
    }
    @Override
    protected ShapeData LoadData() {
        return Json.ToObjectFomKey("puzzData",ShapeData.class);
    }

    @Override
    protected void NewView(Shape shape, IGroup slot) {
        super.NewView(shape, slot);
        Color color = new Color(colors.get(newShapes.size()-1));
        color.lerp(Color.WHITE,0.5f);
        color.a = 1;
        GetView(shape).SetColor(color);
    }
    @Override
    protected void NewShapes() {
        ITable table = game.FindIGroup("footer").FindITable("table");
        List<Shape> shapes = new ArrayList<>(GetModel().map.values());
        int row = shapes.size()/4+(shapes.size()%4>0?1:0);
        table.column = row>2?5:4;
        table.spaceX = row>2?10:40;
        newShapes.clear();
        slots = table.CloneChild(shapes, this::NewView);
        FitShapeView();
    }
}
