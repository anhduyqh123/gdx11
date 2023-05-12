package JigsawWood.Controller;

import GDX11.Asset;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Json;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import JigsawWood.View.VPiece;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GJigsawBoard extends GBoard {
    public GJigsawBoard(IGroup game) {
        super(game);
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
        Start(shapeData.GetShape(level-1));
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
    }

    private void SetBoard(Shape board)
    {
        this.model = new JigsawWood.Model.JigsawBoard(board);
        GetModel().CutShapes();

        IGroup iBoard = game.FindIGroup("board");
        Vector2 fitSize = new Vector2(iBoard.GetActor().getWidth(),iBoard.GetActor().getHeight());
        if (board.IsJigsaw()){
            iBoard.iComponents.GetIComponent("draw").active = true;
            iBoard.FindIImage("mask").texture = board.texture;
            iBoard.FindIImage("front").texture = board.texture+"x";
            iBoard.FindIImage("front").visible = true;
            TextureRegion tr = Asset.i.GetTexture(board.texture);
            fitSize.set(tr.getRegionWidth(),tr.getRegionHeight());
        }
        FitTable(iBoard.FindITable("table0"),board,fitSize);
        FitTable(iBoard.FindITable("table"),board,fitSize);
        iBoard.Refresh();
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
    private void NewView(Shape shape,IGroup slot)
    {
        ITable iTable0 = game.FindIGroup("board").FindITable("table");
        VPiece vPiece = new VPiece(shape,iTable0,slot.GetActor());
        vPiece.onClick = ()->dragShape = shape;
        vPiece.SetMaskPos(GetMaskPos(shape));
        map.put(shape,vPiece);
        newShapes.add(shape);
    }
    private Vector2 GetMaskPos(Shape shape)
    {
        ITable iTable0 = game.FindIGroup("board").FindITable("table");
        Actor mask = game.FindIGroup("board").FindActor("mask");
        Vector2 pos = new Vector2(shape.x,shape.y);
        Actor cell = iTable0.Get(pos).GetActor();
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
    }

    protected void RemoveShape(Shape shape)
    {
        newShapes.remove(shape);
        GetView(shape).parent.setVisible(false);
        RefreshFooter();
    }

    //static
    public static void FitTable(ITable iTable, Shape board,Vector2 fitSize)
    {
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
        if (!shape.IsJigsaw()) scale = Math.min(scale,1);
        iActor.iSize.width = scale*aWidth+"";
        iActor.iSize.height = scale*aHeight+"";
    }
}
