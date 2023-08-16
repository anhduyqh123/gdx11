package DrinkGame.Game.TiktaktoeCore;

import GDX11.IObject.IActor.IGroup;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class GPlayer {
    protected IGroup board,iGroup;
    public int id, winCount;
    protected Runnable next;
    protected Board model;
    public GPlayer(int id,IGroup board,Board model){
        this.board = board;
        this.id = id;
        this.model = model;
        iGroup = board.GetIParent().FindIGroup("player"+id);
        iGroup.FindILabel("lb").SetText(winCount);
    }
    public void Touch(IGroup slot){
        Vector2 pos = board.FindITable("table").GetCell(slot.GetActor());
        model.Set(id,null,(int)pos.x+"."+(int)pos.y);
        slot.RunAction("player"+id);
        board.GetActor().setTouchable(Touchable.disabled);
        board.Run(this::NextTurn,0.4f);
    }
    public void Turn(Runnable next){
        this.next = next;
        board.GetActor().setTouchable(Touchable.enabled);
        iGroup.RunAction("turn");
    }
    protected void NextTurn(){
        next.run();
        iGroup.RunAction("reset");
    }
    public void Win(){
        winCount++;
        iGroup.FindILabel("lb").SetText(winCount);
    }
}
