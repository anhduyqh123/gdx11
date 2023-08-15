package DrinkGame.Game.SnakeLadder;

import DrinkGame.Base.BaseGame;
import DrinkGame.Base.EndGameScreen;
import DrinkGame.Base.OptionScreen;
import DrinkGame.Base.TutScreen;
import GDX11.Screen;

import java.util.List;

public class SnakeLadder extends BaseGame {
    private List<Integer> topList;
    public SnakeLadder() {
        super("snakeladder");
        NewMode();
    }

    @Override
    protected void NewGame() {
        super.NewGame();
        GBoard gBoard = new GBoard(game.FindIGroup("board"));
        OptionScreen opScreen = NewOptionScreen();
        opScreen.Show(()->{
            gBoard.InitPlayer(opScreen.GetValue("player"));
            if (bot) gBoard.BotMode();
            gBoard.Start();
        });
        gBoard.endGame = list->{
            topList = list;
            EndGame();
        };
    }

    @Override
    protected EndGameScreen NewEndGameScreen() {
        EndGameScreen screen = super.NewEndGameScreen();
        screen.SetTop(topList);
        return screen;
    }

    @Override
    protected void OnNewOptionScreen(OptionScreen screen) {
        screen.NewSlider("player",2,4,4);
    }
}
