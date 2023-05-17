package JigsawWood.Screen;

import Extend.XItem;
import GDX11.Config;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;
import GDX11.Util;

public class GameScreen extends Screen {
    public GameScreen() {
        this("Game");
    }
    public GameScreen(String name) {
        super(name);
        AddClick("btBack",()->{
            Hide();
            new MenuScreen().Show();
        });
        SetCoinEvent(FindIGroup("top").FindIGroup("coin"));
    }

    public static void SetCoinEvent(IGroup coin)
    {
        XItem.Get("coin").AddChangeEvent("game",(o, n)->coin.FindILabel("lb").SetText(n));
        Config.SetRun("addCoin",ia->XItem.Get("coin").Add(10));
        Config.SetRun("videoCoin",()->{//add 100coin
            Util.For(0,9, i->{
                IActor c = coin.Obtain("icon");
                c.iParam.Set("delay",(i/2)*0.1f);
                c.RunAction("add");
            });
        });
    }
}
