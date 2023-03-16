package GDX11;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Collection;
import java.util.HashMap;
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
    public static void Round(Vector2 v)
    {
        v.x = Math.round(v.x);
        v.y = Math.round(v.y);
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
    public static String[][] ReadCSVFromNode(String name)
    {
        return ReadCSV(GDX.GetStringFromNode(name));
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
}
