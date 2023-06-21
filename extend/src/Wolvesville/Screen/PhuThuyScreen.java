package Wolvesville.Screen;

import Extend.IDropDown;
import Wolvesville.Global;
import Wolvesville.Model.Card;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhuThuyScreen extends BaseScreen implements Global {
    private IDropDown dropDown1,dropDown2;
    public PhuThuyScreen() {
        super("PhuThuy",phuthuy);
    }
    private void Refresh(){
        FindActor("btNext").setVisible(phuthuy.Valid());
        FindActor("lbInfo").setVisible(phuthuy.BiVoHieu());
        FindActor("dropDown1").setTouchable(targetSoi.size()>0 && phuthuy.save>0?Touchable.enabled:Touchable.disabled);
        FindActor("dropDown2").setTouchable(phuthuy.kill>0?Touchable.enabled:Touchable.disabled);
    }
    private void SetTarget(){
        List<String> list = new ArrayList<>();
        for (String player : targetSoi)
            if (PhuThuyCuu(player)) list.add(player);
        FindILabel("lb1").iParam.Set("xname",list.toString());
        list.add(0,"Không");
        dropDown1.SetItems(list);
        dropDown1.SetSelected(list.get(0));
    }
    private boolean PhuThuyCuu(String player){
        Card card = map.get(player);
        if (card==null) return true;
        return card.PhuThuy_Cuu();
    }

    @Override
    public void Init(Card fc) {
        IDropDown dropDown = FindIGroup("dropDown0").iComponents.GetIComponent("dropDown");
        SetMainDropdown(phuthuy,dropDown, this::Refresh);

        dropDown1 = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");
        dropDown2 = FindIGroup("dropDown2").iComponents.GetIComponent("dropDown");
        List<String> list = new ArrayList<>(Arrays.asList("Không"));
        list.addAll(alive);
        list.remove(phuthuy.player);
        dropDown2.SetItems(list);
        dropDown2.SetSelected(list.get(0));

        SetTarget();

        Refresh();

        phuthuy.ResetX();
        BtNext(this,()->{
            if (dropDown1.GetIndexSelected()!=0) phuthuy.Save(dropDown1.selected);
            if (dropDown2.GetIndexSelected()!=0) phuthuy.Kill(dropDown2.selected);
            leftName.remove(phuthuy.player);
        });

        CheckGiaLangDie(phuthuy,this);
        CheckDie(phuthuy,this);

        Click("btInfo",()->new PlayerInfoScreen().Show());

        SetBack(this);
    }
}
