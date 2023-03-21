package Tool.ObjectTool.Point;

import GDX11.IObject.IActor.IActor;
import GDX11.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class IPoints {
    private IActor iActor;
    private EventListener event = NewEvent();

    public IPoints()
    {

    }
    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
        Scene.i.ui.clearChildren();
        Scene.i.ui.addListener(event);
    }
    protected EventListener NewEvent()
    {
        return new ClickListener(){

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (IPoint.selected==null) return;
                IPoint.selected.SetPos(new Vector2(x,y));
            }
        };
    }
    public void Close()
    {
        Scene.i.ui.removeListener(event);
    }
}
