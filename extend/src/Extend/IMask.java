package Extend;

import GDX11.IObject.IComponent.IComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class IMask extends IComponent {
    private List<Actor> maskedActors = new ArrayList<>();
    private List<Actor> actors = new ArrayList<>();
    @Override
    public void Refresh() {

    }
}
