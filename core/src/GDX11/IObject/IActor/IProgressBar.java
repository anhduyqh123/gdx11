package GDX11.IObject.IActor;

import GDX11.Actors.ProgressBar;
import GDX11.Reflect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IProgressBar extends IScrollImage{
    public float percent = 1;

    @Override
    protected Actor NewActor() {
        return new ProgressBar();
    }

    @Override
    public void SetTexture(TextureRegion texture) {
        super.SetTexture(texture);
        ProgressBar pro = GetActor();
        pro.SetValue(percent);
        Reflect.AddEvent(this,"percent",vl->pro.SetValue(percent));
        iParam.AddChangeEvent("percent",()-> pro.SetValue(iParam.Get("percent",0f)));
    }
    public ProgressBar GetActor()
    {
        return super.GetActor();
    }

    //IProgressBar
    public void SetValue(float percent)
    {
        iParam.Set("percent",percent);
    }
}
