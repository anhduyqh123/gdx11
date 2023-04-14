package JigsawWood.Model;

import GDX11.GDX;
import com.badlogic.gdx.math.Vector2;

public class Shape {
    public int width,height;
    protected boolean[][] grid;
    public Shape(){}
    public Shape(int width,int height)
    {
        this.width = width;
        this.height = height;
        grid = new boolean[width][height];
    }
    public boolean ForIf(GDX.Func2<Boolean,Vector2,Boolean> cb)//cb true ->return
    {
        for (int i=0;i<width;i++)
            for (int j=0;j<height;j++)
                if (!cb.Run(new Vector2(i,j),grid[i][j])) return false;
        return true;
    }
    public void For(GDX.Runnable2<Vector2,Boolean> cb)
    {
        ForIf((p,b)->{
            cb.Run(p,b);
            return true;
        });
    }
    public void Set(Vector2 pos,boolean vl){
        grid[(int)pos.x][(int)pos.y]=vl;
    }
    public boolean Get(Vector2 pos){
        return grid[(int)pos.x][(int)pos.y];
    }
    public String ToString()
    {
        return "";
    }
}
