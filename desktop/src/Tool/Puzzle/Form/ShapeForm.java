package Tool.Puzzle.Form;

import GDX11.GDX;
import GDX11.Json;
import GDX11.Reflect;
import JigsawWood.Model.PuzzleShape;
import JigsawWood.Model.Shape;
import Tool.Swing.GList;
import Tool.Swing.UI;
import Tool.Puzzle.Core.PuzzleShapeEditor;
import com.badlogic.gdx.utils.JsonWriter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ShapeForm {
    public JPanel panel1;
    private JTree tree1;
    private JButton btNew;
    private JButton btDelete;
    private JButton btClone;
    private JPanel pnInfo;
    private JTextField tfWidth;
    private JTextField tfHeight;
    private JButton saveButton;
    private JButton createButton;

    //data
    private PuzzleShape data = LoadData();
    public ShapeForm()
    {
        List<String> list = new ArrayList<>(data.map.keySet());
        GList gList = new GList(tree1,null);
        gList.onSelect = n->OnSelect(data.GetShape(n));
        gList.newID = ()->{
            String name = "shape"+list.size();
            data.map.put(name,new Shape());
            return name;
        };
        gList.cloneID = ()->{
            String name = "shape"+list.size();
            Shape shape = data.GetShape(gList.GetSelectedID());
            data.map.put(name, Reflect.Clone(shape));
            return name;
        };
        if (list.size()>0)
            gList.SetSelection(list.get(0));

        UI.Button(btNew,gList::New);
        UI.Button(btDelete,gList::Delete);
        UI.Button(btClone,gList::Clone);
        UI.Button(saveButton,()->{
            Save();
            UI.NewDialog("save sucess!",panel1);
        });
        UI.Button(createButton,()->{
            Shape shape = data.GetShape(gList.GetSelectedID());
            shape.width = Integer.parseInt(tfWidth.getText());
            shape.height = Integer.parseInt(tfHeight.getText());
            shape.Create();
            new PuzzleShapeEditor(shape);
        });
    }
    public void OnTab()
    {

    }
    private void OnSelect(Shape shape)
    {
        tfWidth.setText(shape.width+"");
        tfHeight.setText(shape.height+"");
        new PuzzleShapeEditor(shape);
    }
    //data
    private PuzzleShape LoadData()
    {
        return GDX.Try(()-> Json.ToObjectFomKey("puzzleShape",PuzzleShape.class), PuzzleShape::new);
    }
    public void Save()
    {
        String stData = Json.ToJson(data).toJson(JsonWriter.OutputType.minimal);
        GDX.WriteToFile("default/data/puzzleShape.txt",stData);
    }
}
