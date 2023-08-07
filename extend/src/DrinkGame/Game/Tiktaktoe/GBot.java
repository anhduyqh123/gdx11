package DrinkGame.Game.Tiktaktoe;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;

public class GBot extends GPlayer{
    public GBot(int id, IGroup board, Board model) {
        super(id, board, model);
        iGroup.RunAction("bot");
    }
    public void Turn(Runnable next){
        this.next = next;
        iGroup.RunAction("turn");
        String slot = model.NewStep(id);
        IActor cell = (IActor) board.iParam.Get("getCell", GDX.Func1.class).Run(slot);
        iGroup.Run(()->cell.Run("clicked"),0.4f);
    }
}
