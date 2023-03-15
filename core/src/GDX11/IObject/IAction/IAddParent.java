package GDX11.IObject.IAction;

import GDX11.Scene;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IAddParent extends IAction {
    public boolean keepTransform;
    public String parent = "";

    @Override
    public void Run() {
        if (keepTransform) Scene.AddActorKeepTransform(GetActor(),GetParent());
        else GetParent().addActor(GetActor());
    }
    private Group GetParent()
    {
        if (parent.equals("")) return GetIActor().GetIParent().GetActor();
        return GetIActor().IRootFind(parent).GetActor();
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
