package DrinkGame.Base;

import GDX11.Screen;
import SDK.SDK;

public class EndGameScreen extends Screen {
    public EndGameScreen(String name,Runnable replay) {
        super(name);

        Click("btReplay",()->{
            Hide();
            replay.run();
            SDK.i.ShowFullscreen();
        });
        Click("btRate",()->{
            SDK.i.Rate();
            SDK.i.TrackCustomEvent("rated_game");
        });
    }
    public EndGameScreen(Runnable replay) {
        this("EndGame",replay);
    }
}
