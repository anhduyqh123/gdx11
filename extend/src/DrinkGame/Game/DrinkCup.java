package DrinkGame.Game;

import DrinkGame.Game.Drinkopoly.Drinkopoly;
import GDX11.Config;

public class DrinkCup extends Drinkopoly {
    public DrinkCup() {
        super("drinkcup");
        Config.i.Set("maxSlot",24);
    }
}
