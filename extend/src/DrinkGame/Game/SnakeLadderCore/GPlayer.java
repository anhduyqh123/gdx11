package DrinkGame.Game.SnakeLadderCore;

import GDX11.GAudio;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Util;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

public class GPlayer {
    protected final IGroup iGroup,board;
    public Actor actor;
    protected Runnable next;
    private final int maxSlot = 99;
    public int slot=-1,num,id;
    private boolean bot;
    private IActor btAuto;
    public GPlayer(int id,IGroup board){
        this.id = id;
        this.board = board;
        iGroup = board.FindIGroup("player"+id);
        actor = iGroup.FindActor("value");
        iGroup.FindIActor("value").iParam.SetRun("first",()->{
            CheckSlot(slot);
            ShowDice();
        });
        btAuto = iGroup.FindIActor("btAuto");
        btAuto.iParam.SetRun("clicked",this::AutoRoll);

        iGroup.RunAction("reset");
        iGroup.GetActor().setVisible(true);
    }
    public void BotMode(){
        bot = true;
        iGroup.RunAction("bot");
    }
    public void Turn(Runnable next){
        this.next = next;
        if (IsFinish()){
            NextTurn();
            return;
        }
        Resolve();
    }
    protected void Resolve(){
        iGroup.RunAction("turn");
        if (slot<0){
            slot = 0;
            iGroup.RunAction("first");
        }
        else ShowDice();
    }
    protected void ShowDice(){
        num = MathUtils.random(1,6);
        IActor dice = board.FindIGroup("dice").FindIActor("value");
        dice.iParam.Set("base","dice_"+num);
        board.FindIActor("dice").Run(bot?"clicked":"reset");
        AutoRoll();
    }
    private void AutoRoll(){
        boolean auto = btAuto.iParam.Get("auto");
        if (!auto || bot || !board.FindActor("dice").isTouchable()) return;
        board.FindIActor("dice").RunAction("clicked");
    }
    public void RollDone(){
        int start = slot+1;
        slot = Math.min(maxSlot,slot+num);
        CheckSlot(start-1);
        actor.setScale(0.8f);
        actor.toFront();
        SequenceAction se = Actions.sequence();
        Util.For(start,slot,i->{
            se.addAction(GetActionAt(i));
            se.addAction(Actions.run(()-> GAudio.i.PlaySound("drop_ball")));
        });
        se.addAction(Actions.run(this::JumpDone));
        actor.addAction(se);
    }
    private Action GetActionAt(int number){
        Vector2 pos = GetPosAtSlot(number);
        return Actions.moveToAligned(pos.x,pos.y-30, Align.bottom, 0.2f, Interpolation.fade);
    }
    protected void JumpDone(){
        board.iParam.Run("jumpDone");
    }
    public void NextTurn(){
        iGroup.RunAction("reset");
        next.run();
    }
    public void CheckSlot(){
        CheckSlot(slot);
    }
    private void CheckSlot(int slot){
        iGroup.FindIActor("value").iParam.Set("x1",actor.getX());
        iGroup.FindIActor("value").iParam.Set("y1",actor.getY());
        GDX.Runnable1<Integer> check = board.FindITable("table").iParam.Get("CheckAtSlot");
        check.Run(slot);
    }
    public Vector2 GetPosAtSlot(int slot){
        ITable table = board.FindITable("table");
        GDX.Func1<IActor,Integer> getCell = table.iParam.Get("GetCell");
        Vector2 pos = getCell.Run(slot).GetLocalPosition(Align.center);
        return getCell.Run(slot).GetActor().localToActorCoordinates(actor.getParent(),pos);
    }
    public void JumpTo(int newSlot){
        slot = newSlot;
        actor.addAction(Actions.sequence(GetActionAt(slot),Actions.run(this::JumpDone)));
    }
    public boolean IsFinish(){
        return slot>=maxSlot;
    }
}
