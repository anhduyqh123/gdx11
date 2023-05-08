package JigsawWood.Controller;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.IObject.IObject;
import GDX11.Json;
import GDX11.Util;
import JigsawWood.Model.JigsawBoard;
import JigsawWood.Model.Piece;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

public class GJigsawBoard {
    private IGroup game;
    private ShapeData shapeData = Json.ToObjectFomKey("boardData",ShapeData.class);
    private JigsawBoard board;

    public GJigsawBoard(IGroup game)
    {
        this.game = game;
        SetBoard(shapeData.GetShape(2));
    }
    private void SetBoard(Shape board)
    {
        this.board = new JigsawBoard(board);
        this.board.CutShapes();

        IGroup iBoard = game.FindIGroup("board");
        iBoard.FindIImage("mask").texture = board.texture;
        iBoard.FindIImage("front").texture = board.texture+"x";
        ITable iTable = iBoard.FindITable("table");
        iTable.column = board.width;
        iTable.clone = board.width*board.height;
        FitSize(iTable.GetIActor("empty"),board);
        iBoard.Refresh();
        InitBoard();
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
        ITable iTable = game.FindIGroup("board").FindITable("table");
        board.For(p->{
            Actor a = iTable.Get(p).GetActor();
            if (board.Get(p)>=0) a.setColor(Color.BROWN);
        });
        game.FindIGroup("footer").FindITable("table").CloneChild(board.pieces,(piece,iGroup)->{
            NewView(piece,(IGroup) iGroup);
            iGroup.GetActor().debug();
        });
    }
    private void NewView(Piece piece,IGroup slot)
    {
        ITable iTable0 = game.FindIGroup("board").FindITable("table");

        IGroup iGroup = IObject.Get("vPiece").Clone();
        ITable iTable = iGroup.FindITable("table");
        iTable.column = piece.width;
        iTable.clone = piece.width*piece.height;
        iTable.GetIActor("empty").iSize.width = iTable0.GetIActor("empty").iSize.width;
        iTable.GetIActor("empty").iSize.height = iTable0.GetIActor("empty").iSize.height;

        iGroup.SetIRoot(slot.GetActor());
        iGroup.Refresh();
        iGroup.GetActor().setSize(iTable.GetTable().getPrefWidth(),iTable.GetTable().getPrefHeight());
        iGroup.GetActor().setPosition(0,0);
        Vector2 maskPos = GetMaskPos(piece);
        iGroup.FindIActor("mask").SetPosition(maskPos, Align.bottomLeft);
        iTable.GetTable().setSize(iTable.GetTable().getPrefWidth(),iTable.GetTable().getPrefHeight());

        piece.For(p->{
            Actor a = iTable.Get(p).GetActor();
            if (piece.Get(p)>=0) a.setColor(Color.GOLD);
        });
        //iGroup.GetGroup().debug();
    }
    private Vector2 GetMaskPos(Piece piece)
    {
        ITable iTable0 = game.FindIGroup("board").FindITable("table");
        Actor mask = game.FindIGroup("board").FindActor("mask");
        Vector2 pos = new Vector2(piece.x,piece.y);
        Actor cell = iTable0.Get(pos).GetActor();
        return mask.localToActorCoordinates(cell,new Vector2());
    }
}
