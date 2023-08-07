package DrinkGame.Base;

import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Screen;
import SDK.SDK;

import java.util.List;

public class EndGameScreen extends Screen {
    public EndGameScreen(String name,Runnable replay) {
        super(name);

        Click("btReplay",()->{
            Hide();
            replay.run();
            SDK.i.ShowFullscreen();
        });
        Click("btRate",()->{
            SDK.i.Rate();
            SDK.i.TrackCustomEvent("rated_game");
        });
    }
    public EndGameScreen(Runnable replay) {
        this("EndGame",replay);
    }
    private int best=0;
    public void SetData(List<Integer> scoreList){
        for (int score : scoreList) best = Math.max(best,score);
        ITable table = FindITable("table");
        table.CloneChild(scoreList,this::SetScore);
    }
    private void SetScore(int index, int score, IGroup iGroup){
        int id = index+1;
        iGroup.FindILabel("lb").iParam.Set("num",id);
        iGroup.FindILabel("score").SetText(score);
        iGroup.FindIImage("ball").SetTexture("ball"+id);
        if (score==best) iGroup.RunAction("best");
    }
    public void SetWin(int playerID,boolean bot){//0->draw
        if (bot) iGroup.RunAction("bot");
        if (playerID==0) iGroup.RunAction("draw");
        else FindIGroup("line"+playerID).RunAction("win");
    }
    public void SetTop(List<Integer> list){
        ITable table = FindITable("table");
        table.CloneChild(list,this::SetPlayer);
    }
    private void SetPlayer(int index, int playerID, IGroup iGroup){
        int top = index+1;
        iGroup.FindILabel("lb").iParam.Set("num",playerID);
        iGroup.FindILabel("score").SetText("top"+top);
        iGroup.RunAction("player"+playerID);
        if (index==0) iGroup.RunAction("best");
    }
}
