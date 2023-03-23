package GDX11.Actions;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

public class MovePath extends TemporalAction {
    public Path<Vector2> doPath;
    public float delAngle;
    public boolean isRotate = true;
    public int align = Align.bottomLeft;

    @Override
    protected void update(float percent) {
        Vector2 out = new Vector2();
        doPath.valueAt(out, percent);
        actor.setPosition(out.x,out.y, align);
        if (isRotate)
        {
            doPath.derivativeAt(out, percent);
            actor.setRotation(out.angleDeg() +delAngle);
        }
    }
    public MovePath SetDeltaAngle(float delAngle)
    {
        this.delAngle = delAngle;
        return this;
    }
    public MovePath SetIsRotate(boolean isRotate)
    {
        this.isRotate = isRotate;
        return this;
    }
    public static MovePath Get(boolean continuous, Vector2[] path, int align, float duration, Interpolation interpolation)
    {
        MovePath movePath = Actions.action(MovePath.class);
        Vector2[] way = path;
        if (!continuous) way = GetPath(path);
        movePath.doPath = new CatmullRomSpline<>(way,continuous);
        movePath.setDuration(duration);
        movePath.setInterpolation(interpolation);
        movePath.align = align;
        return movePath;
    }
    public static MovePath Get(Vector2[] path,int align, float duration, Interpolation interpolation)
    {
        return Get(false,path,align,duration, interpolation);
    }
    public static MovePath Get(Vector2[] path, int align, float duration)
    {
        return Get(path,align,duration, Interpolation.linear);
    }
    private static Vector2[] GetPath(Vector2[] arr)
    {
        List<Vector2> list = new ArrayList<>();
        list.add(arr[0]);
        for(Vector2 pos : arr) list.add(pos);
        list.add(arr[arr.length-1]);
        return list.toArray(new Vector2[list.size()]);
    }
    //arc
    public static Action Arc(Vector2 start, Vector2 end, float percent,int align, float duration)
    {
        return Arc(start,end,percent,align,duration,Interpolation.linear);
    }
    public static Action Arc(Vector2 start, Vector2 end, float percent, int align, float duration, Interpolation interpolation)
    {
        Vector2 mid = GetNormalPos(start,end,percent);
        Vector2[] path = {start,mid,end};
        return MovePath.Get(path,align,duration,interpolation);
    }
    public static Vector2 GetNormalPos(Vector2 pos1, Vector2 pos2, float percent)
    {
        Vector2 dir = new Vector2(pos1);
        dir.sub(pos2);
        float l = dir.len()/2;
        if (percent>0) dir.set(-dir.y,dir.x);
        else dir.set(dir.y,-dir.x);
        Vector2 mid = new Vector2(pos1);
        mid.add(pos2);
        mid.scl(0.5f);
        mid.add(dir.setLength(l*Math.abs(percent)));
        return mid;
    }
}
