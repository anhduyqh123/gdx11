package DrinkGame.Game.PassOutCore;

import DrinkGame.Game.DrinkopolyCore.GBoard;
import DrinkGame.Game.DrinkopolyCore.GPlayer;
import GDX11.Config;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Scene;
import GDX11.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GBoard1 extends GBoard {
    private List<String> passCard = new ArrayList<>();
    private List<String> pinkCard = new ArrayList<>();
    public GBoard1(IGroup board) {
        super(board);
        InitCard();
        Config.i.Set("maxSlot",48);
        Config.i.Set("playerScale",0.6f);

        Config.i.SetRun1("newPass",this::NewPassCard);
        Config.i.SetRun1("newPink",this::NewPinkCard);
    }
    @Override
    protected GPlayer NewGPlayer(int id) {
        return new GPlayer1(id,board);
    }
    @Override
    protected void JumpDone(IActor iActor) {
        if (iActor.iParam.Has("passout")) board.RunAction("newPass");
        else super.JumpDone(iActor);
    }
    private void InitCard(){
        Util.For(0,29, i->passCard.add("passout_pass"+i));
        Util.For(0,26, i->pinkCard.add("passout_pink"+i));
        Collections.shuffle(passCard);
        Collections.shuffle(pinkCard);
    }

    private String GetPassCard(List<String> listCard){
        String card = listCard.get(0);
        listCard.remove(card);
        listCard.add(card);
        return card;
    }
    private void NewPinkCard(IGroup iGroup){
        String card = pinkCard.get(0);
        pinkCard.remove(card);
        iGroup.FindILabel("lb").iParam.Set("text",card);
        iGroup.iParam.SetRun("off",()->{
            IActor pink = board.FindIGroup("player"+GetPlayer().id).FindIActor("pinkx");
            Scene.AddActorKeepTransform(iGroup.GetActor(),pink.GetActor());
        });
        iGroup.iParam.SetRun("close",()->{
            GetPlayer().TakeCard(card);
            GetPlayer().NextTurn();
        });
    }
    private void NewPassCard(IGroup iGroup){
        String card = GetPassCard(passCard);
        iGroup.FindILabel("lb").iParam.Set("text",card);
        iGroup.iParam.SetRun("close",()-> RunPassCard(card,iGroup));
    }

    @Override
    protected GPlayer1 GetPlayer() {
        return (GPlayer1)super.GetPlayer();
    }

    private void RunPassCard(String card,IGroup iGroup){
        switch (card){
            case "passout_pass0":
                GetPlayer().GoToBar();
                break;
            case "passout_pass1":
                for (GPlayer pl : players)
                    ((GPlayer1)pl).GoToBarAll();
                board.FindIActor("dice").RunAction("reset");
                break;
            case "passout_pass5":
                board.RunAction("newPink");
                break;
            case "passout_pass7":
                GetPlayer().num = 5;
                GetPlayer().StartJump();
                break;
            case "passout_pass9":
                if (GetPlayer().pinkCard.size()>0) GivePinkCard();
                else Result("1");
                break;
            case "passout_pass15":
            case "passout_pass24":
                GetPlayer().num = 3;
                GetPlayer().StartJump();
                break;
            case "passout_pass19":
            case "passout_pass22":
            case "passout_pass27":
                GetPinkCard();
                break;
            case "passout_pass23":
                GetPlayer().num = -2;
                GetPlayer().StartJump();
                break;
            case "passout_pass26":
                GetPlayer().num = -3;
                GetPlayer().StartJump();
                break;
            case "passout_pass29":
                GetPlayer().num = 15;
                GetPlayer().StartJump();
                break;
            default:
                GetPlayer().NextTurn();
        }
    }
    private void Result(String color){
        IGroup result = board.FindIGroup("result");
        result.FindActor("btClose").setVisible(false);
        result.FindILabel("lb0").iParam.Set("color",color);
        result.iParam.SetRun("close",()->GetPlayer().NextTurn());
        result.RunAction("on");
        result.Run(()->result.RunAction("off"),1f);
    }

    //PinkCard
    private void GivePinkCard(){
        List<GPlayer> list = new ArrayList<>(players);
        list.remove(GetPlayer());
        for (GPlayer pl : list) ((GPlayer1)pl).ShowGive();
    }
    private void GetPinkCard(){
        List<GPlayer1> list = new ArrayList<>();
        for (GPlayer pl : players){
            GPlayer1 pl1 = (GPlayer1) pl;
            if (pl1.pinkCard.size()>0) list.add(pl1);
        }
        list.remove(GetPlayer());
        if (list.size()==0){
            Result("2");
            return;
        }
        for (GPlayer1 pl : list)
            pl.ShowGet();
    }
}
