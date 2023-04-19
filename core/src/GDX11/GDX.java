package GDX11;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;

public class GDX {
    public static GDX i;
    public GDX()
    {
        i = this;
    }
    //Prefs
    private final Preferences pref = Gdx.app.getPreferences("Save");
    public void ClearPreferences()
    {
        pref.clear();
        pref.flush();
    }
    public long GetPrefLong(String key, long value0)
    {
        return pref.getLong(key,value0);
    }
    public void SetPrefLong(String key, long value)
    {
        pref.putLong(key,value);
        pref.flush();
    }
    public int GetPrefInteger(String key, int value0)
    {
        return pref.getInteger(key,value0);
    }
    public void SetPrefInteger(String key, int value)
    {
        pref.putInteger(key,value);
        pref.flush();
    }
    public float GetPrefFloat(String key, float value0)
    {
        return pref.getFloat(key,value0);
    }
    public void SetPrefFloat(String key, float value)
    {
        pref.putFloat(key,value);
        pref.flush();
    }
    public String GetPrefString(String key, String value0)
    {
        return pref.getString(key,value0);
    }
    public void SetPrefString(String key, String value)
    {
        pref.putString(key,value);
        pref.flush();
    }
    public Boolean GetPrefBoolean(String key, boolean value0)
    {
        return pref.getBoolean(key,value0);
    }
    public void SetPrefBoolean(String key, boolean value)
    {
        pref.putBoolean(key,value);
        pref.flush();
    }
    //Platform
    public static boolean IsDesktop()
    {
        return Gdx.app.getType()== Application.ApplicationType.Desktop;
    }
    public static void Vibrate(int num)
    {
        if (num<=0) return;
        Gdx.input.vibrate(num);
    }
    public static float DeltaTime()
    {
        return Gdx.graphics.getDeltaTime();
    }
    public static float GetFPS()
    {
        return Gdx.graphics.getFramesPerSecond();
    }

    public static void Log(Object log)
    {
        Gdx.app.log("log",log+"");
    }
    public static void Error(Object log)
    {
        Gdx.app.error("error",log+"");
    }
    public static void Exit()
    {
        GDX.PostRunnable(()->Gdx.app.exit());
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
    public static String GetStringByKey(String key)//get string from node
    {
        return GetString(Asset.i.GetNode(key).url);
    }
    public static FileHandle GetFile(String path)
    {
        return Gdx.files.internal(path);
    }
    public static List<FileHandle> GetFiles(FileHandle dir, String extension)
    {
        List<FileHandle> files = new ArrayList<>();
        for (FileHandle file : dir.list())
        {
            if (file.extension().equals("DS_Store")) continue;
            if (file.isDirectory()) files.addAll(GetFiles(file,extension));
            else {
                if (extension.equals("") || file.extension().equals(extension))
                    files.add(file);
            }
        }
        return files;
    }

    //try catch
    public static void Try(Runnable onTry)
    {
        Try(onTry,false);
    }
    public static void Try(Runnable onTry,boolean printError)
    {
        try {
            onTry.Run();
        }catch (Exception ignored){
            if (printError)
                ignored.printStackTrace();
        }
    }
    public static void Try(Runnable onTry, Runnable onCatch)
    {
        try {
            onTry.Run();
        }catch (Exception ignored){
            onCatch.Run();
        }
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
    public static class Vector4
    {
        public float x,y,z,w;
        public Vector4(float x,float y,float z,float w)
        {
            this.x = x;this.y=y;this.z=z;this.w=w;
        }

        @Override
        public String toString() {
            return "("+x+","+y+","+z+","+w+")";
        }
    }
}
