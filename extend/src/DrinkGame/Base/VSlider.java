package DrinkGame.Base;

import GDX11.Actors.ProgressBar;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;

public class VSlider {
    private IGroup iGroup;
    private int min,max;
    private float size;
    public GDX.Runnable1<Integer> onChange;
    public GDX.Func1<String,Integer> onValue = vl->vl+"";

    public VSlider(IGroup iGroup)
    {
        this.iGroup = iGroup;
        iGroup.GetActor().addListener(new DragListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SetDot(x);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                SetDot(x);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });
    }
    public void SetLimit(int min,int max)
    {
        this.min = min;
        this.max = max;
        size = iGroup.GetActor().getWidth()/max;
    }
    private void SetDot(float x)
    {
        float size = iGroup.GetActor().getWidth()/max;
        int len = (int)(x/size);
        SetValue(len);
    }
    public void SetValue(int value)
    {
        if (value<min || value>max) return;
        float x0 = value*size;
        IGroup iDot = iGroup.FindIGroup("dot");
        iDot.GetActor().setX(x0, Align.center);
        ProgressBar iBar = iGroup.FindActor("value");
        iBar.SetValue(x0/iGroup.GetActor().getWidth());
        iDot.FindILabel("lb").SetText(onValue.Run(value));
        onChange.Run(value);
    }
}
