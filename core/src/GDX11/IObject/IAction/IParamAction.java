package GDX11.IObject.IAction;

import GDX11.Config;
import GDX11.IObject.IParam;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IParamAction extends IAction{
    public enum Type
    {
        None,//local param and global param
        Global,
        Pref
    }
    public enum Kind{
        Set, //set stValue to param
        Switch,// stValue is value default
        Equal, // if param equal stValue Run(name_true) else Run(name_false)
        Run,//Runnable in param
    }
    public Type type = Type.None;
    public Kind kind = Kind.Set;
    public String param = "";
    public String stValue = "";

    public IParamAction() {
        super("param");
    }

    @Override
    public void Run() {
        switch (type)
        {
            case None:
                IParam iParam = GetIActor().iParam;
                if (kind==Kind.Set) iParam.Set(param,stValue);
                if (kind==Kind.Switch) iParam.Set(param,Switch());
                if (kind==Kind.Equal) Equal(iParam.Get(param));
                if (kind==Kind.Run) iParam.Run(param);
                break;
            case Global:
                if (kind==Kind.Set) Config.i.Set(param,stValue);
                if (kind==Kind.Switch) Config.i.Set(param,Switch());
                if (kind==Kind.Equal) Equal(Config.i.Get(param));
                if (kind==Kind.Run) Config.i.Run(param);
                break;
            case Pref:
                if (kind==Kind.Set) Config.SetPref(param,stValue);
                if (kind==Kind.Switch) Config.SetPref(param,Switch());
                if (kind==Kind.Equal) Equal(Config.GetPref(param));
                break;
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
    private Object Switch()
    {
        return !GetBoolValue();
    }
    private boolean GetBoolValue()
    {
        if (type==Type.None) return GetIActor().iParam.Get(param,Boolean.parseBoolean(stValue));
        if (type==Type.Pref) return Config.GetPref(param,Boolean.parseBoolean(stValue));
        return Config.i.Get(param,Boolean.parseBoolean(stValue));
    }
    private void Equal(Object ob1)
    {
        String st = ob1+"";
        GetIActor().RunAction(name+"_"+st.equals(stValue));
    }
}
