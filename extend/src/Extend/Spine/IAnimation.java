package Extend.Spine;

import GDX11.IObject.IAction.IAction;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.esotericsoftware.spine.AnimationState;

public class IAnimation extends IAction {
    public String aniName = "";
    public boolean loop;
    public float timeScale = 1;
    public float alpha = 1;
    public float mixDuration = 0.2f;

    @Override
    public void Run() {
        new AniAction().Start();
    }

    @Override
    public Action Get() {
        return new AniAction();
    }
    public class AniAction extends Action
    {
        private AnimationState.TrackEntry entry;
        private boolean start;
        public void Start()
        {
            GSpine gSpine = GetActor();
            entry = gSpine.SetAnimation(aniName,loop);
            entry.setLoop(loop);
            entry.setTimeScale(timeScale);
            entry.setAlpha(alpha);
            entry.setMixDuration(mixDuration);
            start = true;
        }
        @Override
        public boolean act(float delta) {
            if (!start) Start();
            return entry.isComplete();
        }
    }
}
