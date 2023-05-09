package JigsawWood.Controller;

import GDX11.Asset;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Json;
import GDX11.Util;
import JigsawWood.Model.*;
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
    protected JigsawBoard GetModel()
    {
        return (JigsawBoard)model;
    }

    @Override
    public void Start(int level) {
        SetBoard(shapeData.GetShape(level-1));
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
        this.model = new JigsawBoard(board);
        GetModel().CutShapes();

        IGroup iBoard = game.FindIGroup("board");
        iBoard.FindIImage("mask").texture = board.texture;
        iBoard.FindIImage("front").texture = board.texture+"x";
        FitTable(iBoard.FindITable("table0"),board);
        FitTable(iBoard.FindITable("table"),board);
        iBoard.Refresh();
        InitBoard();
    }
    private void FitTable(ITable iTable,Shape board)
    {
        iTable.column = board.width;
        iTable.clone = board.width*board.height;
        FitSize(iTable.GetIActor("empty"),board);
    }
    private void FitSize(IActor iActor,Shape shape)
    {
        TextureRegion tr = Asset.i.GetTexture(shape.texture);
        float aWidth = Float.parseFloat(iActor.iSize.width);
        float aHeight = Float.parseFloat(iActor.iSize.height);
        float width = shape.width*aWidth;
        float height = shape.height*aHeight;
        float scaleX = tr.getRegionWidth()/width;
        float scaleY = tr.getRegionHeight()/height;
        float scale = Math.max(scaleX,scaleY);
        iActor.iSize.width = scale*aWidth+"";
        iActor.iSize.height = scale*aHeight+"";
    }
    private void InitBoard()
    {
        ITable iTable = game.FindIGroup("board").FindITable("table0");
        model.For(p->{
            Actor a = iTable.Get(p).GetActor();
            if (model.Get(p)>=0) a.setColor(Color.BROWN);
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
    protected void RemoveShape(Shape shape)
    {
        newShapes.remove(shape);
        GetView(shape).parent.setVisible(false);
        RefreshFooter();
    }
}
