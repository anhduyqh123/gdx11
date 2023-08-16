package DrinkGame.Game;

import GDX11.Config;

public class DrinkCup extends Drinkopoly {
    public DrinkCup() {
        super("drinkcup");
        Config.i.Set("maxSlot",24);
    }
}
