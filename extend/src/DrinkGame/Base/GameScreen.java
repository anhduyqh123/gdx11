package DrinkGame.Base;

import GDX11.Screen;

public class GameScreen extends Screen {
    public static GameScreen i;
    public GameScreen() {
        super("Game");
        i = this;
        SetEndHideEvent(()-> MenuScreen.i.Show());
    }
}
