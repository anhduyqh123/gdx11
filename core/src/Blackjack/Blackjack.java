package Blackjack;

import Blackjack.Screen.GameScreen;
import GDX11.GDX;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Blackjack {

    public Blackjack()
    {
        GameScreen screen = new GameScreen();
        Table a = screen.FindActor("table");
        GDX.Log(a.getChildren().size);
    }
}
