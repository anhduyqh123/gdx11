package DrinkGame.Game.Drinkopoly;

import GDX11.Config;
import GDX11.GAudio;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Util;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

public class GPlayer {
    protected final int maxSlot = Config.i.Get("maxSlot",20);
    protected final float scale0 = Config.i.Get("playerScale",1f);
    protected final IGroup board,iGroup;
    public final int id;
    protected final Actor actor;
    protected final IActor btAuto;
    protected Runnable next;
    public int slot=-1,num,num1,num2;
    public GPlayer(int id, IGroup board){
        this.board = board;
        this.id=id;
        iGroup = board.FindIGroup("player"+id);
        iGroup.FindIActor("lbName").iParam.Set("num",id);
        actor = iGroup.FindActor("value");
        iGroup.FindIActor("value").iParam.Set("scale0",scale0);
        iGroup.FindIActor("value").iParam.SetRun("first",()->{
            CheckSlot(slot);
            ShowDice();
        });
        btAuto = iGroup.FindIActor("btAuto");
        btAuto.iParam.SetRun("clicked",this::AutoRoll);
        iGroup.RunAction("reset");
        iGroup.RunAction("player"+id);
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
        boolean auto = btAuto.iParam.Get("auto");
        board.FindIActor("dice").RunAction(auto?"clicked":"reset");
    }
    private void AutoRoll(){
        boolean auto = btAuto.iParam.Get("auto");
        if (!auto || next==null || !board.FindActor("dice").isTouchable()) return;
        board.FindIActor("dice").RunAction("clicked");
    }
    public void RollDone(){
        num1 = board.FindIGroup("dice").FindIActor("value").iParam.Get("id");
        num2 = board.FindIGroup("dice").FindIActor("value1").iParam.Get("id");
        num=num1+num2;
        StartJump();
    }
    public void StartJump(){
        int slot0 = slot;
        int del = num>0?1:-1;
        int start = slot+del;
        int end = slot+num;
        if (end>=maxSlot) NewRound();
        Jump(start,end);
        slot = end%maxSlot;
        CheckSlot(slot0);
    }
    protected void NewRound(){}
    protected void Jump(int start,int end){
        actor.setScale(scale0*0.8f);
        actor.toFront();
        SequenceAction se = Actions.sequence();
        Util.For(start,end, i->{
            se.addAction(GetActionAt(i%maxSlot));
            se.addAction(Actions.run(()-> GAudio.i.PlaySound("drop_ball")));
        });
        se.addAction(Actions.run(this::JumpDone));
        actor.addAction(se);
    }
    protected void JumpDone(){
        IGroup result = board.FindIGroup("result");
        result.FindIActor("lb0").iParam.Set("id",slot);
        result.FindIActor("lb1").iParam.Set("id",slot);
        result.RunAction("on");
        IActor box = board.FindIActor("i"+slot);
        num = box.iParam.Get("nextNum",0);
        if (num!=0){
            result.Run(()->{
                StartJump();
                result.RunAction("off");
            },1f);
        }
        else Config.i.GetRun1("jumpDone").Run(box);
    }
    protected Action GetActionAt(int number){
        Vector2 pos = GetPosAtSlot(number);
        return Actions.moveToAligned(pos.x,pos.y-10, Align.center, 0.2f, Interpolation.fade);
    }
    public Vector2 GetPosAtSlot(int slot){
        IActor box = board.FindIActor("i"+slot);
        return box.GetLocalToActor(actor.getParent(),Align.center);
    }
    public void NextTurn(){
        Runnable xxx = next;
        iGroup.Run(()->{
            iGroup.RunAction("reset");
            xxx.run();
        },0.2f);
        next = null;
    }
    public void CheckSlot(){
        board.FindIActor("i"+slot).RunAction("on");
        CheckSlot(slot);
    }
    protected void CheckSlot(int slot){
        if (slot<0) return;
        iGroup.FindIActor("value").iParam.Set("x1",actor.getX());
        iGroup.FindIActor("value").iParam.Set("y1",actor.getY());
        GDX.Runnable1<Integer> check = board.iParam.Get("CheckAtSlot");
        check.Run(slot);
    }
    public boolean IsFinish(){
        return slot==0;
    }
    public IActor GetValue(){
        return iGroup.FindIActor("value");
    }
}
