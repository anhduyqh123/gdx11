package SDK;

import GDX11.GDX;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by VuNguyen on 1/16/18.
 */

public interface IZen {
    //ads
    void ShowFullscreen();
    void ShowBanner(boolean visible);
    boolean isVideoRewardReady();
    void ShowVideoReward(GDX.Runnable1<Boolean> callback);

    //track
    void TrackCustomEvent(String eventName);
    void TrackLevelCompleted(int level);
    void TrackLevelFailed(int level);
    void TrackLevelStart(int level);

    //remote config
    int GetConfigInt(String name, int defaultValue);
    String GetConfigString(String name, String defaultValue);

    //other
    String GetLocale();
    void Vibrate(int num);

    //leaderboard
    void ShowLeaderBoard();
    void ReportScore(long score);

    //link
    void Rate();
    void Like();
    void LinkOtherGame(String packageName);
    void Log(String log);
    void Link(String url);

    void GetStringFromUrl(String url,GDX.Runnable1<String> onCompleted);
    void GetTextureFromUrl(String url,GDX.Runnable1<Texture> onCompleted);

    public void showAdflyIcon(Boolean visible);
    public void showAdflyIcon(Boolean visible,float x,float y);
}
