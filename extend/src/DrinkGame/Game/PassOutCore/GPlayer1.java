package DrinkGame.Game.PassOutCore;

import DrinkGame.Game.DrinkopolyCore.GPlayer;
import GDX11.Config;
import GDX11.GAudio;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Scene;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.List;

public class GPlayer1 extends GPlayer {
    public final List<String> pinkCard = new ArrayList<>();
    private boolean goBar,takePink;
    private int countBar;
    public GPlayer1(int id, IGroup board) {
        super(id, board);
        iGroup.FindIActor("btGive").Click(this::GivePinkCard);
        iGroup.FindIActor("btGet").Click(this::GetPinkCard);
        Config.i.SetRun1("newCard",this::NewPinkCardX);
    }
    public void RollDone(){
        num1 = board.FindIGroup("dice").FindIActor("value").iParam.Get("id");
        num2 = board.FindIGroup("dice").FindIActor("value1").iParam.Get("id");
        num=num1+num2;
        if (CheckBar()){
            goBar = false;
            StartJump();
        }
        else NextTurn();
    }
    private void ResultPop(IGroup result){
        result.FindActor("btClose").setVisible(false);
        result.Run(()->result.RunAction("off"),1f);
    }

    @Override
    protected void NewRound() {
        takePink = true;
    }

    @Override
    protected void JumpDone(){
        IGroup result = board.FindIGroup("result");
        result.FindActor("btClose").setVisible(true);
        IActor box = board.FindIActor("i"+slot);
        num = box.iParam.Get("nextNum",0);
        String color = box.iParam.Get("color");
        result.FindILabel("lb0").iParam.Set("color",color);
        if (color.equals("bar") && !goBar){
            Config.i.GetRun1("jumpDone").Run(box);
            return;
        }
        result.iParam.SetRun("close",()->Config.i.GetRun1("jumpDone").Run(box));
        result.RunAction("on");
        if (color.equals("gray")){
            ResultPop(result);
            return;
        }
        if (color.equals("gobar")){
            result.iParam.SetRun("close",this::GoToBar);
            ResultPop(result);
        }
    }
    private void GoToBar(Runnable done){
        goBar = true;
        countBar = 0;
        actor.setScale(scale0*0.8f);
        actor.toFront();
        actor.addAction(Actions.sequence(GetActionAt(12),Actions.run(()->{
                    GAudio.i.PlaySingleSound("drop_ball");
                    slot = 12;
                    CheckSlot(slot);
                }),Actions.delay(0.6f),Actions.run(done)));
    }
    public void GoToBarAll(){
        iGroup.Run(()->GoToBar(()->{}), MathUtils.random(0,0.6f));
    }
    public void GoToBar(){
        GoToBar(this::JumpDone);
    }
    private boolean CheckBar(){
        if (!goBar) return true;
        return countBar>3 || num==7 || num==11;
    }
    public void TakeCard(String card){
        pinkCard.add(card);
        iGroup.FindIGroup("pinkx").FindILabel("lb").SetText(pinkCard.size());
    }

    @Override
    public boolean IsFinish() {
        return pinkCard.size()>=10;
    }

    @Override
    public void NextTurn() {
        if (takePink){
            takePink = false;
            board.RunAction("newPink");
        }
        else super.NextTurn();
    }
    public void ShowGive(){
        iGroup.FindActor("btGive").setVisible(true);
    }
    public void ShowGet(){
        iGroup.FindActor("btGive").setVisible(pinkCard.size()>0);
    }
    private void GivePinkCard(){
        board.RunAction("btOff");
        GPlayer1 player = board.iParam.Get("player");
        player.MovePinkCardTo(this);
    }
    private void GetPinkCard(){
        board.RunAction("btOff");
        GPlayer1 player = board.iParam.Get("player");
        this.MovePinkCardTo(player);
    }
    private void MovePinkCardTo(GPlayer player){
        iGroup.iParam.Set("player",player);
        board.FindIGroup("pinkx").RunAction("newCard");
    }
    private void NewPinkCardX(IActor iActor){
        GPlayer1 player = iGroup.iParam.Get("player_toMove");
        Scene.AddActorKeepTransform(iActor.GetActor(),player.iGroup.FindActor("pinkx"));
        String card = pinkCard.get(0);
        pinkCard.remove(card);
        iActor.iParam.SetRun("end",()->{
            player.TakeCard(card);
            GPlayer1 main = board.iParam.Get("player");
            main.NextTurn();
        });
    }
}
