package Extend.Box2D;

import GDX11.GDX;
import GDX11.IObject.IAction.IAction;
import GDX11.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.HashMap;
import java.util.Map;

public class IBox2DProperty extends IAction {
    public enum Type{
        Active,
        UnActive,
        AddForceToCenter,
        SetVelocity,
        SetAngular,
        GravityScale
    }
    public Type type = Type.Active;
    public String value = "";//10,(1,2);
    @Override
    public void Run() {
        switch (type){
            case Active:GetBody().setActive(true);
                break;
            case UnActive:GetBody().setActive(false);
                break;
            case AddForceToCenter:GetBody().applyForceToCenter(GetVecValue(),true);
                break;
            case SetVelocity:GetBody().setLinearVelocity(GetVecValue());
                break;
            case SetAngular:GetBody().setAngularVelocity(GetFloatValue(value));
                break;
            case GravityScale:GetBody().setGravityScale(GetFloatValue(value));
                break;
            default:
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }

    private IBody GetIBody(){
        return GetIActor().iComponents.GetIComponent("body");
    }
    private Body GetBody(){
        return GetIBody().body;
    }

    private Vector2 GetVecValue(){
        Map<String,String> map = new HashMap<>();
        String str0 = Util.FindString(value,"[","]",map );//random
        String[] arr = str0.replace("(","").replace(")","").split(",");
        Util.For(0,arr.length-1,i->{
            for (String key : map.keySet())
                if (arr[i].contains(key)) arr[i] = arr[i].replace(key,map.get(key));
        });
        return new Vector2(GetFloatValue(arr[0]),GetFloatValue(arr[1]));
    }
}
