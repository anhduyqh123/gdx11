package GDX11.IObject.IAction;

import GDX11.IObject.IObject;
import GDX11.Util;
import com.badlogic.gdx.scenes.scene2d.Action;

import java.util.HashMap;
import java.util.Map;

public abstract class IAction extends IObject {
    public IAction(){}
    public IAction(String name){
        super(name);
    }

    public abstract void Run();
    public abstract Action Get();

    protected float GetFloatValue(String stValue)
    {
        return GetIActor().GetGlobalNum(stValue).floatValue();
    }
    protected String GetRealString(String stValue){
        stValue = SetVarToString(stValue);
        String stRandom = Util.FindString(stValue,"[","]");
        if (stRandom==null) return stValue;
        return stValue.replace(stRandom,GetIActor().GetGlobalNum(stRandom)+"");
    }
    private String SetVarToString(String stValue){
        Map<String,String> map = new HashMap<>();
        stValue = Util.FindString(stValue,"{","}",map);
        for (String key : map.keySet()){
            String vl = map.get(key).replace("{","").replace("}","");
            stValue = stValue.replace(key,GetIActor().iParam.Get(vl)+"");
        }
        return stValue;
    }
}
