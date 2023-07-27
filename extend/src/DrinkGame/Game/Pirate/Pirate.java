package DrinkGame.Game.Pirate;

import DrinkGame.Base.BaseGame;
import DrinkGame.Base.EndGameScreen;
import DrinkGame.Base.TutScreen;
import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;
import GDX11.Util;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class Pirate extends BaseGame {
    private IGroup board;
    private String result = "slot0";
    private final List<IActor> touched = new ArrayList<>();
    public Pirate() {
        super("pirate");
        InitUI();
        board = game.FindIGroup("board");
        Config.i.SetRun1("touch",a->{
            touched.add(a);
            if (a.name.equals(result)) board.Run("end");
        });
        Config.i.SetRun1("endgame",a-> EndGame());
        game.iGroup.RunAction("start");
        game.FindIActor("btStart").Click(this::NewGame);
    }

    @Override
    protected void NewGame() {
        Util.For(0,11,i->{
            IGroup slot = board.FindIGroup("slot"+i);
            int id = Integer.parseInt(slot.FindIImage("mask").texture.replace("slot_",""));
            slot.FindIImage("sword").SetTexture((MathUtils.random(0,1)==0?"sword_red":"sword_blue")+id);
        });
        result = "slot"+ MathUtils.random(0,11);
        board.RunAction("reset");
    }
    @Override
    protected EndGameScreen NewEndGameScreen(){
        return new EndGameScreen("EndGame0",()->{
            Util.ForIndex(touched,i->{
                IActor slot = touched.get(i);
                board.Run(()->slot.RunAction("out"),i*0.1f);
            });
            board.RunAction("jump_in");
            board.Run(this::Replay,Math.max(1.4f,touched.size()*0.1f));
            touched.clear();
        });
    }
    protected Screen NewTutScreen()
    {
        return new TutScreen(2);
    }
}
