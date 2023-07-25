package DrinkGame;

import GDX11.Config;
import GDX11.Scene;

public class DrinkGame {

    public DrinkGame(){
        float delH = Scene.i.height-Scene.i.height0;
        Config.i.Set("long_device",delH>300);
        Config.i.Set("medium_device",delH>=240);
    }
}
