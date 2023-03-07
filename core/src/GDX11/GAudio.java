package GDX11;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GAudio {
    public static Plat plat = new Plat();
    public static GAudio i = new GAudio();
    private String musicName;
    private Map<String, GDX.Runnable1<Float>> cbSound = new HashMap<>(),
            cbMusic = new HashMap<>(),
            cbVibrate = new HashMap<>();
    public float soundVolume = GDX.GetPrefFloat("soundVolume", 1),
            musicVolume = GDX.GetPrefFloat("musicVolume", 1),
            vibrateVolume = GDX.GetPrefFloat("vibrateVolume", 1);

    private List<String> singles = new ArrayList<>();
    private void Refresh(Map<String, GDX.Runnable1<Float>> map,float volume)
    {
        for(GDX.Runnable1 cb : map.values()) cb.Run(volume);
    }
    private void RefreshSound()
    {
        Refresh(cbSound,soundVolume);
    }
    private void RefreshMusic()
    {
        Refresh(cbMusic,musicVolume);
    }
    private void RefreshVibrate()
    {
        Refresh(cbVibrate,vibrateVolume);
    }
    public void AddSoundEvent(String st, GDX.Runnable1<Float> cb)
    {
        cbSound.put(st,cb);
        cb.Run(soundVolume);
    }
    public void AddMusicEvent(String st, GDX.Runnable1<Float> cb)
    {
        cbMusic.put(st,cb);
        cb.Run(musicVolume);
    }
    public void AddVibrateEvent(String st, GDX.Runnable1<Float> cb)
    {
        cbVibrate.put(st,cb);
        cb.Run(vibrateVolume);
    }

    public void SetSoundVolume(float volume)
    {
        soundVolume = volume;
        GDX.SetPrefFloat("soundVolume", soundVolume);
        RefreshSound();
    }
    public void SetMusicVolume(float volume)
    {
        musicVolume = volume;
        GDX.SetPrefFloat("musicVolume", musicVolume);
        RefreshMusic();
        if (musicName==null) return;
        Asset.i.GetMusic(musicName).setVolume(musicVolume);
    }
    public void SetVibrateVolume(float volume)
    {
        vibrateVolume = volume;
        GDX.SetPrefFloat("vibrateVolume", vibrateVolume);
        RefreshVibrate();
    }

    private float Switch(float volume)
    {
        return volume==0?1:0;
    }
    public void SwitchSound()
    {
        SetSoundVolume(Switch(soundVolume));
    }
    public void SwitchMusic()
    {
        SetMusicVolume(Switch(musicVolume));
    }
    public void SwitchVibrate()
    {
        vibrateVolume = Switch(vibrateVolume);
        GDX.SetPrefFloat("vibrateVolume", vibrateVolume);
        DoVibrate(100);
        RefreshVibrate();
    }

    //Vibrate
    public void DoVibrate()
    {
        DoVibrate(30);
    }
    public void DoVibrate(int num)
    {
        plat.Vibrate((int)(num*i.vibrateVolume));
    }
    private void DoSingleVibrate(float delay,int num)
    {
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
    public void StartMusic(String name)
    {
        musicName = name;
        Music music = Asset.i.GetMusic(name);
        music.setLooping(true);
        music.setVolume(musicVolume);
        music.play();
    }

    public void PauseMusic()
    {
        if (musicName==null) return;
        Asset.i.GetMusic(musicName).pause();
    }
    public void ResumeMusic()
    {
        if (musicName==null) return;
        Asset.i.GetMusic(musicName).play();
    }
    public void StopMusic()
    {
        if (musicName==null) return;
        StopMusic(musicName);
        musicName = null;
    }

    //Sound
    public void PlaySingleSound(String name)
    {
        i.PlaySingleSound(name,0.1f);
    }
    public void PlaySingleSound(String name,float delay)
    {
        if (singles.contains(name)) return;
        singles.add(name);
        PlaySound(name);
        GDX.Delay(()->singles.remove(name),delay);
    }
    public void PlaySingleSound(String name,int from,int to,float delay)
    {
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
    public void PlaySound(String name)
    {
        Asset.i.GetSound(name).play(i.soundVolume);
    }
    public void StopSound(String name)
    {
        Asset.i.GetSound(name).stop();
    }

    public static class Plat
    {
        public void Vibrate(int num)
        {
            GDX.Vibrate(num);
        }
        public void SoundLoop(String name,float volume,float pan,GDX.Runnable1<Long> cb)
        {
            cb.Run(Asset.i.GetSound(name).loop(volume,1,pan));
        }
    }
}
