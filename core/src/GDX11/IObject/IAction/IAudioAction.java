package GDX11.IObject.IAction;

import GDX11.GAudio;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IAudioAction extends IAction{
    public enum Type {
        Sound,
        Music,
        Vibrate,
        GameSound   //volume is effected by actor
    }
    public enum State {
        Play,
        PlaySingle,
        Loop,
        Stop
    }
    public Type type = Type.Sound;
    public State state = State.Play;
    public float volume = 1;
    public String effName = "";

    public IAudioAction() {
        super("audio");
    }

    @Override
    public void Run() {
        switch (type) {
            case Sound: Sound();break;
            case Music: Music();break;
            case Vibrate: Vibrate();break;
            case GameSound: GameSound();break;
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }

    //sound
    private void Sound() {
        switch (state) {
            case Play: GAudio.i.PlaySound(GetRealString(effName)); break;
            case PlaySingle: GAudio.i.PlaySingleSound(GetRealString(effName)); break;
            case Loop: break;
            case Stop: GAudio.i.StopSound(effName); break;
        }
    }
    private void Music() {
        switch (state) {
            case Play: GAudio.i.StartMusic(effName); break;
            case PlaySingle:
            case Loop: break;
            case Stop: GAudio.i.StopMusic(effName); break;
        }
    }
    private void Vibrate() {
        switch (state) {
            case Play: GAudio.i.DoVibrate((int)volume); break;
            case PlaySingle:
            case Loop:
            case Stop:  break;
        }
    }
    private void GameSound() {
    }
}
