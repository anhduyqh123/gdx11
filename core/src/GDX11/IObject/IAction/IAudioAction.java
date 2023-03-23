package GDX11.IObject.IAction;

import GDX11.GAudio;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IAudioAction extends IAction{
    public enum Type
    {
        Sound,
        Music,
        Vibrate,
        GameSound   //volume is effected by actor
    }
    public enum State
    {
        Switch,
        Play,
        PlaySingle,
        Loop,
        Stop
    }
    public Type type = Type.Sound;
    public State state = State.Switch;
    public float volume = 1;
    public String random = "";

    public IAudioAction() {
        super("audio");
    }

    @Override
    public void Run() {
        switch (type)
        {
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
    private String GetRandom()
    {
        if (random.equals("")) return random;
        String[] arr = random.split(",");
        return MathUtils.random(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]))+"";
    }
    private String GetName()
    {
        return name+GetRandom();
    }

    //sound
    private void Sound()
    {
        switch (state)
        {
            case Switch: GAudio.i.SwitchSound();break;
            case Play: GAudio.i.PlaySound(GetName()); break;
            case PlaySingle: GAudio.i.PlaySingleSound(GetName()); break;
            case Loop: break;
            case Stop: GAudio.i.StopSound(name); break;
        }
    }
    private void Music()
    {
        switch (state)
        {
            case Switch: GAudio.i.SwitchMusic();break;
            case Play: GAudio.i.StartMusic(name); break;
            case PlaySingle:
            case Loop: break;
            case Stop: GAudio.i.StopMusic(name); break;
        }
    }
    private void Vibrate()
    {
        switch (state)
        {
            case Switch: GAudio.i.SwitchVibrate();break;
            case Play: GAudio.i.DoVibrate((int)volume); break;
            case PlaySingle:
            case Loop:
            case Stop:  break;
        }
    }
    private void GameSound()
    {

    }
}
