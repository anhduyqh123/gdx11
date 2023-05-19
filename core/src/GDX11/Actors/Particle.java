package GDX11.Actors;

import GDX11.Asset;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Particle extends Actor {
    private ParticleEffect pe;
    private boolean isRunning;
    public boolean autoRemove,resetOnStart=true;

    @Override
    public void act(float delta) {
        super.act(delta);
        if (pe ==null || !isRunning) return;
        pe.setPosition(getX()+getOriginX(),getY()+getOriginY());
        pe.update(delta);
        if (pe.isComplete() && autoRemove) remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (pe !=null && isRunning) pe.draw(batch);
    }
    @Override
    protected void scaleChanged () {
        super.scaleChanged();
        if (pe !=null)
            pe.scaleEffect(getScaleX(), getScaleY(), getScaleY());
    }
    @Override
    public void clear() {
        isRunning = false;
        super.clear();
    }
    public void SetEffect(String name)
    {
        SetEffect(Asset.i.GetParticleEffect(name));
    }
    public void SetEffect(ParticleEffect effect)
    {
        pe = new ParticleEffect(effect);
        Reset();
    }
    public void Reset()
    {
        pe.reset(false);
        isRunning = false;
    }

    public void Play() {
        isRunning = true;
        if (resetOnStart) pe.reset(false);
        pe.start();
    }
    public void Stop()
    {
        pe.setDuration(0);
    }

    public void AllowCompletion () {
        pe.allowCompletion();
    }
}
