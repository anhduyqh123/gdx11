package JigsawWood.Controller;

import GDX11.*;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IActor.ITable;
import JigsawWood.Model.JigsawBoard;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import JigsawWood.Screen.GameScreen;
import JigsawWood.Screen.WinScreen;
import JigsawWood.View.VPiece;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GJigsawBoard extends GBoard {
    protected static final List<Color> colors = new ArrayList<>();
    {
        for (String s : GDX.GetStringByKey("colors").split("\n"))
            colors.add(Color.valueOf(s));
    }
    private int level;
    private WinScreen winScreen = NewWinScreen();
    public GJigsawBoard() {
        game.FindIActor("btReset").AddClick(this::Restart);
        game.FindIActor("btHint").AddClick(this::Hint);
    }

    @Override
    protected Screen NewScreen() {
        return new GameScreen("JigsawGame");
    }

    @Override
    protected void InitItem() {

    }

    protected ShapeData LoadData()
    {
        return Json.ToObjectFomKey("jigsawData",ShapeData.class);
    }
    protected JigsawBoard GetModel()
    {
        return (JigsawBoard)model;
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
    protected void Restart() {
        game.FindIActor("board").Refresh();
        Start(level);
    }

    @Override
    protected void NewShapes() {
        newShapes.clear();
        List<Shape> shapes = new ArrayList<>(GetModel().map.values());
        slots = game.FindIGroup("footer").FindITable("table").CloneChild(shapes, this::NewView);
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
        ITable iTable = iBoard.FindITable("table");
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

    private int reward;
    private WinScreen NewWinScreen()
    {
        IGroup coin = game.FindIGroup("top").FindIGroup("coin");
        WinScreen screen = new WinScreen();
        screen.AddClick("btNext",()->{
            screen.Hide();
            Global.AddCoin(reward);
            Start(level+1);
        });
        screen.AddClick("btAd",()->{
            screen.Hide();
            Global.AddCoin(reward*2);
            Start(level+1);
        });
        screen.onShow = ()->coin.AddToParent(screen.iGroup);
        screen.onHideDone = ()->coin.AddToParent(game.FindIGroup("top"));
        return screen;
    }
    private void Win()
    {
        InitBoard(winScreen.FindIGroup("board"),model);
        ITable table = winScreen.FindITable("table");
        ITable table0 = game.FindIGroup("board").FindITable("table");
        model.For(p->{
            IActor cell = table.Get(p);
            Actor a = blockMap.get(p)!=null?blockMap.get(p):table0.Get(p).GetActor();
            cell.GetActor().setColor(a.getColor());
        });
        reward = GetModel().map.size()*10;

        game.setTouchable(Touchable.disabled);
        game.Run(()->{
            winScreen.Show(level,reward);
            game.setTouchable(Touchable.enabled);
        },0.4f);
    }
    private void Hint()
    {
        Shape shape = Util.Random(newShapes);
        shape.ForValue(p->{
            Vector2 pos = p.add(shape.GetPos());
            if (model.Empty(pos)) return;
            Shape other = GetModel().map.get(model.Get(pos));
            model.Remove(other);
            newShapes.add(other);
            GetView(other).parent.setVisible(true);
        });
        PutShape(shape.GetPos(),shape);
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
