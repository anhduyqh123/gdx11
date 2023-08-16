package DrinkGame.Base;

import GDX11.Asset;
import GDX11.Config;
import GDX11.Screen;
import SDK.SDK;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class BaseGame {
    protected String name;
    protected GameScreen game;
    protected int win,total;
    protected boolean bot;
    protected int win1,win2;
    public BaseGame(String name){
        this.name = name;
        Asset.i.ForceLoadPackages(name);

        game = new GameScreen();
        game.Click("btHome",()->NewBackMenuScreen().Show());
        game.Click("btTut",()->NewTutScreen().Show());

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
        game.FindIGroup("board").Reconnect();
        game.FindIGroup("board").Refresh();
    }
    protected void NewMode(){
        NewMode(this::NewGame, this::NewGame);
    }
    protected void NewMode(Runnable onBot,Runnable onMulti){
        Screen screen = new Screen("Mode");
        screen.Click("btBot",()->{
            screen.Hide();
            bot = true;
            onBot.run();
        });
        screen.Click("btMulti",()->{
            screen.Hide();
            bot = false;
            onMulti.run();
        });
        screen.Show();
    }
    protected void EndGame(int playerID,boolean bot){
        total++;
        Config.SetPref(name+"total",total);
        if (bot && playerID==1){
            this.win++;
            Config.SetPref(name+"win",this.win);
        }
        if (playerID==1) win1++;if (playerID==2) win2++;
        EndGameScreen screen = NewEndGameScreen();
        screen.SetWin(playerID,bot);
        EndGame(screen);
    }
    protected void EndGame(){
        total++;
        Config.SetPref(name+"total",total);
        EndGame(NewEndGameScreen());
    }
    protected void EndGame(Screen screen){
        game.setTouchable(Touchable.disabled);
        game.Run(()->{
            screen.Show();
            game.setTouchable(Touchable.enabled);
        },0.6f);
    }
    protected EndGameScreen NewEndGameScreen(){
        return new EndGameScreen("EndGameX",this::Replay);
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
            },0.6f);
        }
    }
    protected Screen NewBackMenuScreen(){
        Screen screen = new Screen("BackMenu");
        screen.Click("btYes",()->{
            screen.Hide();
            game.Hide();
        });
        return screen;
    }
    protected Screen NewTutScreen()
    {
        return new TutScreen();
    }

    protected OptionScreen NewOptionScreen(){
        OptionScreen screen = new OptionScreen();
        OnNewOptionScreen(screen);
        return screen;
    }
    protected void OnNewOptionScreen(OptionScreen screen){}

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
