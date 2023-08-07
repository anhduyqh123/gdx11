package DrinkGame.Game.BottleSpin;

import DrinkGame.Base.BaseGame;
import DrinkGame.Base.TutScreen;
import GDX11.Config;
import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;

public class BottleSpin extends BaseGame {
    public BottleSpin() {
        super("bottlespin");
        IGroup board = game.FindIGroup("board");
        Config.i.SetRun1("spinDone",a->{
            board.RunAction("reset");
        });
    }

    @Override
    protected Screen NewTutScreen() {
        return new TutScreen(2);
    }
}
