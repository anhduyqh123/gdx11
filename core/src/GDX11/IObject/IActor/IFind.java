package GDX11.IObject.IActor;

import com.badlogic.gdx.scenes.scene2d.Actor;

//Interface IFind
public interface IFind {
    <T extends IActor> T FindIActor(String name);
    default <T extends IActor> T FindIActor(String name, Class<T> type)
    {
        return FindIActor(name);
    }
    default <T extends Actor> T FindActor(String name)
    {
        return FindIActor(name).GetActor();
    }

    default IGroup FindIGroup(String name)
    {
        return FindIActor(name);
    }
    default IImage FindIImage(String name)
    {
        return FindIActor(name);
    }
    default ILabel FindILabel(String name)
    {
        return FindIActor(name);
    }
    default ITable FindITable(String name)
    {
        return FindIActor(name);
    }
}
