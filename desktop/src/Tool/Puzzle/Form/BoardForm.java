package Tool.Puzzle.Form;

import GDX11.GDX;
import GDX11.Json;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import Tool.Swing.GList;
import Tool.Swing.UI;
import Tool.Swing.JObList;
import Tool.Puzzle.Core.BoardEditor;
import com.badlogic.gdx.utils.JsonWriter;

import javax.swing.*;
import java.util.List;

public class BoardForm {
    private JTree tree1;
    private JButton btNew;
    private JButton btDelete;
    private JButton btClone;
    private JButton saveButton;
    private JPanel pnInfo;
    private JTextField tfWidth;
    private JTextField tfHeight;
    private JButton createButton;
    private JTree shapeTree;
    public JPanel panel1;
    private JButton sNew;
    private JButton sDelete;

    //data
    private ShapeData data = LoadData();
    public BoardForm()
    {
        JObList<Shape> jObList = new JObList<>();
        jObList.onSelectObject = this::OnSelect;
        jObList.newObject = Shape::new;
        jObList.Init(tree1,data.shapes);

        UI.Button(btNew, jObList::New);
        UI.Button(btDelete, jObList::Delete);
        UI.Button(btClone, jObList::Clone);
        UI.Button(saveButton,()->{
            Save();
            UI.NewDialog("save sucess!",panel1);
        });
        UI.Button(createButton,()->{
            Shape shape = jObList.GetSelectedObject();
            shape.width = Integer.parseInt(tfWidth.getText());
            shape.height = Integer.parseInt(tfHeight.getText());
            shape.Create();
            new BoardEditor(shape);
        });
    }
    public void OnTab()
    {

    }
    private void OnSelect(Shape shape)
    {
        InitShapeList(shape);
        tfWidth.setText(shape.width+"");
        tfHeight.setText(shape.height+"");
        new BoardEditor(shape);
    }
    private void InitShapeList(Shape shape)//check
    {
        List<String> list = shape.GetShapeIDs();
        if (list.size()==0) list.add("1");
        GDX.Func<String> newID = ()->{
            for (int i=1;i<=list.size();i++)
                if (!list.get(i-1).equals(i+"")) return i+"";
            return (list.size()+1)+"";
        };

        GList gList = new GList();
        gList.onSelect = id->BoardEditor.numID = Integer.parseInt(id);
        gList.deleteID = list::remove;
        gList.newID = ()->{
            String id = newID.Run();
            list.add(id);
            return id;
        };
        gList.Init(shapeTree,()->list);
        UI.Button(sNew,gList::New);
        //UI.Button(sDelete,gList::Delete);
    }
    //data
    private ShapeData LoadData()
    {
        return GDX.Try(()-> Json.ToObjectFomKey("boardData",ShapeData.class), ShapeData::new);
    }
    public void Save()
    {
        String stData = Json.ToJson(data).toJson(JsonWriter.OutputType.minimal);
        GDX.WriteToFile("default/data/boardData.txt",stData);
    }
}
