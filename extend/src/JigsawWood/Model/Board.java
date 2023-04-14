package JigsawWood.Model;

import com.badlogic.gdx.math.Vector2;

public class Board extends Shape {
    public Board(){}
    public Board(int width,int height)
    {
        this.width = width;
        this.height = height;
        grid = new boolean[width][height];
    }
    public boolean IsFit(Vector2 pos,Shape shape)
    {
        return shape.ForIf((p,b)->!Get(p.add(pos)));
    }
    public void Set(Vector2 pos,Shape shape)
    {
        shape.For((p,b)-> Set(p.add(pos),b));
    }
}
