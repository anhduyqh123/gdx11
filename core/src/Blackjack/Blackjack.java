package Blackjack;

import Blackjack.Controller.GBoard;
import Blackjack.Screen.GameScreen;
import GDX11.GDX;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Blackjack {

    public Blackjack()
    {
        GameScreen screen = new GameScreen();
        screen.Show();

        GBoard gBoard = new GBoard(screen.iGroup);
        gBoard.Start();
    }
}
