package JigsawWood.Model;

import GDX11.GDX;
import GDX11.Json;
import GDX11.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Shape implements Json.JsonObject {
    //* is null
    //0 is empty
    //X is wall
    //a,b,c,d is value
    protected final char nullChar = '*';
    protected final char emptyChar = '0';
    protected final char valueChar = 'A';


    public int width=4,height=4,x,y;
    protected char[][] grid = new char[width][height];
    public String texture = "";
    public Shape(){
        Create();
    }
    public Shape(int width,int height) {
        this.width = width;
        this.height = height;
        Create();
    }
    public Shape(Shape shape) {
        width = shape.width;
        height = shape.height;
        Create();
        shape.For(p->Set(p,shape.Get(p)));
        this.texture = shape.texture;;
    }
    public void Create() {
        grid = new char[width][height];
        For(p->Set(p,emptyChar));
    }
    public boolean ForIf(GDX.Func1<Boolean,Vector2> cb) {//cb true ->return
        for (int i=0;i<width;i++)
            for (int j=0;j<height;j++)
                if (!cb.Run(new Vector2(i,j))) return false;
        return true;
    }
    public void For(GDX.Runnable1<Vector2> cb) {
        ForIf(p->{
            cb.Run(p);
            return true;
        });
    }
    public void ForValue(GDX.Runnable1<Vector2> cb)
    {
        For(p->{
            if (Get(p)>=valueChar) cb.Run(p);
        });
    }
    public void ForBlock(GDX.Runnable1<Vector2> cb) {
        For(p->{
            if (Get(p)>=emptyChar) cb.Run(p);
        });
    }
    public boolean Valid(Vector2 pos)
    {
        return pos.x<width && pos.y < height;
    }
    public void SetPos(Vector2 pos)
    {
        x = (int)pos.x;
        y = (int)pos.y;
    }
    public void Set(Vector2 pos,char vl){
        grid[(int)pos.x][(int)pos.y]=vl;
    }
    public void Set(int x,int y,char vl){
        grid[x][y]=vl;
    }
    public void Set(int index,char vl)
    {
        Set(index%width,index/width,vl);
    }
    public void Set(Shape shape) {
        shape.ForValue(p->{
            char vl = shape.Get(p);
            Set(p.add(shape.x,shape.y),vl);
        });
    }
    public void Remove(Shape shape)
    {
        shape.ForValue(p->{
            Vector2 pos = p.add(shape.x,shape.y);
            if (Null(pos)) return;
            Set(pos,emptyChar);
        });
    }
    public void Remove(char id)
    {
        For(p->{
            if (Get(p)==id) Set(p,emptyChar);
        });
    }
    public Vector2 GetPos()
    {
        return new Vector2(x,y);
    }
    public char Get(int x,int y){
        return grid[x][y];
    }
    public char Get(Vector2 pos){
        return grid[(int)pos.x][(int)pos.y];
    }
    public boolean HasValue(int x,int y)
    {
        return HasValue(new Vector2(x,y));
    }
    public boolean HasValue(Vector2 pos)
    {
        return Get(pos)>=valueChar;
    }
    public boolean Empty(Vector2 pos)
    {
        return Get(pos)==emptyChar;
    }
    public boolean Null(Vector2 pos){
        return Get(pos)==nullChar;
    }
    public String ToString() {
        return width+":"+height+":"+ToData()+":"+texture;
    }
    private String ToData() {
        String st = "";
        for (int j=0;j<height;j++)
            for (int i=0;i<width;i++) st=st+Get(i,j)+",";
        return st.substring(0,st.length()-1);
    }
    private void SetData(String data) {
        String[] arr = data.split(",");
        for (int i=0;i<arr.length;i++)
            Set(i,arr[i].charAt(0));
    }
    @Override
    public JsonValue ToJson(Object object0) {
        return new JsonValue(ToString());
    }

    @Override
    public Shape ToObject(JsonValue js) {
        String[] st = js.asString().split(":",4);
        width = Integer.parseInt(st[0]);
        height = Integer.parseInt(st[1]);
        grid = new char[width][height];
        SetData(st[2]);
        texture = st[3];
        return this;
    }
    public List<String> GetShapeIDs() {
        HashSet<String> set = new HashSet<>();
        For(p->{
            if (HasValue(p)) set.add(Get(p)+"");
        });
        return new ArrayList<>(set);
    }
    public boolean IsFit(Vector2 pos,Shape shape) {
        return shape.ForIf(p->{
            Vector2 bPos = new Vector2(p).add(pos);
            if (!Valid(bPos)) return false;
            return Empty(bPos) || !shape.HasValue(p);
        });
    }
    public List<List<Vector2>> GetDestroyList(){
        return new ArrayList<>();
    }
    public void Destroy(List<Vector2> list){
        Util.For(list, p->Set(p,emptyChar));
    }
}
