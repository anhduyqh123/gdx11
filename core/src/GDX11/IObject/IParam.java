package GDX11.IObject;

import GDX11.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.Map;

public class IParam extends IBase {
    public Map<String,String> dataMap = new HashMap<>();
    protected GDX.Func<Map> getParam;
    public Map<String,String> GetParam()
    {
        if (getParam==null)
        {
            Map map = new HashMap(dataMap);
            getParam = ()->map;
        }
        return getParam.Run();
    }
    public <T> T GetParam(String name,T value0)
    {
        String stValue = GetParam().get(name);
        return GDX.Try(()->{
            if (value0 instanceof Color) return (T)Color.valueOf(stValue);
            if (value0 instanceof Vector) return (T)GetVector(stValue);
            return (T)GetVariable(stValue);
        },()->value0);
    }
    public <T> void SetParam(String name,T value)
    {
        try {
            GetParam().put(name,value+"");
        }catch (Exception e){}
    }
    public boolean HasParam(String name)
    {
        return GetParam().containsKey(name);
    }
    //variable
    public Number GetValueFromString(String stValue)
    {
        return GetCalculate(stValue, this::GetVariable);
    }
    private Number GetVariable(String stValue)
    {
        if (stValue.contains("[")) return GetRandom(stValue);
        if (stValue.equals("scale")) return Scene.i.scale;
        if (stValue.equals("sw")) return (float) Scene.i.width;
        if (stValue.equals("sh")) return (float) Scene.i.height;
        if (stValue.contains("_")) return GetExtendVariable(stValue);
        return GetActorVariable(stValue,GetActor());
    }
    private Number GetActorVariable(String stValue,Actor actor)
    {
        if (HasParam(stValue)) return GetBaseValue(GetParam().get(stValue));
        if (stValue.equals("pSize")) return actor.getParent().getChildren().size;
        if (stValue.equals("index")) return actor.getZIndex();
        if (stValue.equals("w")) return actor.getWidth();
        if (stValue.equals("h")) return actor.getHeight();
        if (stValue.equals("pw")) return actor.getParent().getWidth();
        if (stValue.equals("ph")) return actor.getParent().getHeight();
        if (stValue.equals("xl")) return actor.getX(Align.left);
        if (stValue.equals("x")) return actor.getX(Align.center);
        if (stValue.equals("xr")) return actor.getX(Align.right);
        if (stValue.equals("yb")) return actor.getY(Align.bottom);
        if (stValue.equals("y")) return actor.getY(Align.center);
        if (stValue.equals("yt")) return actor.getY(Align.top);
        return GetBaseValue(stValue);
    }
    public Number GetBaseValue(String stValue)
    {
        if (stValue.contains(".")) return Float.valueOf(stValue);
        return Integer.valueOf(stValue);
    }
    private Number GetExtendVariable(String stValue)
    {
        String[] arr = stValue.split("_");
        if (Asset.i.GetNode(arr[0])!=null) return GetTextureParam(arr[0],arr[1]);
        return GetActorVariable(arr[1],GetIActor().IRootFind(arr[0]).GetActor());
    }
    private Number GetTextureParam(String name,String key)
    {
        if (key.equals("w")) return Asset.i.GetTexture(name).getRegionWidth();
        return Asset.i.GetTexture(name).getRegionHeight();
    }
    //random
    private Number GetRandom(String stValue)
    {
        stValue = stValue.replace("[","").replace("]","");
        String[] arr = stValue.split(",");
        Number v1 = GetVariable(arr[0]);
        Number v2 = GetVariable(arr[2]);
        if (arr[0].contains(".")) return MathUtils.random(v1.floatValue(),v2.floatValue());
        return MathUtils.random(v1.intValue(),v2.intValue());
    }
    //vector
    private static Vector GetVector(String value)//(1,2)
    {
        value = value.replace("(","").replace(")","");
        String[] arr = value.split(",");
        if (arr.length==2) return new Vector2(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]));
        if (arr.length==3) return new Vector3(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]),Float.parseFloat(arr[2]));
        return null;
    }
    //Calculate
    private static String UnGroup(String value, GDX.Func1<Number,String> onVar)//(a+b)+c
    {
        Map<String,String> map = new HashMap<>();
        String stEnd = Util.FindString(value,"(",")",map);
        for (String key : map.keySet())
        {
            String value0 = map.get(key).replace("(","").replace(")","");
            stEnd = stEnd.replace(key, GetCalculate(value0,onVar)+"");
        }
        return stEnd;
    }
    private static Number GetCalculate(String value, GDX.Func1<Number,String> onVar)//a+b+c;
    {
        if (value.contains("(")) value = UnGroup(value,onVar);
        if (value.charAt(0)=='[') return onVar.Run(value);
        if (value.charAt(0)=='-') value = "0"+value;
        if (value.contains("+")) return GetCalculate(value,"\\+",onVar);
        if (value.contains("-")) return GetCalculate(value,"\\-",onVar);
        if (value.contains("*")) return GetCalculate(value,"\\*",onVar);
        if (value.contains("/")) return GetCalculate(value,"\\/",onVar);
        return onVar.Run(value);
    }
    private static Number GetCalculate(String value, String sign,GDX.Func1<Number,String> onVar)
    {
        String[] arr = value.split(sign);
        Number vl = GetCalculate(arr[0],onVar);
        for (int i=1;i<arr.length;i++)
        {
            Number result = GetCalculate(arr[i],onVar);
            vl = JoinNumber(vl,result,sign);
        }
        return vl;
    }
    private static Number JoinNumber(Number n1,Number n2,String sign)
    {
        if (n1 instanceof Integer && n2 instanceof Integer) return IntJoin(n1.intValue(), n2.intValue(), sign);
        return FloatJoin(n1.floatValue(), n2.floatValue(), sign);
    }
    private static float FloatJoin(float n1,float n2,String sign)
    {
        if (sign.equals("\\+")) return n1+n2;
        if (sign.equals("\\-")) return n1-n2;
        if (sign.equals("\\*")) return n1*n2;
        if (sign.equals("\\/")) return n1/n2;
        return 0;
    }
    private static int IntJoin(int n1,int n2,String sign)
    {
        if (sign.equals("\\+")) return n1+n2;
        if (sign.equals("\\-")) return n1-n2;
        if (sign.equals("\\*")) return n1*n2;
        if (sign.equals("\\/")) return n1/n2;
        return 0;
    }

    //Align
    public static int GetAlign(String align)
    {
        if (align.equals("")) return Align.bottomLeft;
        if (align.equals("bottomLeft")) return Align.bottomLeft;
        if (align.equals("bottom")) return Align.bottom;
        if (align.equals("bottomRight")) return Align.bottomRight;
        if (align.equals("left")) return Align.left;
        if (align.equals("center")) return Align.center;
        if (align.equals("right")) return Align.right;
        if (align.equals("topLeft")) return Align.topLeft;
        if (align.equals("top")) return Align.top;
        if (align.equals("topRight")) return Align.topRight;
        return Align.bottomLeft;
    }
}
