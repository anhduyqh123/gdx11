package JigsawWood.Controller;

import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Scene;
import GDX11.Util;
import JigsawWood.Model.Board;
import JigsawWood.Model.Shape;
import JigsawWood.View.VShape;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GBoard {
    private IGroup game;
    private Board board = new Board(9,9);
    private Map<Shape, VShape> map = new HashMap<>();
    private Map<Vector2,Actor> blockMap = new HashMap<>();
    private final List<Vector2> highlightPos = new ArrayList<>();
    private List<Shape> newShapes = new ArrayList<>();
    private Shape dragShape;
    private Vector2 hitCell;
    public GBoard(IGroup game)
    {
        this.game = game;

        NewShapes();
        InitEvent();
    }
    private void NewShapes()
    {
        IGroup footer = game.FindIGroup("footer");
        Util.For(0,2,i->NewShape(footer.FindIGroup("slot"+i)));
    }
    private void NewShape(IGroup slot)//footer
    {
        Shape shape = Shape.NewShape2();
        VShape vShape = new VShape(shape,slot.GetActor());
        vShape.onClick = ()->dragShape = shape;
        map.put(shape,vShape);
        newShapes.add(shape);
    }
    private void InitEvent()
    {
        game.GetActor().addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer!=0 || dragShape==null) return false;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                GetView(dragShape).Drag(new Vector2(x,y), Align.bottom);
                Check(dragShape);
                HighLightOff();
                if (hitCell!=null) HighLight(hitCell,dragShape);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (hitCell!=null) PutShape(hitCell,dragShape);
                else GetView(dragShape).Back();
                dragShape = null;
                hitCell = null;
                HighLightOff();
            }
        });
    }
    public VShape GetView(Shape shape)
    {
        return map.get(shape);
    }
    private void Check(Shape shape)
    {
        ITable table = game.FindIGroup("board").FindITable("table");
        float blockSize = table.GetTable().getChild(0).getWidth();
        Vector2 vPos = GetView(shape).localToActorCoordinates(game.FindActor("board"),new Vector2(blockSize/2,0));
        Actor hit = table.GetTable().hit(vPos.x,vPos.y,false);
        if (hit==null)
        {
            hitCell = null;
            return;
        }
        if (hit instanceof Group) return;
        hitCell = table.GetCell(hit);
        if (!board.IsFit(hitCell,shape)) hitCell = null;
    }
    private void HighLight(Vector2 pos,Shape shape)
    {
        ITable table = game.FindIGroup("board").FindITable("table");
        shape.ForTrue(p-> highlightPos.add(p.add(pos)));
        Util.For(highlightPos,p->table.Get(p).GetActor().getColor().a=0.5f);
    }
    private void HighLightOff()
    {
        ITable table = game.FindIGroup("board").FindITable("table");
        Util.For(highlightPos,p->table.Get(p).GetActor().getColor().a=0);
        highlightPos.clear();
    }
    private void PutShape(Vector2 pos,Shape shape)
    {
        Scene.AddActorKeepTransform(GetView(shape),game.FindIGroup("board").GetActor());
        Vector2 vPos = game.FindIGroup("board").FindITable("table").Get(pos).GetPosition(Align.bottomLeft);
        GetView(shape).Drop(vPos);
        board.Set(pos,shape);
        shape.ForTrue(p-> blockMap.put(new Vector2(pos).add(p),GetView(shape).GetBlockView(p)));
        newShapes.remove(shape);
        if (newShapes.size() == 0) NewShapes();
        Destroy();
    }
    private void Destroy()
    {
        Util.For(board.GetDestroyList(),list->{
            board.Destroy(list);
            Util.For(list,p-> blockMap.get(p).setVisible(false));
        });
    }
}
