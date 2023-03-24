package Blackjack.Screen;

import GDX11.GDX;
import GDX11.IObject.IAction.ICountAction;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IProgressBar;
import GDX11.Screen;

public class GameScreen extends Screen {
    public GameScreen() {
        super("Game");
        FindIGroup("top").FindIActor("btMore").AddClick(()-> new MoreCoinScreen().Show());
        FindIGroup("top").FindIActor("btSetting").AddClick(()->new Screen("Setting").Show());
    }
    public void SetExp(float p1,float p2)
    {
        p1 = Math.min(p1,1f);p2 = Math.min(p2,1f);
        IProgressBar iBar = FindIGroup("top").FindIGroup("star").FindIActor("value");
        if (p1==p2) iBar.SetValue(p1);
        else{
            iBar.iAction.Find("count", ICountAction.class).Set(p1,p2);
            iBar.RunAction("exp");
        }
    }
    public void SetLevel(int level)
    {
        IGroup star = FindIGroup("top").FindIGroup("star");
        star.FindILabel("lb").SetText(level);
    }
}
