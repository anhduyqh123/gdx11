package Blackjack;

import Blackjack.Controller.GBoard;
import Blackjack.Controller.GConfig;
import Blackjack.Controller.GPlayer;
import Blackjack.Screen.GameScreen;
import Extend.XItem;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IEvent;

public class Blackjack {

    public Blackjack()
    {
        GDX.ClearPreferences();

        XItem.InitItem(new XItem("money",5000));
        GConfig gConfig = new GConfig();

        GameScreen screen = new GameScreen();
        XItem.Get("money").AddChangeEvent("game",(o,n)->{
            IGroup bar = screen.FindIGroup("top").FindIGroup("bar");
            bar.FindILabel("lb").SetText(n);
        });
        screen.Show();

        gConfig.SetExpEvent(screen::SetExp);
        gConfig.SetLevelEvent(screen::SetLevel);
        IEvent.SetGameRun("exp1", ia-> gConfig.CheckNextLevel());

        GPlayer.onWin = gConfig::Win;
        GPlayer.onLose = gConfig::Lose;
        GPlayer.onPush = gConfig::Push;

        GBoard gBoard = new GBoard(screen.iGroup);
        gBoard.Start();
    }
}
