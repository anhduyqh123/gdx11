package GDX11.IObject;

import GDX11.GDX;
import GDX11.Util;
import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;
import java.util.Map;

public class ICalculate {
    private GDX.Func1<Number,String> getVar;
    public ICalculate(GDX.Func1<Number,String> getVar)
    {
        this.getVar = getVar;
    }
    public Number Get(String stValue)
    {
        stValue = ReplaceValue(stValue,"(",")",vl->{
            vl = vl.replace("(","").replace(")","");
            return Get(vl)+"";
        });
        stValue = ReplaceValue(stValue,"[","]",vl-> GetRandom(vl)+"");
        //if (stValue.charAt(0)=='[') return GetRandom(stValue);
        if (stValue.charAt(0)=='-') stValue = "0"+stValue;
        if (stValue.contains("+")) return Get(stValue,"\\+");
        if (stValue.contains("-")) return Get(stValue,"\\-");
        if (stValue.contains("*")) return Get(stValue,"\\*");
        if (stValue.contains("/")) return Get(stValue,"\\/");
        if (stValue.contains("%")) return Get(stValue,"%");
        return getVar.Run(stValue);
    }
    private String ReplaceValue(String stValue, String c1, String c2, GDX.Func1<String,String> cb){
        if (!stValue.contains(c1)) return stValue;
        Map<String,String> map = new HashMap<>();
        stValue = Util.FindString(stValue,c1,c2,map);
        for (String key : map.keySet())
            stValue = stValue.replace(key,cb.Run(map.get(key)));
        return stValue;
    }
    private Number Get(String stValue,String sign)
    {
        String[] arr = stValue.split(sign);
        Number vl = Get(arr[0]);
        for (int i=1;i<arr.length;i++)
        {
            Number result = Get(arr[i]);
            vl = JoinNumber(vl,result,sign);
        }
        return vl;
    }
    private Number JoinNumber(Number n1,Number n2,String sign)
    {
        if (n1 instanceof Integer && n2 instanceof Integer) return IntJoin(n1.intValue(), n2.intValue(), sign);
        return FloatJoin(n1.floatValue(), n2.floatValue(), sign);
    }
    private float FloatJoin(float n1,float n2,String sign)
    {
        if (sign.equals("\\+")) return n1+n2;
        if (sign.equals("\\-")) return n1-n2;
        if (sign.equals("\\*")) return n1*n2;
        if (sign.equals("\\/")) return n1/n2;
        if (sign.equals("%")) return n1%n2;
        return 0;
    }
    private int IntJoin(int n1,int n2,String sign)
    {
        if (sign.equals("\\+")) return n1+n2;
        if (sign.equals("\\-")) return n1-n2;
        if (sign.equals("\\*")) return n1*n2;
        if (sign.equals("\\/")) return n1/n2;
        if (sign.equals("%")) return n1%n2;
        return 0;
    }
    //random
    private Number GetRandom(String stValue)
    {
        stValue = stValue.replace("[","").replace("]","");
        String[] arr = stValue.split(",");
        Number v1 = Get(arr[0]);
        Number v2 = Get(arr[1]);
        if (arr[0].contains(".")) return MathUtils.random(v1.floatValue(),v2.floatValue());
        return MathUtils.random(v1.intValue(),v2.intValue());
    }
}
