package Wolvesville.Screen;

import Extend.IDropDown;
import Wolvesville.Global;
import Wolvesville.Model.Card;
import Wolvesville.Model.HumanX;

import java.util.List;

public class HumanXScreen extends BaseScreen implements Global {
    protected IDropDown dropDown,dropDown1;
    protected final String no = "Không";
    public HumanXScreen(Card fc) {
        super("Baove",fc);
        HumanX humanX = (HumanX)fc;
    }
    protected void ShowFunc(String name){

    }
    protected void Refresh(Card fc){
        FindActor("btNext").setVisible(false);
        FindActor("lbInfo").setVisible(fc.BiVoHieu());
    }

    @Override
    public void Init(Card fc) {
        FindILabel("lb").SetText(fc.name);
        FindILabel("lb1").SetText(fc.wantTo);

        dropDown = FindIGroup("dropDown").iComponents.GetIComponent("dropDown");
        dropDown1 = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");
        HumanX humanX = (HumanX)fc;

        Refresh(fc);
        SetMainDropdown(fc,dropDown,()->{
            dropDown1.SetItems(GetTargetListView());
            Refresh(fc);
        });
        dropDown1.onSelect = vl->{
            ShowFunc(vl);
            humanX.SetTarget(vl.equals(no)?null:vl);
            FindActor("btNext").setVisible(true);
        };
        dropDown1.SetItems(GetTargetListView());
        dropDown1.SetView("?");


        BtNext(this,()->{
            leftName.remove(fc.player);
        });

        CheckForcedTarget(humanX,this);
        CheckGiaLangDie(fc,this);
        CheckDie(fc,this);

        Click("btInfo",()->new PlayerInfoScreen().Show());
        SetBack(this);
    }
    protected List<String> GetTargetListView(){
        HumanX humanX = (HumanX)fc;
        return humanX.GetValidList(alive);
    }
}
