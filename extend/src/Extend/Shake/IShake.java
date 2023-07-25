package Extend.Shake;

import GDX11.IObject.IAction.IDelay;
import com.badlogic.gdx.scenes.scene2d.Action;

public class IShake extends IDelay {
    public int offsetX=3,offsetY=3;

    public IShake()
    {
        name = "shake";
    }

    @Override
    public void Run() {
    }

    @Override
    public Action Get() {
        return GShake.Get(offsetX,offsetY,GetDuration());
    }
}
