package JigsawWood.Controller;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IActor.ITable;
import GDX11.Json;
import GDX11.Util;
import JigsawWood.Model.JigsawBoard;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import JigsawWood.Screen.WinScreen;
import JigsawWood.View.VPiece;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.List;

public class GJigsawBoard extends GBoard {
    protected static final List<Color> colors = new ArrayList<>();
    {
        FileHandle file = new FileHandle("colors.txt");
        for (String s : file.readString().split("\n"))
            colors.add(Color.valueOf(s));
    }
    private int level;
    public GJigsawBoard(IGroup game) {
        super(game);
    }

    @Override
    protected void InitItem() {

    }

    protected ShapeData LoadData()
    {
        return Json.ToObjectFomKey("jigsawData",ShapeData.class);
    }
    protected JigsawWood.Model.JigsawBoard GetModel()
    {
        return (JigsawWood.Model.JigsawBoard)model;
    }

    @Override
    public void Start(int level) {
        this.level = level;
        Start(shapeData.GetShape(level-1));
        game.FindIGroup("top").FindIGroup("level").FindILabel("lb").SetText("LEVEL "+level);
    }
    public void Start(Shape board)
    {
        SetBoard(board);
        Start();
    }

    @Override
    protected void NewShapes() {
        slots = game.FindIGroup("footer").FindITable("table").CloneChild(GetModel().pieces,(piece,iGroup)->{
            NewView(piece,(IGroup) iGroup);
            //iGroup.GetActor().debug();
        });
        FitShapeView();
    }

    private void SetBoard(Shape board)
    {
        this.model = new JigsawBoard(board);
        GetModel().CutShapes();

        InitBoard(game.FindIGroup("board"),model);
        InitBoard();
    }
    private void InitBoard()
    {
        IGroup iBoard = game.FindIGroup("board");
        ITable iTable = iBoard.FindITable("table0");
        model.For(p->{
            Actor a = iTable.Get(p).GetActor();
            if (model.Null(p)) a.getColor().a = 0f;
            if (model.Empty(p)) a.setColor(Color.BROWN);
            if (model.HasValue(p)) a.setColor(Color.DARK_GRAY);
        });
    }
    protected void NewView(Shape shape,IGroup slot)
    {
        VPiece vPiece = new VPiece(shape,game.FindIGroup("board"),slot.GetActor());
        vPiece.onClick = ()->dragShape = shape;
        vPiece.SetMaskPos(GetMaskPos(shape));
        map.put(shape,vPiece);
        newShapes.add(shape);
    }
    private Vector2 GetMaskPos(Shape shape)
    {
        ITable iTable = game.FindIGroup("board").FindITable("table");
        Actor mask = game.FindIGroup("board").FindActor("mask");
        Vector2 pos = new Vector2(shape.x,shape.y);
        Actor cell = iTable.Get(pos).GetActor();
        return mask.localToActorCoordinates(cell,new Vector2());
    }
    private VPiece GetVPiece(Shape shape)
    {
        return (VPiece) GetView(shape);
    }

    @Override
    protected void HighLight(Vector2 pos, Shape shape) {

    }

    @Override
    protected void PutShape(Vector2 pos, Shape shape) {
        super.PutShape(pos, shape);
        game.FindIGroup("board").FindActor("front").toFront();
        if (newShapes.size()==0) Win();
    }

    protected void RemoveShape(Shape shape)
    {
        newShapes.remove(shape);
        GetView(shape).parent.setVisible(false);
        RefreshFooter();
    }
    private void Win()
    {
        IGroup coin = game.FindIGroup("top").FindIGroup("coin");
        WinScreen screen = new WinScreen(level);
        InitBoard(screen.FindIGroup("board"),model);
        screen.AddClick("btNext",()->{
            screen.Hide();
            Start(level+1);
        });
        screen.onHideDone = ()->coin.AddToParent(game.FindIGroup("top"));

        game.GetGroup().setTouchable(Touchable.disabled);
        game.Run(()->{
            coin.AddToParent(screen.iGroup);
            screen.Show();
            game.GetGroup().setTouchable(Touchable.enabled);
        },0.4f);
    }

    //static
    public static void FitTable(ITable iTable, Shape board,Vector2 fitSize)
    {
        if (iTable==null) return;
        iTable.column = board.width;
        iTable.clone = board.width*board.height;
        FitSize(iTable.GetIActor("empty"),board,fitSize);
    }
    private static void FitSize(IActor iActor, Shape shape,Vector2 fitSize)
    {
        float aWidth = Float.parseFloat(iActor.iSize.width);
        float aHeight = Float.parseFloat(iActor.iSize.height);
        float width = shape.width*aWidth+(shape.width-1)*2f;
        float height = shape.height*aHeight+(shape.height-1)*2f;;
        float scaleX = fitSize.x/width;
        float scaleY = fitSize.y/height;
        float scale = Math.max(scaleX,scaleY);
        if (!shape.IsJigsaw()){
            scale = Math.min(scaleX,scaleY);
            scale = Math.min(scale,1);
        }
        iActor.iSize.width = scale*aWidth+"";
        iActor.iSize.height = scale*aHeight+"";
    }
    public static void InitBoard(IGroup iBoard,Shape board)
    {
        Vector2 fitSize = new Vector2(iBoard.GetActor().getWidth(),iBoard.GetActor().getHeight());
        if (board.IsJigsaw()){
            TextureRegion tr = Asset.i.GetTexture(board.texture);
            iBoard.FindActor("mask").setSize(tr.getRegionWidth(),tr.getRegionHeight());
            float aScale = Util.GetFitScale(iBoard.FindActor("mask"),iBoard.GetActor());
            FitSize(iBoard.FindIImage("mask"),board.texture,aScale);
            FitSize(iBoard.FindIImage("front"),board.texture+"x",aScale);
            iBoard.FindIImage("front").visible = true;
            iBoard.iComponents.GetIComponent("draw").active = true;
            fitSize.set(tr.getRegionWidth()*aScale,tr.getRegionHeight()*aScale);
        }
        FitTable(iBoard.FindITable("table"),board,fitSize);
        FitTable(iBoard.FindITable("table0"),board,fitSize);
        iBoard.Refresh();
    }
    private static void FitSize(IImage img,String txt,float scale)
    {
        TextureRegion tr = Asset.i.GetTexture(txt);
        img.texture = txt;
        img.iSize.width = tr.getRegionWidth()*scale+"";
        img.iSize.height = tr.getRegionHeight()*scale+"";
    }
}
