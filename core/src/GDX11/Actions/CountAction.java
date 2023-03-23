package GDX11.Actions;

import GDX11.GDX;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class CountAction extends TemporalAction {
    private GDX.Runnable1<Float> onUpdate;
    private float start,delta;

    @Override
    protected void update(float percent) {
        float value = start+delta*percent;
        onUpdate.Run(value);
    }
    public static CountAction Get(GDX.Runnable1<Float> onUpdate, float start, float end, float duration, Interpolation interpolation)
    {
        CountAction action = Actions.action(CountAction.class); //check html5
        action.onUpdate = onUpdate;
        action.start = start;
        action.delta = end-start;
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        if (duration<=0) action.delta = 0;
        return action;
    }
    public static CountAction Get(GDX.Runnable1<Float> onUpdate, float start, float end, float duration)
    {
        return Get(onUpdate, start, end, duration,Interpolation.linear);
    }
}
