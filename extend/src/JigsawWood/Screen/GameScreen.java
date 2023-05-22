package JigsawWood.Screen;

import Extend.XItem;
import GDX11.*;
import GDX11.IObject.IAction.ICountAction;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ILabel;
import JigsawWood.Controller.Global;

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
        Global.SetCoinEvent(FindIGroup("top").FindIGroup("coin"));
    }
    public void SetBest(int value) {
        FindIGroup("top").FindIGroup("best").FindILabel("lb").SetText(value);
    }
    public void SetScore(int oldVL,int newVL){
        IActor lbScore = FindIGroup("top").FindIGroup("score").FindIActor("lb");
        lbScore.iAction.Find("count", ICountAction.class).Set(oldVL,newVL);
        lbScore.RunAction("count");
    }
    public void SetScore(int value){
        ILabel lbScore = FindIGroup("top").FindIGroup("score").FindIActor("lb");
        lbScore.SetText(value);
    }
    public void SetLevel(int value)
    {
        ILabel lbScore = FindIGroup("top").FindIGroup("level").FindIActor("lb");
        lbScore.SetText("LEVEL "+value);
    }
}
