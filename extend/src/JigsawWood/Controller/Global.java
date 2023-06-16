package JigsawWood.Controller;

import Extend.XItem;
import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;
import GDX11.Util;
import JigsawWood.Screen.ShopScreen;
import SDK.SDK;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Global {
    public static XItem itemCoin = new XItem("coin",100);
    public static XItem itemKill = new XItem("kill",3);
    public static XItem itemShuffle = new XItem("shuffle",5);
    public static XItem itemHint = new XItem("hint",3);

    public static void InitItem()
    {
        XItem.InitItem(itemCoin);
        XItem.InitItem(itemKill);
        XItem.InitItem(itemShuffle);
        XItem.InitItem(itemHint);
        itemKill.SetAddItem(3);itemShuffle.SetAddItem(5);itemHint.SetAddItem(3);
    }
    public static void AddCoin(int coin)
    {
        Config.i.Set("coinNum",coin/10);
        Config.i.GetRun("videoCoin").run();
    }
    public static void SetCoinEvent(IGroup coin)
    {
        coin.Click(()-> new ShopScreen().Show());
        Global.itemCoin.SetChangeEvent("game",(o, n)->coin.FindILabel("lb").SetText(n));
        Config.i.Set("addCoin",(GDX.Runnable1) ia->Global.itemCoin.Add(10));
        Config.i.SetRun("videoCoin",()->{//add 100coin
            Util.For(1, Config.i.Get("coinNum"), i->{
                IActor c = coin.Obtain("icon");
                c.iParam.Set("delay",(i/2)*0.1f);
                c.RunAction("add");
            });
        });
    }
    public static void AddTopCoin(Screen screen)
    {
        IGroup coin = Screen.GetLatest().FindIGroup("top").FindIGroup("coin");
        screen.onShow = ()->{
            coin.GetActor().setTouchable(Touchable.disabled);
            coin.AddToParent(screen.iGroup);
        };
        screen.onHideDone = ()->{
            coin.AddToParent(coin.GetIParent());
            coin.GetActor().setTouchable(Touchable.enabled);
        };
    }
    public static void ShowVideoReward(Runnable cb)
    {
        if (SDK.i.isVideoRewardReady())
            SDK.i.ShowVideoReward(success->{
                if (success) cb.run();
            });
        else NotVideo();
    }
    private static void NotVideo()
    {
        Screen dialog = new Screen("Dialog");
        dialog.iGroup.RunAction("no_video");
        dialog.Show();
    }
}
