package DrinkGame.Game;

import DrinkGame.Game.DrinkopolyCore.GBoard;
import DrinkGame.Game.PassOutCore.GBoard1;
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
