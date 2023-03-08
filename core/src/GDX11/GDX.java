package GDX11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;
import java.util.Map;

public class GDX {
    public static void Vibrate(int num)
    {
        if (num<=0) return;
        Gdx.input.vibrate(num);
    }
    public static float DeltaTime()
    {
        return Gdx.graphics.getDeltaTime();
    }
    public static Func2<Object,String,Object> getRemote = (key,vl0)->vl0;
    //remote
    public static <T> T GetRemote(String key,T value0)
    {
        return (T)getRemote.Run(key,value0);
    }

    public static void Log(Object log)
    {
        Gdx.app.log("log",log+"");
    }
    public static void Error(Object log)
    {
        Gdx.app.error("error",log+"");
    }
    //Prefs
    private static final Preferences prefs = Gdx.app.getPreferences("Save");
    public static void ClearPreferences()
    {
        prefs.clear();
    }
    public static long GetPrefLong(String key, long value0)
    {
        return prefs.getLong(key,value0);
    }
    public static void SetPrefLong(String key, long value)
    {
        prefs.putLong(key,value);
        prefs.flush();
    }
    public static int GetPrefInteger(String key, int value0)
    {
        return prefs.getInteger(key,value0);
    }
    public static void SetPrefInteger(String key, int value)
    {
        prefs.putInteger(key,value);
        prefs.flush();
    }
    public static float GetPrefFloat(String key, float value0)
    {
        return prefs.getFloat(key,value0);
    }
    public static void SetPrefFloat(String key, float value)
    {
        prefs.putFloat(key,value);
        prefs.flush();
    }
    public static String GetPrefString(String key, String value0)
    {
        return prefs.getString(key,value0);
    }
    public static void SetPrefString(String key, String value)
    {
        prefs.putString(key,value);
        prefs.flush();
    }
    public static boolean GetPrefBoolean(String key, boolean value0)
    {
        return prefs.getBoolean(key,value0);
    }
    public static void SetPrefBoolean(String key, boolean value)
    {
        prefs.putBoolean(key,value);
        prefs.flush();
    }
    //PostRunnable
    public static void PostRunnable(java.lang.Runnable runnable)
    {
        Gdx.app.postRunnable(runnable);
    }
    public static Timer.Task Delay(java.lang.Runnable runnable, float delay) // delay by second
    {
        return Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                runnable.run();
            }
        },delay);
    }
    //file handle
    public static void WriteToFile(String path,String data)
    {
        Gdx.files.local(path).writeString(data,false);
    }
    public static String GetString(String path)//get string from file
    {
        return GetFile(path).readString();
    }
    public static String GetStringFromNode(String name)//get string from node
    {
        return GetString(Asset.i.GetNode(name).url);
    }
    public static FileHandle GetFile(String path)
    {
        return Gdx.files.internal(path);
    }

    //try catch
    public static void Try(Runnable onTry)
    {
        try {
            onTry.Run();
        }catch (Exception ignored){ignored.printStackTrace();}
    }
    public static void Try(Runnable onTry, Runnable onCatch)
    {
        try {
            onTry.Run();
        }catch (Exception ignored){}
        onCatch.Run();
    }
    public static <T> T Try(Func<T> onTry,Func<T> onCatch)
    {
        try {
            return onTry.Run();
        }catch (Exception ignored){}
        return onCatch.Run();
    }
    //interface
    public interface Runnable{
        void Run();
    }
    public interface Runnable1<T>{
        void Run(T value);
    }
    public interface Runnable2<T1,T2>{
        void Run(T1 vl1,T2 vl2);
    }
    public interface Runnable3<T1,T2,T3>{
        void Run(T1 vl1,T2 vl2,T3 vl3);
    }
    public interface Func<T>
    {
        T Run();
    }
    public interface Func1<T,T1>
    {
        T Run(T1 ob);
    }
    public interface Func2<T,T1,T2>
    {
        T Run(T1 ob1,T2 ob2);
    }

    public static class Ref<T>
    {
        private T object;
        public Ref()
        {
            Set(null);
        }
        public Ref(T object)
        {
            Set(object);
        }
        public T Get()
        {
            return object;
        }
        public void Set(T object)
        {
            this.object = object;
        }
    }
}
