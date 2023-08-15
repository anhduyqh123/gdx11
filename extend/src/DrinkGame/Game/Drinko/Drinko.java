package DrinkGame.Game.Drinko;

import DrinkGame.Base.BaseGame;
import DrinkGame.Base.EndGameScreen;
import DrinkGame.Base.OptionScreen;
import DrinkGame.Base.TutScreen;
import Extend.Box2D.IBody;
import Extend.Box2D.IBodyListener;
import GDX11.Config;
import GDX11.GAudio;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.Screen;
import GDX11.Util;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Drinko extends BaseGame {
    private IGroup board = game.FindIGroup("board");
    private Vector2 pos = new Vector2();
    private int player = 7,amount = 5,turn = 1,count,doneCount;
    private final Map<Integer,Integer> scoreMap = new HashMap<>();
    public Drinko() {
        super("drinko");

        Config.i.SetRun1("newBall",a->{
            IImage img = (IImage) a;
            img.SetTexture("ball"+turn);
            IBody iBody = a.iComponents.GetIComponent("body");
            a.GetActor().setX(pos.x, Align.center);
            iBody.AddEvent(new IBodyListener() {
                @Override
                public void OnUpdate(float delta) {
                    if (iBody.body.getLinearVelocity().len()<=0)
                        iBody.body.applyForceToCenter(MathUtils.random(-50,50),50,true);
                }
            });
        });
        Config.i.SetRun1("score",a->{
            IGroup ig = (IGroup)a;
            String st = ig.FindILabel("lb").text.replace("+","");
            int score = Integer.parseInt(st);
            scoreMap.put(turn,scoreMap.get(turn)+score);
            GAudio.i.PlaySingleSound("key_taken");
            doneCount--;
            if (doneCount<=0) NextTurn();
        });
        IActor obs = board.FindIGroup("obs");
        obs.Click(()->{
            if (count<=0) return;
            count--;
            if (count<=0) game.FindIActor("noti").RunAction("off");
            pos.set(obs.iParam.Get("clickedX"),obs.iParam.Get("clickedY"));
            obs.GetActor().localToParentCoordinates(pos);
            board.RunAction("drop");
        });
        NewGame();
    }

    @Override
    protected void NewGame() {
        NewOptionScreen().Show(()->{
            turn = 0;
            NextTurn();
        });
    }
    @Override
    protected Screen NewTutScreen() {
        return new TutScreen(3);
    }
    private void NextTurn(){
        turn++;
        if (turn>player){
            game.Run(this::EndGame,1f);
            return;
        }
        scoreMap.put(turn,0);
        game.FindIGroup("noti").FindIActor("lb").iParam.Set("num",turn);
        game.FindIActor("noti").RunAction("turn");
        game.FindIActor("noti").RunAction("noti");
        game.Run(()->{
            game.FindIActor("noti").RunAction("tap");
            game.FindIActor("noti").RunAction("on");
            count = amount;
            doneCount = amount;
        },2f);
    }

    @Override
    protected void OnNewOptionScreen(OptionScreen screen) {
        screen.NewSlider("player",2,7,player,vl->player=vl);
        screen.NewSlider("amount",1,5,amount,vl->amount=vl);
    }

    @Override
    protected EndGameScreen NewEndGameScreen() {
        EndGameScreen screen = super.NewEndGameScreen();
        List<Integer> list = new ArrayList<>();
        Util.For(1,player,i->list.add(scoreMap.get(i)));
        screen.SetData(list);
        return screen;
    }
}
