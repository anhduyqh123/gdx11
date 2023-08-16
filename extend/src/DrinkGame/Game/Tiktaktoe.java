package DrinkGame.Game;

import DrinkGame.Base.BaseGame;
import DrinkGame.Base.TutScreen;
import DrinkGame.Game.TiktaktoeCore.GBoard;
import GDX11.Screen;

public class Tiktaktoe extends BaseGame {
    private boolean bot;
    private final GBoard gBoard = new GBoard(game.FindIGroup("board3"));
    {
        gBoard.endGame = pl->EndGame(pl,bot);
    }
    public Tiktaktoe() {
        super("tiktaktoe");
        NewMode(this::Bot,this::Multi);
    }

    @Override
    protected Screen NewTutScreen() {
        return new TutScreen(1);
    }

    private void Bot(){
        bot = true;
        gBoard.Bot();
        NewGame();
    }
    private void Multi(){
        gBoard.Multi();
        NewGame();
    }

    @Override
    protected void NewGame() {
        gBoard.Start();
    }
}
