package DrinkGame.Game.PassOut;

import DrinkGame.Game.Drinkopoly.Drinkopoly;
import DrinkGame.Game.Drinkopoly.GBoard;
import GDX11.IObject.IActor.IGroup;

public class PassOut extends Drinkopoly {
    public PassOut() {
        super("passout");
    }

    @Override
    protected GBoard NewGBoard(IGroup iGroup) {
        return new GBoard1(iGroup);
    }
}
