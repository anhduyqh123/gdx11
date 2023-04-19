package Tool.Puzzle.Form;

import GDX11.GDX;
import GDX11.Json;
import JigsawWood.Model.Board;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import Tool.JFrame.UI;
import Tool.JFrame.JObList;
import Tool.Puzzle.Core.PuzzleShapeEditor;
import com.badlogic.gdx.utils.JsonWriter;

import javax.swing.*;

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
    private JButton sClone;

    //data
    private ShapeData data = LoadData();
    public BoardForm()
    {
        JObList<Shape> jObList = new JObList<>();
        jObList.onSelectObject = this::OnSelect;
        jObList.newObject = Board::new;
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
            new PuzzleShapeEditor(shape);
        });
    }
    public void OnTab()
    {

    }
    private void OnSelect(Shape shape)
    {
        InitShapeList((Board) shape);
        tfWidth.setText(shape.width+"");
        tfHeight.setText(shape.height+"");
        new PuzzleShapeEditor(shape);
    }
    private void InitShapeList(Board board)//check
    {
        JObList<Shape> jObList = new JObList<>();
        jObList.onSelectObject = ob->{};
        jObList.newObject = Shape::new;
        UI.Button(sNew, jObList::New);
        UI.Button(sDelete, jObList::Delete);
        UI.Button(sClone, jObList::Clone);
        jObList.Init(shapeTree,board.shapes);
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
