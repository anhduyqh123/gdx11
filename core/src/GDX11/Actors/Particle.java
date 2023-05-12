package GDX11.Actors;

import GDX11.Asset;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Particle extends Actor {
    private ParticleEffect particleEffect;
    private boolean isRunning;
    public boolean autoRemove,resetOnStart=true;

    @Override
    public void act(float delta) {
        super.act(delta);
        if (particleEffect==null || !isRunning) return;
        particleEffect.setPosition(getX()+getOriginX(),getY()+getOriginY());
        particleEffect.update(delta);
        if (particleEffect.isComplete() && autoRemove) remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (particleEffect!=null && isRunning) particleEffect.draw(batch);
    }
    @Override
    protected void scaleChanged () {
        super.scaleChanged();
        if (particleEffect!=null)
            particleEffect.scaleEffect(getScaleX(), getScaleY(), getScaleY());
    }
    @Override
    public void clear() {
        isRunning = false;
        super.clear();
    }
    public void SetEffect(ParticleEffect effect)
    {
        particleEffect = new ParticleEffect(effect);
    }
    public void SetEffect(String name)
    {
        SetEffect(Asset.i.GetParticleEffect(name));
    }

    public void Start () {
        isRunning = true;
        if (resetOnStart) particleEffect.reset(false);
        particleEffect.start();
    }

    public void AllowCompletion () {
        particleEffect.allowCompletion();
    }
}
