package JigsawWood.Model;

import GDX11.GDX;
import GDX11.Json;
import GDX11.Util;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Shape implements Json.JsonObject {
    public int width,height;
    protected boolean[][] grid;
    public Shape(){}
    public Shape(int width,int height)
    {
        this.width = width;
        this.height = height;
        grid = new boolean[width][height];
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
            if (Get(p)) cb.Run(p);
        });
    }
    public boolean Valid(Vector2 pos)
    {
        return pos.x<width && pos.y < height;
    }
    public void Set(Vector2 pos,boolean vl){
        grid[(int)pos.x][(int)pos.y]=vl;
    }
    public void Set(int x,int y,boolean vl){
        grid[x][y]=vl;
    }
    public void Set(int index,boolean vl)
    {
        Set(index%width,index/width,vl);
    }
    public boolean Get(int x,int y){
        return grid[x][y];
    }
    public boolean Get(Vector2 pos){
        return grid[(int)pos.x][(int)pos.y];
    }
    public String ToString()//width:height:data
    {
        return width+":"+height+":"+ToData();
    }
    private String ToData()
    {
        String st = "";
        for (int j=0;j<height;j++)
            for (int i=0;i<width;i++) st=st+(Get(i,j)?"1":"0");
        return st;
    }
    private void SetData(String data)
    {
        for (int i=0;i<data.length();i++)
            Set(i,data.charAt(i)=='1');
    }
    @Override
    public JsonValue ToJson(Object object0) {
        return new JsonValue(ToString());
    }

    @Override
    public Object ToObject(JsonValue js) {
        String[] st = js.asString().split(":");
        width = Integer.parseInt(st[0]);
        height = Integer.parseInt(st[1]);
        grid = new boolean[width][height];
        SetData(st[2]);
        return this;
    }

    public Shape RotateRight()
    {
        boolean[][] newGrid = new boolean[height][width];
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

    public static Shape NewRandomShape(){
        int id = MathUtils.random(0,3);
        if (id==0) return NewShape0();
        if (id==1) return NewShape1();
        if (id==2) return NewShape1().RotateRight();
        return NewShape2();
    }
    public static Shape NewShape0()
    {
        Shape shape = new Shape(2,2);
        shape.grid = new boolean[][]{{true, true},
                                    {true, true}};
        return shape;
    }
    public static Shape NewShape1()
    {
        Shape shape = new Shape(4,1);
        Util.For(0,shape.width-1,i->shape.Set(i,0,true));
        return shape;
    }
    public static Shape NewShape2()
    {
        Shape shape = new Shape(3,2);
        Util.For(0,shape.width-1,i->shape.Set(i,0,true));
        shape.Set(0,1,true);
        return shape;
    }
}
