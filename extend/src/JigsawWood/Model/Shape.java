package JigsawWood.Model;

import GDX11.GDX;
import GDX11.Json;
import GDX11.Util;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Shape implements Json.JsonObject {
    public int width=4,height=4;
    protected int[][] grid = new int[width][height];
    //0 is null
    //1 is empty
    //2 is value
    public Shape(){
        Create();
    }
    public Shape(int width,int height)
    {
        this.width = width;
        this.height = height;
        Create();
    }
    public Shape(Shape shape)
    {
        width = shape.width;
        height = shape.height;
        Create();
        shape.For(p->Set(p,shape.Get(p)));
    }
    public void Create()
    {
        grid = new int[width][height];
        For(p->Set(p,1));
    }
    public boolean ForIf(GDX.Func1<Boolean,Vector2> cb)//cb true ->return
    {
        for (int i=0;i<width;i++)
            for (int j=0;j<height;j++)
                if (!cb.Run(new Vector2(i,j))) return false;
        return true;
    }
    public void For(GDX.Runnable1<Vector2> cb)
    {
        ForIf(p->{
            cb.Run(p);
            return true;
        });
    }
    public void ForTrue(GDX.Runnable1<Vector2> cb)
    {
        For(p->{
            if (Get(p)==2) cb.Run(p);
        });
    }
    public void ForBlock(GDX.Runnable1<Vector2> cb)
    {
        For(p->{
            if (Get(p)>0) cb.Run(p);
        });
    }
    public boolean Valid(Vector2 pos)
    {
        return pos.x<width && pos.y < height;
    }
    public void Set(Vector2 pos,int vl){
        grid[(int)pos.x][(int)pos.y]=vl;
    }
    public void Set(int x,int y,int vl){
        grid[x][y]=vl;
    }
    public void Set(int index,int vl)
    {
        Set(index%width,index/width,vl);
    }
    public int Get(int x,int y){
        return grid[x][y];
    }
    public int Get(Vector2 pos){
        return grid[(int)pos.x][(int)pos.y];
    }
    public boolean HasValue(int x,int y)
    {
        return Get(x,y)>1;
    }
    public boolean HasValue(Vector2 pos)
    {
        return Get(pos)>1;
    }
    public boolean Empty(Vector2 pos)
    {
        return Get(pos)==1;
    }
    public String ToString()//width:height:data
    {
        return width+":"+height+":"+ToData();
    }
    private String ToData()
    {
        String st = "";
        for (int j=0;j<height;j++)
            for (int i=0;i<width;i++) st=st+Get(i,j);
        return st;
    }
    private void SetData(String data)
    {
        for (int i=0;i<data.length();i++)
            Set(i,data.charAt(i)-'0');
    }
    @Override
    public JsonValue ToJson(Object object0) {
        return new JsonValue(ToString());
    }

    @Override
    public Shape ToObject(JsonValue js) {
        String[] st = js.asString().split(":");
        width = Integer.parseInt(st[0]);
        height = Integer.parseInt(st[1]);
        grid = new int[width][height];
        SetData(st[2]);
        return this;
    }

    public Shape RotateRight()
    {
        int[][] newGrid = new int[height][width];
        For(p->{
            Vector2 pos = RotateRight(p);
            newGrid[(int)pos.x][(int)pos.y] = Get(p);
        });
        int temp = width;
        width = height;
        height = temp;
        grid = newGrid;
        return this;
    }
    private Vector2 RotateRight(Vector2 pos)
    {
        return new Vector2(height-1-pos.y,pos.x);
    }
}
