package GDX11.IObject;

import GDX11.GDX;
import GDX11.Util;

import java.util.HashMap;
import java.util.Map;

public class ICalculate {
    private GDX.Func1<Number,String> getVar;
    private Map<String,String> map = new HashMap<>();

    public ICalculate(GDX.Func1<Number,String> getVar)
    {
        this.getVar = getVar;
    }
    private String GetGroup(String key)
    {
        //remove ()
        return map.get(key).replace("(","").replace(")","");
    }
    public Number Get(String stValue)
    {
        if (stValue.contains("(")) stValue = Util.FindString(stValue,"(",")",map);;
        if (stValue.charAt(0)=='-') stValue = "0"+stValue;
        if (stValue.contains("+")) return Get(stValue,"\\+");
        if (stValue.contains("-")) return Get(stValue,"\\-");
        if (stValue.contains("*")) return Get(stValue,"\\*");
        if (stValue.contains("/")) return Get(stValue,"\\/");
        if (map.containsKey(stValue)) return Get(GetGroup(stValue));
        return getVar.Run(stValue);
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
        return 0;
    }
    private int IntJoin(int n1,int n2,String sign)
    {
        if (sign.equals("\\+")) return n1+n2;
        if (sign.equals("\\-")) return n1-n2;
        if (sign.equals("\\*")) return n1*n2;
        if (sign.equals("\\/")) return n1/n2;
        return 0;
    }
}
