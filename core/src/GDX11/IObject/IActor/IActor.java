package GDX11.IObject.IActor;

import GDX11.GDX;
import GDX11.IObject.*;
import GDX11.IObject.IAction.IMulAction;
import GDX11.IObject.IComponent.IComponents;
import GDX11.Scene;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class IActor extends IObject {

    public String prefab = "";
    public String hexColor = Color.WHITE.toString();
    public boolean visible = true;
    public Touchable touchable = Touchable.enabled;
    public IParam iParam = new IParam();
    public ISize iSize = new ISize();
    public IPos iPos = new IPos();
    public IRunnable iRun = new IRunnable();
    public IMulAction iAction = new IMulAction();
    {
        iAction.name = "action";
    }
    public IComponents iComponents = new IComponents();

    protected GDX.Func<Actor> getActor;
    protected GDX.Func<IActor> getIRoot;
    protected GDX.Func<IGroup> getIParent;
    protected GDX.Func<Group> getParentOfRoot;

    //actor
    public <T extends Actor> T GetActor()
    {
        if (getActor==null) return null;
        return (T)getActor.Run();
    }
    public void SetActor(Actor actor)
    {
        getActor = ()->actor;
    }
    protected Actor NewActor()
    {
        return new Actor(){
            @Override
            public void act(float delta) {
                super.act(delta);
                OnUpdate(delta);
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                OnDraw(batch,parentAlpha,()->super.draw(batch, parentAlpha));
            }

            @Override
            public boolean remove() {
                OnRemove();
                return super.remove();
            }
        };
    }
    //Event
    protected void OnUpdate(float delta)
    {
        iComponents.Update(delta);
    }
    protected void OnDraw(Batch batch, float parentAlpha, Runnable onDraw)
    {
        iComponents.Draw(batch, parentAlpha, onDraw);
    }
    protected void OnRemove()
    {
        iComponents.Remove();
    }
    public void InitActor()
    {
        if (getActor==null) SetActor(NewActor());
        Clear();
        GetActor().setUserObject(this);
        JointParent();
    }
    protected void Clear()
    {
        GetActor().clear();
    }
    protected void JointParent()
    {
        try {
            Group parent = getParentOfRoot!=null?getParentOfRoot.Run():GetIParent().GetActor();
            parent.addActor(GetActor());
        }catch (Exception e){}
    }

    public IGroup GetIGroup()
    {
        return (IGroup) this;
    }
    public IGroup GetIParent()
    {
        return getIParent.Run();
    }
    public void SetIParent(IGroup iParent)
    {
        getIParent = ()->iParent;
        getIRoot = iParent.getIRoot;
        Connect();
    }
    public boolean IsRoot()
    {
        return GetIRoot().equals(this);
    }
    public <T extends IActor> T GetIRoot()
    {
        return (T)getIRoot.Run();
    }
    public void SetIRoot(Group group)
    {
        getParentOfRoot = ()->group;
        getIRoot = ()->this;
        Connect();
    }
    protected void Connect()
    {
        iParam.SetIActor(this);
        iSize.SetIActor(this);
        iPos.SetIActor(this);
        iAction.SetIActor(this);
    }
    public <T extends IActor> T IRootFind(String name)
    {
        IGroup iRoot = GetIRoot();
        return iRoot.FindIActor(name);
    }


    //refresh
    public void RefreshContent()
    {

    }
    public void RefreshLanguage()
    {

    }
    public void Refresh()
    {
        RefreshCore();
        InitEvent();
    }
    protected void RefreshCore()
    {
        InitActor();
        BaseRefresh();
        RefreshContent();
    }
    public void BaseRefresh()
    {
        iSize.Refresh();
        iPos.Refresh();
        Actor actor = GetActor();
        actor.setColor(GetColor());
        actor.setTouchable(touchable);
        actor.setVisible(visible);
        InitParam0();
    }
    protected void InitEvent()
    {
        RunEventAction("init");
    }
    protected void InitParam0()
    {
        iParam.SetParam("x0",GetActor().getX());
        iParam.SetParam("y0",GetActor().getY());
    }
    public <T> T GetParam(String st)
    {
        if (iRun.HasFunc(st)) return (T)iRun.GetFunc(st).Run();
        return (T)iParam.GetValueFromString(st);
    }
    //Action
    public void RunAction(String name)
    {
        RunEventAction(name);
    }
    protected void RunEventAction(String name)
    {
        if (!iAction.Contain(name)) return;
        GetActor().addAction(iAction.Find(name).Get());
    }

    //Get Data
    protected Color GetColor()
    {
        return Color.valueOf(hexColor);
    }

    //extend
    public void Runnable(GDX.Runnable1<IActor> cb)
    {
        cb.Run(this);
    }
    public void AddClick(Runnable onClick)
    {
        GetActor().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getPointer()!=0) return;
                onClick.run();
            }
        });
    }

    //Position
    public Vector2 GetLocalPosition(int align)
    {
        return new Vector2(GetActor().getX(align),GetActor().getY(align));
    }
    public Vector2 GetPosition(int align)
    {
        return new Vector2(GetActor().getX(align),GetActor().getY(align));
    }

    public void SetPosition(Vector2 pos,int align)
    {
        Scene.SetPosition(GetActor(),pos,align);
    }
    public void SetStagePosition(Vector2 pos,int align)
    {
        Scene.SetStagePosition(GetActor(),pos,align);
    }

    public static <T extends IActor> T GetIActor(Actor actor)
    {
        return (T)actor.getUserObject();
    }
}
