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
        Map<Character, Integer> minX = new HashMap<>();
        Map<Character,Integer> maxX = new HashMap<>();
        Map<Character, Integer> minY = new HashMap<>();
        Map<Character,Integer> maxY = new HashMap<>();
        Map<Character,List<Vector2>> listPos = new HashMap<>();
        ForBlock(p->{
            char id = Get(p);
            if (id<=emptyChar) return;
            if (!listPos.containsKey(id)) listPos.put(id,new ArrayList<>());
            listPos.get(id).add(p);
            PutMin(minX,id,p.x);
            PutMin(minY,id,p.y);
            PutMax(maxX,id,p.x);
            PutMax(maxY,id,p.y);
        });
        for (char id : listPos.keySet())
        {
            if (id<varChar) continue;
            pieces.add(NewPiece(id,listPos.get(id),minX.get(id),maxX.get(id),minY.get(id),maxY.get(id)));
        }
        for (char id : listPos.keySet())
            if (id>=varChar) Util.For(listPos.get(id),p->Set(p,emptyChar));
    }
    private void PutMin(Map<Character, Integer> map,char id,float value)
    {
        if (!map.containsKey(id)) map.put(id,(int)value);
        map.put(id,Math.min(map.get(id),(int)value));
    }
    private void PutMax(Map<Character, Integer> map,char id,float value)
    {
        if (!map.containsKey(id)) map.put(id,(int)value);
        map.put(id,Math.max(map.get(id),(int)value));
    }
    private Shape NewPiece(char id,List<Vector2> listPos,int x0,int x1,int y0,int y1)
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
