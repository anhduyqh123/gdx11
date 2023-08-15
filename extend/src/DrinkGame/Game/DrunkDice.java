package DrinkGame.Game;

import DrinkGame.Base.BaseGame;
import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.ITable;

public class DrunkDice extends BaseGame {
    public DrunkDice() {
        super("drunkdice");
        ITable table = game.FindITable("table");
        Config.i.SetRun1("end",a->{
            int x = game.FindIActor("dice1").iParam.Get("id",1)-1;
            int y = game.FindIActor("dice0").iParam.Get("id",1)-1;
            table.GetIChild(y*table.column+x).RunAction("on");
        });
    }
}
