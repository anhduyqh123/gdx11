package JigsawWood.Model;

import GDX11.Util;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JigsawBoard extends Shape{
    public List<Shape> pieces = new ArrayList<>();

    public JigsawBoard() {
        super();
    }

    public JigsawBoard(Shape shape) {
        super(shape);
    }

    public void CutShapes()
    {
        Map<Integer, Integer> minX = new HashMap<>();
        Map<Integer,Integer> maxX = new HashMap<>();
        Map<Integer, Integer> minY = new HashMap<>();
        Map<Integer,Integer> maxY = new HashMap<>();
        Map<Integer,List<Vector2>> listPos = new HashMap<>();
        ForBlock(p->{
            int id = Get(p);
            if (id<=0) return;
            if (!listPos.containsKey(id)) listPos.put(id,new ArrayList<>());
            listPos.get(id).add(p);
            PutMin(minX,id,p.x);
            PutMin(minY,id,p.y);
            PutMax(maxX,id,p.x);
            PutMax(maxY,id,p.y);
        });
        for (int id : listPos.keySet())
            pieces.add(NewPiece(id,listPos.get(id),minX.get(id),maxX.get(id),minY.get(id),maxY.get(id)));
        ForBlock(p->Set(p,0));
    }
    private void PutMin(Map<Integer, Integer> map,int id,float value)
    {
        if (!map.containsKey(id)) map.put(id,(int)value);
        map.put(id,Math.min(map.get(id),(int)value));
    }
    private void PutMax(Map<Integer, Integer> map,int id,float value)
    {
        if (!map.containsKey(id)) map.put(id,(int)value);
        map.put(id,Math.max(map.get(id),(int)value));
    }
    private Shape NewPiece(int id,List<Vector2> listPos,int x0,int x1,int y0,int y1)
    {
        Shape piece = new Shape(x1-x0+1,y1-y0+1);
        piece.texture = texture;
        piece.x = x0;
        piece.y = y0;
        Util.For(listPos,p->{
            Vector2 pos = new Vector2(p).sub(x0,y0);
            piece.Set(pos,id);
        });
        return piece;
    }
}
