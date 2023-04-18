package JigsawWood.Model;

import GDX11.Util;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Board extends Shape {
    public Board(){}
    public Board(int width,int height) {
        this.width = width;
        this.height = height;
        grid = new boolean[width][height];
    }
    public boolean IsFit(Vector2 pos,Shape shape) {
        return shape.ForIf(p->{
            Vector2 bPos = p.add(pos);
            if (!Valid(bPos)) return false;
            return !Get(bPos);
        });
    }
    public void Set(Vector2 pos,Shape shape) {
        shape.For(p->{
            boolean vl = shape.Get(p);
            Set(p.add(pos),vl);
        });
    }
    public void Destroy(List<Vector2> list){
        Util.For(list,p->Set(p,false));
    }
    public List<List<Vector2>> GetDestroyList() {
        List<List<Vector2>> list = new ArrayList<>();
        Util.For(0,width-1,col->{
            List<Vector2> full = GetFullColum(col);
            if (full!=null) list.add(full);
        });
        Util.For(0,height-1,row->{
            List<Vector2> full = GetFullRow(row);
            if (full!=null) list.add(full);
        });
        return list;
    }
    private List<Vector2> GetFullColum(int column) {
        List<Vector2> list = new ArrayList<>();
        for (int j=0;j<height;j++) {
            if (!Get(column,j)) return null;
            list.add(new Vector2(column,j));
        }
        return list;
    }
    private List<Vector2> GetFullRow(int row) {
        List<Vector2> list = new ArrayList<>();
        for (int i=0;i<width;i++) {
            if (!Get(i,row)) return null;
            list.add(new Vector2(i,row));
        }
        return list;
    }
}
