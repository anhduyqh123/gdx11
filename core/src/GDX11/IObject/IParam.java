package GDX11.IObject;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Reflect;
import GDX11.Scene;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.Map;

public class IParam {
    public Map<String,String> dataMap = new HashMap<>();
    protected GDX.Func<Map> getParam;
    protected GDX.Func<IActor> getIActor;

    public void SetIActor(IActor iActor)
    {
        getIActor = ()->iActor;
    }
    public IActor GetIActor()
    {
        return getIActor.Run();
    }
    public Actor GetActor()
    {
        return GetIActor().GetActor();
    }
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
            return GetVariable(stValue,Reflect.ToBaseType(stValue,value0));
        },()->value0);
    }
    public <T> void SetParam(String name,T value)
    {
        try {
            GetParam().put(name,value+"");
        }catch (Exception e){}
    }
    //variable
    public Float GetValueFromString(String stValue)
    {
        return GetCalculate(stValue,st->Float.parseFloat(GetVariable(st,0f)+""));
    }
    private  <T> T GetVariable(String stValue,T value0)
    {
        if (stValue.contains("[")) return GetRandom(stValue,value0);
        if (stValue.equals("sw")) return (T)Float.valueOf(Scene.i.width);
        if (stValue.equals("sh")) return (T)Float.valueOf(Scene.i.height);
        if (stValue.contains("_")) return GetExtendVariable(stValue,value0);
        return GetActorVariable(stValue,value0,GetActor());
    }
    private  <T> T GetActorVariable(String stValue,T value0,Actor actor)
    {
        if (stValue.equals("index")) return (T)Integer.valueOf(actor.getZIndex());
        if (stValue.equals("w")) return (T)Float.valueOf(actor.getWidth());
        if (stValue.equals("h")) return (T)Float.valueOf(actor.getHeight());
        if (stValue.equals("pw")) return (T)Float.valueOf(actor.getParent().getWidth());
        if (stValue.equals("ph")) return (T)Float.valueOf(actor.getParent().getHeight());
        if (stValue.equals("xl")) return (T)Float.valueOf(actor.getX(Align.left));
        if (stValue.equals("x")) return (T)Float.valueOf(actor.getX(Align.center));
        if (stValue.equals("xr")) return (T)Float.valueOf(actor.getX(Align.right));
        if (stValue.equals("yb")) return (T)Float.valueOf(actor.getY(Align.bottom));
        if (stValue.equals("y")) return (T)Float.valueOf(actor.getY(Align.center));
        if (stValue.equals("yt")) return (T)Float.valueOf(actor.getY(Align.top));
        return Reflect.ToBaseType(stValue,value0);
    }
    private <T> T GetExtendVariable(String stValue,T value0)
    {
        String[] arr = stValue.split("_");
        if (Asset.i.GetNode(arr[0])!=null) return GetTextureParam(arr[0],arr[1]);
        return GetActorVariable(arr[1],value0,GetIActor().IRootFind(arr[0]).GetActor());
    }
    private <T> T GetTextureParam(String name,String key)
    {
        if (key.equals("w")) return (T)Float.valueOf(Asset.i.GetTexture(name).getRegionWidth());
        return (T)Float.valueOf(Asset.i.GetTexture(name).getRegionHeight());
    }
    //random
    private <T> T GetRandom(String stValue,T value0)
    {
        stValue = stValue.replace("[","").replace("]","");
        String[] arr = stValue.split(",");
        Float v1 = Float.valueOf(GetVariable(arr[0],value0)+"");
        Float v2= Float.valueOf(GetVariable(arr[1],value0)+"");
        if (arr[0].contains(".")) return (T)Float.valueOf(MathUtils.random(v1,v2));
        return (T)Integer.valueOf(MathUtils.random(v1.intValue(),v2.intValue()));
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
    private static Float GetCalculate(String value, GDX.Func1<Float,String> onVar)//a+b+c;
    {
        if (value.charAt(0)=='-') value = "0"+value;
        if (value.contains("+")) return GetCalculate(value,"\\+",onVar);
        if (value.contains("-")) return GetCalculate(value,"\\-",onVar);
        if (value.contains("*")) return GetCalculate(value,"\\*",onVar);
        if (value.contains("/")) return GetCalculate(value,"\\/",onVar);
        return onVar.Run(value);
    }
    private static float GetCalculate(String value, String sign,GDX.Func1<Float,String> onVar)
    {
        String[] arr = value.split(sign);
        float vl = Float.parseFloat(GetCalculate(arr[0],onVar)+"");
        for (int i=1;i<arr.length;i++)
        {
            float result = Float.parseFloat(GetCalculate(arr[i],onVar)+"");
            if (sign.equals("\\+")) vl=vl+ result;
            if (sign.equals("\\-")) vl=vl- result;
            if (sign.equals("\\*")) vl=vl* result;
            if (sign.equals("\\/")) vl=vl/ result;
        }
        return vl;
    }

    //Align
    public static int GetAlign(String align)
    {
        if (align.equals("")) return Align.bottomLeft;
        if (align.equals("bl")) return Align.bottomLeft;
        if (align.equals("b")) return Align.bottom;
        if (align.equals("br")) return Align.bottomRight;
        if (align.equals("l")) return Align.left;
        if (align.equals("c")) return Align.center;
        if (align.equals("r")) return Align.right;
        if (align.equals("tl")) return Align.topLeft;
        if (align.equals("t")) return Align.top;
        if (align.equals("tr")) return Align.topRight;
        return Align.bottomLeft;
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
