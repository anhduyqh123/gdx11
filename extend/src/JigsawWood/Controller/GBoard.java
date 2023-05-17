package JigsawWood.Controller;

import GDX11.GAudio;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IParticle;
import GDX11.IObject.IActor.ITable;
import GDX11.Scene;
import GDX11.Util;
import JigsawWood.Model.ShapeData;
import JigsawWood.Model.Shape;
import JigsawWood.View.VShape;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GBoard {
    protected IGroup game;
    protected Shape model;
    protected final ShapeData shapeData = LoadData();
    protected final Map<Shape, VShape> map = new HashMap<>();
    protected final Map<Vector2,Actor> blockMap = new HashMap<>();
    protected final List<Vector2> highlightPos = new ArrayList<>();
    protected final List<Shape> newShapes = new ArrayList<>();
    protected List<IGroup> slots = new ArrayList<>();
    protected Shape dragShape;
    protected Vector2 hitCell;
    public GBoard(IGroup game) {
        this.game = game;
        InitItem();
        InitModel();
        InitEvent();
    }
    protected abstract void InitItem();
    protected void InitModel(){}
    protected ShapeData LoadData()
    {
        return new ShapeData();
    }
    public void Start()
    {
        NewShapes();
    }
    public void Start(int level) {
        Start();
    }
    protected void Restart()
    {
        game.FindIActor("board").Refresh();
        InitModel();
        Start();
    }
    protected void NewShapes()
    {
        if (slots.size()<=0)
        {
            IGroup footer = game.FindIGroup("footer");
            Util.For(0,2,i->slots.add(footer.FindIGroup("slot"+i)));
        }
        newShapes.clear();
        Util.For(slots, this::NewShape);
        FitShapeView();
    }
    protected void FitShapeView()
    {
        float scale = 1;
        for (Shape shape : newShapes) scale = Math.min(scale,GetView(shape).GetBaseScale());
        for (Shape shape : newShapes) GetView(shape).SetScale0(scale);
    }
    private void NewShape(IGroup slot)//footer
    {
        slot.GetGroup().clearChildren();
        Shape shape = shapeData.GetRandomShape();
        VShape vShape = new VShape(shape,game.FindITable("table"),slot.GetActor());
        vShape.onClick = ()->dragShape = shape;
        map.put(shape,vShape);
        newShapes.add(shape);
    }
    private void InitEvent()
    {
        IActor scroll = game.FindIActor("scroll");
        game.GetActor().addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer!=0 || dragShape==null) return false;
                if (!newShapes.contains(dragShape)) model.Remove(dragShape);;
                if (scroll!=null) scroll.GetActor(ScrollPane.class).cancel();
                Scene.AddActorKeepTransform(GetView(dragShape),game.FindActor("footer"));
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
                else BackShape(dragShape);
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
        boolean fit = model.IsFit(hitCell,shape);
        if (!fit) hitCell = null;
    }
    protected void HighLight(Vector2 pos,Shape shape)
    {
        shape.ForValue(p-> highlightPos.add(p.add(pos)));
        ForCell(highlightPos,a->a.getColor().a=0.5f);
    }
    private void HighLightOff()
    {
        ForCell(highlightPos,a->a.getColor().a=0);
        highlightPos.clear();
    }
    protected void ForCell(List<Vector2> list,GDX.Runnable1<Actor> cb)
    {
        ITable table = game.FindIGroup("board").FindITable("table");
        Util.For(list,p->cb.Run(table.Get(p).GetActor()));
    }
    protected void BackShape(Shape shape)
    {
        if (!newShapes.contains(shape)){
            newShapes.add(shape);
            GetView(shape).parent.setVisible(true);
            RefreshFooter();
        }
        else GetView(shape).Back();
    }
    protected void PutShape(Vector2 pos,Shape shape)
    {
        Scene.AddActorKeepTransform(GetView(shape),game.FindIGroup("board").GetActor());
        Vector2 vPos = game.FindIGroup("board").FindITable("table").Get(pos).GetPosition(Align.bottomLeft);
        GetView(shape).Drop(vPos);
        shape.SetPos(pos);
        model.Set(shape);
        shape.ForValue(p-> blockMap.put(new Vector2(pos).add(p),GetView(shape).GetBlockView(p)));
        RemoveShape(shape);
        Destroy(model.GetDestroyList());
    }
    protected void RefreshFooter()
    {
        Util.For(newShapes,shape->Scene.AddActorKeepTransform(GetView(shape),game.FindActor("footer")));
        List<Actor> list = new ArrayList<>();
        Util.For(slots,ia->list.add(ia.GetActor()));
        game.FindIGroup("footer").FindITable("table").RefreshGrid(list);
        Util.For(newShapes,shape->GetView(shape).Back());
    }
    protected void RemoveShape(Shape shape)
    {
        newShapes.remove(shape);
        if (newShapes.size() == 0) NewShapes();
        GetView(shape).parent.setVisible(false);
        RefreshFooter();
    }
    protected void Destroy(List<List<Vector2>> lists)
    {
        Util.For(lists, list->{
            model.Destroy(list);
            game.Run(()->Util.For(list,p->DestroyEffect(blockMap.get(p))),0.1f);
            GAudio.i.PlaySingleSound("glass2");
            //Util.For(list,p->DestroyEffect(blockMap.get(p)));
        });
    }
    protected void DestroyEffect(Actor actor)
    {
        actor.setVisible(false);
        IParticle eff = game.FindIGroup("board").Clone("eff");
        eff.Refresh();
        Vector2 pos = actor.localToActorCoordinates(game.FindActor("board"),Scene.GetLocal(actor,Align.center));
        eff.SetPosition(pos);
        eff.GetParticle().Start();
    }
}
