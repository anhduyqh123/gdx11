package GDX11;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class GAudio {
    public static Plat plat = new Plat();
    public static GAudio i = new GAudio();

    public final static String soundVolume = "soundVolume",musicVolume="musicVolume",vibrateVolume="vibrateVolume";
    public final static String sound = "sound",music="music",vibrate="vibrate";
    private String currentMusic;

    private final List<String> singles = new ArrayList<>();

    private float GetSoundVolume() {
        return Config.GetPref(soundVolume,1f);
    }
    private float GetMusicVolume() {
        return Config.GetPref(musicVolume,1f);
    }
    private float GetVibrateVolume() {
        return Config.GetPref(vibrateVolume,1f);
    }
    private boolean IsSound() {
        return Config.GetPref(sound,true);
    }
    private boolean IsMusic() {
        return Config.GetPref(music,true);
    }
    private boolean IsVibrate() {
        return Config.GetPref(vibrate,true);
    }

    //Vibrate
    public void DoVibrate()
    {
        DoVibrate(30);
    }
    public void DoVibrate(int num) {
        if (IsVibrate())
            plat.Vibrate((int)(num*GetVibrateVolume()));
    }
    private void DoSingleVibrate(float delay,int num) {
        if (singles.contains("vibrate")) return;
        singles.add("vibrate");
        DoVibrate(num);
        GDX.Delay(()->singles.remove("vibrate"),delay);
    }
    public void DoSingleVibrate(float delay)
    {
        DoSingleVibrate(delay,30);
    }
    public void DoSingleVibrate()
    {
        DoSingleVibrate(0.1f,30);
    }

    //Music
    public void StopMusic(String name)
    {
        Asset.i.GetMusic(name).stop();
    }
    public void StartMusic(String name) {
        currentMusic = name;
        Music music = Asset.i.GetMusic(name);
        music.setLooping(true);
        music.setVolume(GetMusicVolume());
        if (IsMusic())
            music.play();
    }

    public void PauseMusic() {
        if (currentMusic == null) return;
        Asset.i.GetMusic(currentMusic).pause();
    }
    public void ResumeMusic() {
        if (currentMusic ==null) return;
        Asset.i.GetMusic(currentMusic).play();
    }
    public void StopMusic() {
        if (currentMusic ==null) return;
        StopMusic(currentMusic);
        currentMusic = null;
    }

    //Sound
    public void PlaySingleSound(String name)
    {
        i.PlaySingleSound(name,0.1f);
    }
    public void PlaySingleSound(String name,float delay) {
        if (singles.contains(name)) return;
        singles.add(name);
        PlaySound(name);
        GDX.Delay(()->singles.remove(name),delay);
    }
    public void PlaySingleSound(String name,int from,int to,float delay) {
        if (singles.contains(name)) return;
        singles.add(name);
        PlaySound(name,from,to);
        GDX.Delay(()->singles.remove(name),delay);
    }
    public void PlaySingleSound(String name,int from,int to)
    {
        PlaySingleSound(name, from, to,0.1f);
    }
    public void PlaySound(String name,int from,int to)
    {
        PlaySound(name+ MathUtils.random(from,to));
    }
    public void PlaySound(String name) {
        if (name.equals("")) return;
        if (IsSound())
            Asset.i.GetSound(name).play(GetSoundVolume());
    }
    public void StopSound(String name)
    {
        Asset.i.GetSound(name).stop();
    }

    public static class Plat {
        public void Vibrate(int num)
        {
            GDX.Vibrate(num);
        }
        public void SoundLoop(String name,float volume,float pan,GDX.Runnable1<Long> cb) {
            cb.Run(Asset.i.GetSound(name).loop(volume,1,pan));
        }
    }
}
