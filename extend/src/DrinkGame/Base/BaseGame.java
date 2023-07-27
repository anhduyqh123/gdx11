package DrinkGame.Base;

import GDX11.Config;
import GDX11.Screen;
import SDK.SDK;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class BaseGame {
    protected String name;
    protected GameScreen game = new GameScreen();
    protected int win,total;
    public BaseGame(String name){
        this.name = name;
        SDK.i.TrackCustomEvent("game_"+name);
        win = Config.GetPref(name+"win",0);
        total = Config.GetPref(name+"total",0);

        game.Show();
        ShowTut();
    }
    protected void Replay(){
        SDK.i.TrackCustomEvent("replay_"+name);
        NewGame();
    }
    protected void NewGame(){
        game.iGroup.Reconnect();
        game.iGroup.Refresh();
        InitUI();
    }
    protected void InitUI(){
        game.Click("btHome",()->{});
        game.Click("btTut",()->NewTutScreen().Show());
    }
    protected void EndGame(boolean win){
        total++;
        Config.SetPref(name+"total",total);
        if (win){
            this.win++;
            Config.SetPref(name+"win",this.win);
        }
    }
    protected void EndGame(){
        total++;
        Config.SetPref(name+"total",total);

        NewEndGameScreen().Show();
    }
    protected EndGameScreen NewEndGameScreen(){
        return new EndGameScreen("EndGame",this::Replay);
    }

    protected void ShowTut()
    {
        String key = "tut_"+name;
        if (!Config.GetPref(key,false)){
            game.setTouchable(Touchable.disabled);
            game.Run(()->{
                NewTutScreen().Show();
                game.setTouchable(Touchable.enabled);
                Config.SetPref(key,true);
            },1f);
        }
    }
    protected Screen NewTutScreen()
    {
        return new TutScreen();
    }

    //banner
    protected void CheckBanner_LongDevice()
    {
        SDK.i.ShowBanner(Config.i.Get("long_device",false));
    }
    protected void CheckBanner_MediumDevice()
    {
        SDK.i.ShowBanner(Config.i.Get("medium_device",false));
    }
}
