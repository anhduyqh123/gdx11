package GDX11.IObject.IActor;

import GDX11.GDX;
import GDX11.IObject.IComponent.IComponent;
import GDX11.IObject.IMap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;
import java.util.Map;

public class IGroup extends IActor implements IFind {
    public IMap<IActor> iMap = new IMap<>();
    {
        iMap.onAdd = this::OnAddChild;
        iMap.onRemove = this::OnRemoveChild;
    }

    //IActor
    @Override
    protected Actor NewActor() {
        return new Group(){
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

    @Override
    public void SetIRoot(Group group) {
        super.SetIRoot(group);
        ForIChild(i->i.SetIParent(this));
    }

    @Override
    public void SetIParent(IGroup iParent) {
        super.SetIParent(iParent);
        ForIChild(i->i.SetIParent(this));
    }
    @Override
    public void RefreshContent() {
        ForIChild(IActor::RefreshContent);
    }

    @Override
    public void RefreshLanguage() {
        ForIChild(IActor::RefreshLanguage);
    }

    @Override
    public void Refresh() {
        super.Refresh();
        ForIChild(IActor::Refresh);
    }

    @Override
    protected void RefreshCore() {
        InitActor();
        BaseRefresh();
    }

    @Override
    public void RunAction(String name) {
        super.RunAction(name);
        ForIChild(i->i.RunAction(name));
    }

    @Override
    public void Run(String name) {
        super.Run(name);
        ForIChild(i->i.Run(name));
    }

    //IGroup
    public Group GetGroup()
    {
        return GetActor();
    }
    protected void OnAddChild(IActor iActor)
    {
        iActor.SetIParent(this);
    }
    protected void OnRemoveChild(IActor iActor){
        iActor.GetActor().remove();
    }

    public boolean Contains(String name)
    {
        return iMap.Contains(name);
    }
    public <T extends IActor> T GetIActor(String name)
    {
        return (T)iMap.Get(name);
    }

    public void ForIChild(GDX.Runnable1<IActor> cb)
    {
        iMap.For(cb);
    }

    //extend
    public void Runnable(GDX.Runnable1<IActor> cb)
    {
        super.Runnable(cb);
        ForIChild(i->i.Runnable(cb));
    }

    //Clone
    public <T extends IActor> T Clone(int index)
    {
        T clone = iMap.Get(index).Clone();
        clone.SetIParent(this);
        return clone;
    }
    public <T extends IActor> T Clone(String name)
    {
        T clone = iMap.Find(name).Clone();
        clone.SetIParent(this);
        return clone;
    }

    //pool
    private GDX.Func<Map> getPool;
    public Map<String,Pool> GetPool()
    {
        if (getPool==null)
        {
            Map<String,Pool> map = new HashMap<>();
            getPool = ()->map;
        }
        return getPool.Run();
    }
    public boolean HasBool(String childName)
    {
        return GetPool().containsKey(childName);
    }
    public void NewPool(String childName,int size)
    {
        IActor iChild = GetIActor(childName);
        Pool<IActor> pool = new Pool<IActor>() {
            @Override
            protected IActor newObject() {
                IActor iClone = iChild.Clone();
                iClone.InitActor();
                iClone.iComponents.Add(new IComponent("remove"){
                    @Override
                    public void Remove() {
                        GetPool().get(childName).free(iClone);
                    }
                });
                return iClone;
            }
        };
        pool.fill(size);
        GetPool().put(childName,pool);
    }
    public  <T extends IActor> T Obtain(String childName)
    {
        IActor iChild = GetIActor(childName);
        IActor iClone = (IActor) GetPool().get(childName).obtain();
        iClone.SetIParent(iChild.GetIParent());
        iClone.Refresh();
        //iClone.GetActor().setZIndex(iChild.GetActor().getZIndex()+1);
        return (T)iClone;
    }
}

