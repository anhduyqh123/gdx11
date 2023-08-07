package Extend.Box2D;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import com.badlogic.gdx.math.Vector2;

public class IBox2D extends IActor {
    public Vector2 gravity = new Vector2(0,-10);
    public String category = "player,object";
    @Override
    public void Connect() {
        super.Connect();
        SetActor(new GBox2D());
    }

    @Override
    public void RefreshContent() {
        GBox2D box2D = GetActor();
        box2D.world.setGravity(gravity);
        box2D.SetCategory(category);
    }
}
