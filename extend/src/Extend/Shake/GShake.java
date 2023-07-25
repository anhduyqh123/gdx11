package Extend.Shake;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class GShake extends Action {
    private int curOffX,curOffY,offX=3,offY=3;
    private float duration,time;

    public static GShake Get(float duration) {
        GShake action = Actions.action(GShake.class);
        action.duration = duration;
        return action;
    }
    public static GShake Get(int offsetX,int offsetY,float duration) {
        GShake action = Get(duration);
        action.offX = offsetX;
        action.offY = offsetY;
        return action;
    }
    public static GShake Get(int offset,float duration) {
        return Get(offset,offset,duration);
    }

    public boolean act(float delta) {
        if(this.time >= this.duration) {
            this.translateLayer(-this.curOffX, -this.curOffY);
            return true;
        } else {
            int oX = MathUtils.random(-offX, offX);
            int oY = MathUtils.random(-offY, offY);
            this.translateLayer(oX - this.curOffX, oY - this.curOffY);
            this.curOffX = oX;
            this.curOffY = oY;
            this.time += delta;
            return false;
        }
    }

    @Override
    public void restart() {
        this.curOffY = 0;
        this.curOffX = 0;
        this.time = 0;
    }

    public void translateLayer(int offX, int offY) {
        this.getActor().moveBy((float)offX, (float)offY);
    }
}
