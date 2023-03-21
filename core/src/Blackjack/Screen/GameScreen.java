package Blackjack.Screen;

import GDX11.Screen;

public class GameScreen extends Screen {
    public GameScreen() {
        super("Game");
        FindIGroup("top").FindIActor("btMore").AddClick(()-> new MoreCoinScreen().Show());
    }
}
