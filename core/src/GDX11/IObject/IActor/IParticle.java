package GDX11.IObject.IActor;

import GDX11.Actors.Particle;
import GDX11.GDX;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IParticle extends IActor{
    public String parName = "";
    public boolean start = true;
    public boolean resetOnStart = true;
    public boolean autoRemove;
    @Override
    protected Actor NewActor() {
        return new Particle(){
            @Override
            public boolean remove() {
                OnRemove();
                return super.remove();
            }
        };
    }

    @Override
    public void RefreshContent() {
        Particle par = GetActor();
        GDX.Try(()->{
            par.SetEffect(parName);
            par.resetOnStart = resetOnStart;
            par.autoRemove = autoRemove;
            if (start) par.Play();
        });
    }
    public Particle GetParticle()
    {
        return GetActor();
    }
}
