package Extend.Box2D;

import GDX11.IObject.IAction.IMulAction;
import GDX11.IObject.IActor.IActor;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IContact extends IMulAction {
    public enum Type{
        Begin,
        End,
        PrePos,
        Post,
        RayCast
    }
    public enum Sensor{
        None,
        Sensor,
        UnSensor
    }
    public Type type = Type.Begin;
    public Sensor sensor = Sensor.None;
    public String categoryB = "";
    public boolean runB;
    @Override
    public void Run() {
        switch (type){
            case Begin: GetIBody().AddEvent(new IBodyListener() {
                @Override
                public void OnBeginContact(IBody iBodyB, Fixture fixtureB, Contact contact) {
                    if (CheckSensor(fixtureB)) Run(GetIActor(),iBodyB);
                }
            });
                break;
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
    private boolean CheckSensor(Fixture fixtureB)
    {
        if (sensor==Sensor.None) return true;
        if (sensor==Sensor.Sensor && fixtureB.isSensor()) return true;
        if (sensor==Sensor.UnSensor && !fixtureB.isSensor()) return true;
        return false;
    }
    private void Run(IActor iActor,IBody iBodyB){
        if (categoryB.equals("") || iBodyB.category.equals(categoryB)){
            if (runB) iActor = iBodyB.GetIActor();
            RunActions(iActor);
        }
    }
    private void RunActions(IActor iActor){
        iMap.For(i->i.SetIActor(iActor));
        iActor.GetActor().addAction(super.Get());
    }
}
