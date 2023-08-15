package DrinkGame.Game.Crocodile;

import DrinkGame.Base.*;
import GDX11.Config;
import GDX11.GAudio;
import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;
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
        game.iGroup.RunAction("start");
        NewOptionScreen().Show(this::StartGame);
    }
    @Override
    protected Screen NewTutScreen() {
        return new TutScreen(3);
    }

    @Override
    protected EndGameScreen NewEndGameScreen(){
        return new EndGameScreen("EndGame0",this::Replay);
    }

    @Override
    protected void OnNewOptionScreen(OptionScreen screen) {
        VSlider pen = screen.NewSlider("pen",1,teethNum-1,penNum,vl->penNum=vl);
        screen.NewSlider("teeth",2,10,teethNum,vl->{
            teethNum = vl;
            pen.SetLimit(1,vl-1);
            pen.SetValue(Math.min(penNum,vl-1));
        });
    }

    private void StartGame(){
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
