package Tool.Puzzle.Core;

import GDX11.GDX;
import GDX11.Json;
import JigsawWood.Model.PuzzleShape;
import com.badlogic.gdx.utils.JsonWriter;

public class PuzzleShapeEditor {
    private PuzzleShape data = LoadData();
    public PuzzleShapeEditor()
    {

    }
    private PuzzleShape LoadData()
    {
        return GDX.Try(()->{
            String stData = GDX.GetStringByKey("puzzleShape");
            return Json.ToObject(Json.StringToJson(stData), PuzzleShape.class);
        }, PuzzleShape::new);
    }
    public void Save()
    {
        String stData = Json.ToJson(data).toJson(JsonWriter.OutputType.minimal);
        GDX.WriteToFile("default/data/puzzleShape.txt",stData);
    }
}
