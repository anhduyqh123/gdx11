package Blackjack;

import Blackjack.Controller.GBoard;
import Blackjack.Controller.GConfig;
import Blackjack.Controller.GDeck;
import Blackjack.Controller.GPlayer;
import Blackjack.Screen.GameScreen;
import Extend.XItem;
import GDX11.GAudio;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IEvent;
import GDX11.Screen;

public class Blackjack {
    {
        GDX.ClearPreferences();
    }
    private GConfig gConfig = new GConfig();
    public Blackjack()
    {
        GAudio.i.StartMusic("bgMusic");

        XItem.InitItem(new XItem("theme",0));
        XItem.InitItem(new XItem("money"));

        IEvent.SetGameRun("exp1", ia-> gConfig.CheckNextLevel());
        gConfig.nextLevel = this::NewLevel;

        GPlayer.onWin = gConfig::Win;
        GPlayer.onLose = gConfig::Lose;
        GPlayer.onPush = gConfig::Push;

        NewGame();
    }
    private void NewGame()
    {
        GameScreen screen = new GameScreen();
        XItem.Get("money").AddChangeEvent("game", screen::SetMoney);
        screen.reset = this::NewGame;
        screen.Show();

        gConfig.SetExpEvent(screen::SetExp);
        gConfig.SetLevelEvent(screen::SetLevel);

        GBoard gBoard = new GBoard(screen.iGroup);
        gBoard.Start();
    }
    private void NewLevel(int level)
    {
        GAudio.i.PlaySound("sgoal");
        Screen screen = new Screen("LevelUp");
        int coin = gConfig.GetChipReward(level);
        screen.FindILabel("lbContent").iParam.Set("coin",coin);
        screen.FindIGroup("star").FindILabel("lb").SetText(level);
        screen.AddClick("btClaim",()->{
            XItem.Get("money").Add(coin);
            screen.Hide();
        });
        screen.AddClick("btX2",()->{
            XItem.Get("money").Add(coin);
            screen.Hide();
        });
        screen.Show();
    }
}
