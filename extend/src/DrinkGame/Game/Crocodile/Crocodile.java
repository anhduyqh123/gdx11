package DrinkGame.Game.Crocodile;

import DrinkGame.Base.BaseGame;
import DrinkGame.Base.EndGameScreen;
import DrinkGame.Base.VSlider;
import GDX11.Config;
import GDX11.GAudio;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.Util;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Crocodile extends BaseGame {
    private int teethNum=10, penNum =1;
    private final List<String> answer = new ArrayList<>();
    public Crocodile() {
        super("crocodile");
        NewGame();
    }
    @Override
    protected void NewGame() {
        super.NewGame();
        Config.i.SetRun1("onTouch",a-> CheckLose(a.name));
        VSlider teeth = new VSlider(game.FindIGroup("teeth"));
        teeth.SetLimit(2,10);
        VSlider pen = new VSlider(game.FindIGroup("pen"));
        pen.onChange = vl-> penNum = vl;
        teeth.onChange = vl->{
            teethNum = vl;
            pen.SetLimit(1,vl-1);
            pen.SetValue(Math.min(penNum,vl-1));
        };
        teeth.SetValue(teethNum);
        game.Click("btStart",this::StartGame);

        game.iGroup.RunAction("start");
    }

    @Override
    protected EndGameScreen NewEndGameScreen(){
        return new EndGameScreen("EndGame0",this::Replay);
    }
    private void StartGame(){
        //game.FindActor("board").setTouchable(Touchable.enabled);
        IGroup iGroup = game.iGroup;
        iGroup.RunAction("game");
        game.Run(()->{
            GAudio.i.PlaySound("start1");
            iGroup.RunAction("open");
        },1f);
        NewResult();
    }

    private void NewResult(){
        IGroup tooth = game.FindIGroup("board").FindIGroup("tooth");
        tooth.ForIChild(a->a.GetActor().setVisible(false));
        List<String> list = new ArrayList<>();
        Util.For(0,teethNum-1, i->list.add("i"+i));
        for (String s : list) tooth.FindActor(s).setVisible(true);
        Collections.shuffle(list);
        Util.For(0,penNum-1,i->answer.add(list.get(i)));
    }
    private void CheckLose(String name)
    {
        IGroup board = game.FindIGroup("board");
        if (answer.contains(name))
        {
            answer.remove(name);
            board.GetActor().setTouchable(Touchable.disabled);
            game.Run(()->{
                GAudio.i.DoVibrate(100);
                GAudio.i.PlaySound("bite");
                game.iGroup.RunAction("close");
            },0.2f);

            if (answer.size()==0) game.Run(this::EndGame,1f);
            else game.Run(()->{
                board.GetActor().setTouchable(Touchable.enabled);
                game.iGroup.RunAction("open");
            },1f);
        }
        else
        {
            if (MathUtils.random(1,5)==1) board.RunAction("shake");
        }
    }
}
