package Tool.Puzzle.Form;

import GDX11.GDX;
import GDX11.Json;
import GDX11.Util;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import Tool.Puzzle.Core.TestGame;
import Tool.Swing.GList2;
import Tool.Swing.UI;
import Tool.Puzzle.Core.BoardEditor;
import com.badlogic.gdx.utils.JsonWriter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private JTextField tfTexture;
    private JCheckBox cbWall;
    private JButton btTest;
    private JCheckBox cbHide;

    //data
    private String name;
    private ShapeData data;
    private Shape shape;
    private BoardEditor editor;
    private List<Character> hideList = new ArrayList<>();
    public BoardForm(String name)
    {
        UI.SetUserObject(panel1,this);
        this.name = name;
        data = LoadData();
        GListX gList = new GListX("i",tree1);
        gList.onSelect = st->OnSelect(gList.GetSelectedShape());
        gList.SetRoot();

        UI.Button(btNew, gList::New);
        UI.Button(btDelete, gList::Delete);
        UI.Button(btClone, gList::Clone);
        UI.Button(saveButton,()->{
            Save();
            UI.NewDialog("save success!",panel1);
        });
        UI.Button(createButton,()->{
            Shape shape = gList.GetSelectedShape();
            shape.width = Integer.parseInt(tfWidth.getText());
            shape.height = Integer.parseInt(tfHeight.getText());
            shape.Create();
            shape.texture = tfTexture.getText();
            OnSelect(shape);
        });
        UI.TextField(tfTexture,vl->shape.texture = vl);
        UI.Button(btTest,()-> TestGame.TestJigsaw(shape));
    }
    public void OnTab()
    {
        if (shape!=null)
            OnSelect(shape);
    }
    private void OnSelect(Shape shape)
    {
        this.shape = shape;
        InitShapeList(shape);
        tfWidth.setText(shape.width+"");
        tfHeight.setText(shape.height+"");
        tfTexture.setText(shape.texture);
        editor = new BoardEditor(shape);
        if (TestGame.test) TestGame.TestJigsaw(shape);
    }
    private void InitShapeList(Shape shape)//check
    {
        List<String> list = shape.GetShapeIDs();
        if (list.size()==0) list.add("a");
        GDX.Func<String> newID = ()-> cbWall.isSelected()? GetNextChar(list,'A','Z')+"": GetNextChar(list,'a','z')+"";
        GList2 gList = new GList2(shapeTree){
            @Override
            public void New() {
                New(newID.Run());
            }

            @Override
            public void Delete() {
                Util.For(GetSelectedNodes(), n->{
                    char id = GetObject(n).charAt(0);
                    shape.Remove(id);
                });
                OnSelect(shape);
            }
        };
        gList.onSelect = id->{
            BoardEditor.numID = id.charAt(0);
            CheckHide(id.charAt(0));
        };
        gList.SetData(list);
        gList.SetSelection(list.get(0));
        UI.Button(sNew,gList::New);
        UI.Button(sDelete,gList::Delete);
        UI.CheckBox(cbHide,vl->CheckHide(gList.GetMainObject().charAt(0)));
    }
    private void CheckHide(char id)
    {
        if (editor==null) return;
        if (hideList.contains(id) && !cbHide.isSelected())
        {
            editor.Hide(id,false);
            hideList.remove((Object)id);
        }
        if (!hideList.contains(id) && cbHide.isSelected())
        {
            editor.Hide(id,true);
            hideList.add(id);
        }
    }
    private char GetNextChar(List<String> list, char start, char end) {
        if (!list.contains(start+"")) return start;
        for (String s : list)
            if (s.charAt(0)>start && s.charAt(0)<=end) start = s.charAt(0);
        return (char) (start+1);
    }

    //data
    private ShapeData LoadData()
    {
        return GDX.Try(()-> Json.ToObjectFomKey(name,ShapeData.class), ShapeData::new);
    }
    public void Save()
    {
        String stData = Json.ToJson(data).toJson(JsonWriter.OutputType.minimal);
        GDX.WriteToFile("default/data/"+name+".txt",stData);
    }

    //
    public class GListX extends GList2{
        protected String id = "";
        public GListX(String id,JTree tree) {
            super(tree);
            this.id = id;
        }

        public void SetRoot() {
            super.SetRoot("root");
            if (data.shapes.size()>0) SetSelection("i0");
        }

        @Override
        public void Refresh() {
            listID = NewIDList();
            super.Refresh();
        }
        protected List<String> NewIDList() {
            return NewIDList(id,0);
        }
        protected List<String> NewIDList(String id,int start) {
            List<String> list = new ArrayList<>();
            Util.For(start,start+data.shapes.size()-1,i->list.add(id+i));
            return list;
        }

        @Override
        public void Delete() {
            Util.For(GetSelectedNodes(), n->{
                String st = GetObject(n).replace(id,"");
                data.shapes.remove(Integer.parseInt(st));
            });
            super.Delete();
        }

        @Override
        public void New() {
            Shape shape = new Shape();
            data.shapes.add(shape);
            if (name.equals("jigsawData")) shape.texture = "shape"+data.shapes.size();
            tfTexture.setText(shape.texture);
            Refresh();
            SetSelection(id+(data.shapes.size()-1));
        }

        @Override
        public void Clone() {
            Shape clone = new Shape(GetSelectedShape());
            data.shapes.add(clone);
            Refresh();
            SetSelection(id+(data.shapes.size()-1));
        }

        public Shape GetSelectedShape()
        {
            String st = GetSelectedObject().replace(id,"");
            return data.GetShape(Integer.parseInt(st));
        }
    }
}
