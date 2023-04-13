package GDX11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static void Int(Vector2 v)
    {
        v.x = (int)v.x;
        v.y = (int)v.y;
    }
    //
    public static float[] GetVertices(List<Vector2> list)
    {
        float[] vert = new float[list.size()*2];
        ForIndex(list,i->{
            vert[i*2] = list.get(i).x;
            vert[i*2+1] = list.get(i).y;
        });
        return vert;
    }
    //for
    public static void Repeat(int count, GDX.Runnable cb)
    {
        For(1,count,i-> cb.Run());
    }
    public static <T> void For(Iterable<T> list, GDX.Runnable1<T> cb)
    {
        for (T i : list) cb.Run(i);
    }
    public static void ForIndex(Collection list, GDX.Runnable1<Integer> cb)
    {
        Util.For(0,list.size()-1, cb::Run);
    }
    public static <T> void For(int from,int to,GDX.Runnable1<Integer> cb)
    {
        for (int i=from;i<=to;i++) cb.Run(i);
    }
    public static void For(JsonValue json, GDX.Runnable1<JsonValue> cb)
    {
        for (JsonValue js : json)
            cb.Run(js);
    }
    //convert


    //readData
    public static String[][] ReadCSVByKey(String key)
    {
        return ReadCSV(GDX.GetStringByKey(key));
    }
    public static String[][] ReadCSV(String data)//[row][column]
    {
        Map<String,String> map0 = new HashMap<>();data = FindString(data,"\"\"","\"\"",map0);
        Map<String,String> map = new HashMap<>();
        data = FindString(data,"\"","\"",map);
        data = data.replace("\r","");
        String[] rows = data.split("\n");
        String[][] matrix = new String[rows.length][];
        for (int i=0;i<rows.length;i++)
        {
            matrix[i] = rows[i].split(",",-1);
            for (int j=0;j<matrix[i].length;j++)
            {
                if (map.containsKey(matrix[i][j])) matrix[i][j] = map.get(matrix[i][j]);
                for (String key : map0.keySet())
                    if (matrix[i][j].contains(key)) matrix[i][j] = matrix[i][j].replace(key,map0.get(key));
                if (matrix[i][j].startsWith("\"") && matrix[i][j].endsWith("\""))
                    matrix[i][j] = matrix[i][j].replace("\"","");
            }
        }
        return matrix;
    }
    private static String FindString(String str,String c1,String c2)
    {
        return GDX.Try(()->{
            int s = str.indexOf(c1);
            int e = str.indexOf(c2,s+1);
            if (s==-1 || e==-1) return null;
            return str.substring(s,e+c2.length());
        },()->null);
    }
    public static String FindString(String str,String c1,String c2,Map<String,String> map)
    {
        String s = FindString(str,c1,c2);
        while (s!=null)
        {
            String key = "$"+map.size();
            map.put(key,s);
            str = str.replace(s,key);
            s = FindString(str,c1,c2);
        }
        return str;
    }

    //FrameBuffer
    private static Texture GetFrameBuffer(Actor actor)
    {
        Batch batch = Scene.i.GetStage().getBatch();
        int width = (int)Scene.i.GetStage().getWidth();
        int height = (int)Scene.i.GetStage().getHeight();
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

        fbo.begin();
        batch.begin();
        actor.draw(batch,1);
        batch.end();
        fbo.end();

        return fbo.getColorBufferTexture();
    }
    public static TextureRegion GetTextureRegion(Actor actor)
    {
        TextureRegion tr = new TextureRegion(GetFrameBuffer(actor));
        tr.setRegion((int)actor.getX(),(int)actor.getY(),(int)actor.getWidth(),(int)actor.getHeight());
        tr.flip(false,true);
        return tr;
    }
    public static TextureRegion GetScreenshot()//real texture
    {
        Viewport viewport = Scene.i.GetStage().getViewport();
        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(
                viewport.getLeftGutterWidth(),
                viewport.getTopGutterHeight(),
                Gdx.graphics.getWidth() - viewport.getLeftGutterWidth() - viewport.getRightGutterWidth(),
                Gdx.graphics.getHeight() - viewport.getTopGutterHeight() - viewport.getBottomGutterHeight());

        final Pixmap flipPixmap = FlipPixmap(pixmap,false,true);
        final Pixmap potPixmap = new Pixmap(Scene.i.width, Scene.i.height,flipPixmap.getFormat());
        potPixmap.drawPixmap(flipPixmap,
                0, 0, flipPixmap.getWidth(), flipPixmap.getHeight(),
                0, 0, potPixmap.getWidth(), potPixmap.getHeight()
        );

        Texture texture = new Texture(potPixmap);

        pixmap.dispose();
        flipPixmap.dispose();
        potPixmap.dispose();

        return new TextureRegion(texture);
    }
    private static Pixmap FlipPixmap(Pixmap src,boolean flipX, boolean flipY) {
        final int width = src.getWidth();
        final int height = src.getHeight();
        Pixmap flipped = new Pixmap(width, height, src.getFormat());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (flipX)
                    flipped.drawPixel(x, y, src.getPixel(width - x - 1, y));
                if (flipY)
                    flipped.drawPixel(x, y, src.getPixel(x, height - y - 1));
            }
        }
        return flipped;
    }

}
