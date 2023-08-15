package DrinkGame.Game.Drinkopoly;

import DrinkGame.Base.BaseGame;
import DrinkGame.Base.EndGameScreen;
import DrinkGame.Base.OptionScreen;
import GDX11.IObject.IActor.IGroup;

import java.util.List;

public class Drinkopoly extends BaseGame {
    private List<Integer> topList;
    public Drinkopoly() {
        this("drinkopoly");
    }
    public Drinkopoly(String name){
        super(name);
        NewGame();
    }
    @Override
    protected void NewGame() {
        super.NewGame();
        GBoard gBoard = NewGBoard(game.FindIGroup("board"));
        OptionScreen opScreen = NewOptionScreen();
        opScreen.Show(()->{
            gBoard.InitPlayer(opScreen.GetValue("player"));
            gBoard.Start();
        });
        gBoard.endGame = list->{
            topList = list;
            EndGame();
        };
    }
    protected GBoard NewGBoard(IGroup iGroup){
        return new GBoard(iGroup);
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
