package GDX11;

import com.badlogic.gdx.math.Vector2;

import java.util.Collection;

public class Util {
    //vector
    public static float GetAngle(Vector2 p1, Vector2 p2, Vector2 p3)
    {
        Vector2 dir1 = GetDirect(p2,p1);
        Vector2 dir2 = GetDirect(p2,p3);
        return dir1.angleDeg(dir2);
    }
    public static Vector2 GetMidPosition(Vector2 pos1, Vector2 pos2)
    {
        Vector2 pos = new Vector2(pos1);
        pos.add(pos2);
        return pos.scl(0.5f);
    }
    public static Vector2 GetDirect(Vector2 pos1, Vector2 pos2)
    {
        Vector2 pos = new Vector2(pos2);
        return pos.sub(pos1);
    }
    public static float GetDistance(Vector2 pos1, Vector2 pos2)
    {
        return GetDirect(pos1,pos2).len();
    }
    public static float GetDistance(Vector2 pos,Vector2 p1, Vector2 p2)//p1,p2 is points of line
    {
        float a = GetDistance(p1,p2);
        float b = GetDistance(p1,pos);
        float c = GetDistance(pos,p2);
        float p = (a+b+c)/2;
        return 2*((float)Math.sqrt(p*(p-a)*(p-b)*(p-c))/a);
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
    public static void Round(Vector2 v)
    {
        v.x = Math.round(v.x);
        v.y = Math.round(v.y);
    }
    //for
    public static <T> void For(Collection<T> list, GDX.Runnable1<T> cb)
    {
        for (T i : list) cb.Run(i);
    }
    public static <T> void For(int from,int to,GDX.Runnable1<Integer> cb)
    {
        for (int i=from;i<=to;i++) cb.Run(i);
    }

    //convert

}
