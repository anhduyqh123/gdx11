package SDK;

import GDX11.Config;
import GDX11.GDX;
import com.badlogic.gdx.graphics.Texture;

import java.util.Locale;

public abstract class SDK implements IZen {
    public static IZen i = new SDK(){};

    public static void SetSDK(IZen sdk)
    {
        i = sdk;
        Config.getRemote = i::GetConfigString;
        Config.i.Set("installationID",i.GetInstallationID());
    }
    public static void SetDesktopSDK()
    {
        SetSDK(i);
    }

    @Override
    public String GetInstallationID() {
        return "installationID";
    }

    @Override
    public void ShowFullscreen() {

    }

    @Override
    public void ShowBanner(boolean visible) {
    }

    @Override
    public boolean isVideoRewardReady() {
        return true;
    }

    @Override
    public void ShowVideoReward(GDX.Runnable1<Boolean> callback) {
        callback.Run(true);
    }

    @Override
    public void TrackCustomEvent(String eventName) {

    }

    @Override
    public void TrackLevelCompleted(int level) {

    }

    @Override
    public void TrackLevelFailed(int level) {

    }

    @Override
    public void TrackLevelStart(int level) {

    }

    @Override
    public int GetConfigInt(String name, int defaultValue) {
        return defaultValue;
    }

    @Override
    public String GetConfigString(String name, String defaultValue) {
        return defaultValue;
    }

    @Override
    public String GetLocale() {
        return Locale.getDefault().getLanguage()+"_"+Locale.getDefault().getCountry();
    }

    @Override
    public void Vibrate(int num) {

    }

    @Override
    public void ShowLeaderBoard() {

    }

    @Override
    public void ReportScore(long score) {

    }

    @Override
    public void Rate() {

    }

    @Override
    public void LinkOtherGame(String packageName) {

    }

    @Override
    public void Log(String log) {

    }

    @Override
    public void Like() {

    }

    @Override
    public void Link(String url) {

    }

    @Override
    public void GetStringFromUrl(String url, GDX.Runnable1<String> onCompleted) {

    }

    @Override
    public void GetTextureFromUrl(String url, GDX.Runnable1<Texture> onCompleted) {

    }

    @Override
    public void showAdflyIcon(Boolean visible,float x,float y) {
    }

    @Override
    public void DownloadPack(String pack, Runnable done) {
        done.run();
    }

    @Override
    public void showAdflyIcon(Boolean visible) {
    }
}
