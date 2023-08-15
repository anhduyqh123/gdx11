package DrinkGame.Base;

import Extend.Box2D.IBody;
import GDX11.GAudio;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class VControl {
    private IGroup iGroup;
    private boolean xxx;
    public Runnable onStart = ()->{},onWaitingForEnd=()->{};
    private float minDegree,maxDegree;
    public VControl(IGroup iGroup){
        this.iGroup = iGroup;
        iGroup.FindActor("glow").addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer!=0) return false;
                xxx = true;
                return true;
            }
        });

        float maxLen = 200f;
        Vector2 pos0 = new Vector2();
        Vector2 dir = new Vector2();
        iGroup.GetActor().addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!xxx || pointer!=0) return false;
                pos0.set(x,y);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Vector2 dirx = new Vector2(pos0).sub(x,y);
                if (ValidDegree(dirx)) dir.set(dirx);
                float percent = dir.len()/maxLen;
                if (percent>1) percent = 1;
                SetDirection(percent,dir);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                iGroup.Run("off");
                float percent = dir.len()/maxLen;
                if (percent>1) percent = 1;
                if (percent<0.3f){
                    iGroup.RunAction("reset");
                    return;
                }
                //push success
                iGroup.GetActor().setTouchable(Touchable.disabled);
                GAudio.i.PlaySound("quick1");
                if (dir.len()>maxLen) dir.setLength(maxLen);
                IBody iBody = iGroup.iComponents.GetIComponent("body");
                iBody.body.setActive(true);
                iBody.body.applyForceToCenter(dir.scl(20),true);
                onStart.run();
                iGroup.Run(onWaitingForEnd,1f);
            }
        });
    }
    private void SetDirection(float percent, Vector2 dir){
        iGroup.FindActor("glow").setVisible(percent<0.3f);
        iGroup.FindActor("circle").setScale(percent);
        iGroup.FindActor("arrow").setScale(percent);
        iGroup.FindIActor("arrow").SetStageRotation(dir.angleDeg()-90);
    }
    public void SetLimitDegree(int min,int max){
        minDegree = min;
        maxDegree = max;
    }
    private boolean ValidDegree(Vector2 dir){
        if (minDegree*maxDegree==0) return true;
        float value = dir.angleDeg()-90;
        return value>=minDegree&&value<=maxDegree;
    }
}
