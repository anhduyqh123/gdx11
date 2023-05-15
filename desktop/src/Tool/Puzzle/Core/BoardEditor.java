package Tool.Puzzle.Core;

import GDX11.Asset;
import GDX11.Config;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IActor.ITable;
import GDX11.IObject.IObject;
import GDX11.Scene;
import JigsawWood.Controller.GJigsawBoard;
import JigsawWood.Model.Shape;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.ArrayList;
import java.util.List;

public class BoardEditor extends Shape {
    public static char numID = 'a';
    private static final List<Color> colors = new ArrayList<>();
    {
        FileHandle file = new FileHandle("colors.txt");
        for (String s : file.readString().split("\n"))
            colors.add(Color.valueOf(s));
    }
    private Shape board;
    private ITable table;

    public BoardEditor(Shape board)
    {
        this.board = board;
        Scene.i.ui.clearChildren();
        IGroup iGroup = IObject.Get("ShapeEdit").Clone();
        iGroup.SetIRoot(Scene.i.ui);
        iGroup.Refresh();

        IGroup iBoard = iGroup.FindIGroup("board");
        Vector2 fitSize = new Vector2(iBoard.GetActor().getWidth(),iBoard.GetActor().getHeight());
        if (board.IsJigsaw()){
            iBoard.FindIImage("mask").visible = true;
            iBoard.FindIImage("mask").texture = board.texture;
            TextureRegion tr = Asset.i.GetTexture(board.texture);
            fitSize.set(tr.getRegionWidth(),tr.getRegionHeight());
        }
        GJigsawBoard.FitTable(iBoard.FindITable("table0"),board,fitSize);
        GJigsawBoard.FitTable(iBoard.FindITable("table"),board,fitSize);
        iBoard.Refresh();

        table = iBoard.FindITable("table");
        table.ForActor(a->{
            IImage iActor = IActor.GetIActor(a);
            Vector2 cell = table.GetCell(a);
            char id = board.Get(cell);
            Refresh(id,iActor,false);
            a.addListener(new InputListener(){
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (pointer!=0) return;
                    int button = Config.Get("button");
                    char vl = board.Get(cell);
                    if (button==1) board.Set(cell,vl==nullChar?emptyChar:nullChar);
                    else board.Set(cell,vl==numID?emptyChar:numID);
                    Refresh(board.Get(cell),iActor,false);
                }
            });
        });
    }
    private void Refresh(char id,IImage iActor,boolean hide)
    {
        iActor.SetTexture("");
        iActor.GetActor().clearActions();
        if (id==nullChar) iActor.Run("disable");
        if (id==emptyChar) iActor.Run("empty");
        if (hide)
        {
            iActor.Run("empty");
            return;
        }
        if (id>=valueChar){
            int id0 = id<='a'?id+32:id;
            iActor.GetActor().setColor(colors.get(id0-'a'));
            if (id>='A' && (id<='Z')) iActor.RunAction("wall");
        }
    }
    public void Hide(char id,boolean hide)
    {
        board.For(p->{
            if (board.Get(p)==id){
                IImage iActor = table.Get(p);
                Refresh(id,iActor,hide);
            }
        });
    }
}
