package Wolvesville.Screen;

import Extend.IDropDown;
import Wolvesville.Global;
import Wolvesville.Model.Card;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhuThuyScreen extends BaseScreen implements Global {
    private boolean activeDrop1;
    public PhuThuyScreen() {
        super("PhuThuy",phuthuy);
    }
    private void Refresh(){
        FindActor("btNext").setVisible(phuthuy.Valid());
        FindActor("lbInfo").setVisible(phuthuy.BiVoHieu());
        FindActor("dropDown1").setTouchable(activeDrop1 && phuthuy.save>0?Touchable.enabled:Touchable.disabled);
        FindActor("dropDown2").setTouchable(phuthuy.kill>0?Touchable.enabled:Touchable.disabled);
    }
    private void SetTarget(){
        String biCan = targetSoi.Get();
        if (biCan==null) FindILabel("lb1").SetText("Không ai bị cắn");
        else{
            if (KoBiCan(biCan)){
                FindILabel("lb1").SetText("Không ai bị cắn");
                return;
            }
            if (baove.IsCover(biCan)) FindILabel("lb1").SetText(biCan+" Đã được bảo vệ!");
            else{
                FindILabel("lb1").iParam.Set("xname",biCan);
                activeDrop1 = true;
            }
        }
    }
    private boolean KoBiCan(String biCan){
        Card card = map.get(biCan);
        if (card==null) return false;
        if (card.equals(gialang)) return gialang.heal>0;
        if (card.equals(bansoi)) return true;
        return false;
    }

    @Override
    public void Init(Card fc) {
        IDropDown dropDown = FindIGroup("dropDown0").iComponents.GetIComponent("dropDown");
        SetMainDropdown(phuthuy,dropDown, this::Refresh);

        IDropDown dropDown1 = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");
        IDropDown dropDown2 = FindIGroup("dropDown2").iComponents.GetIComponent("dropDown");
        List<String> list = new ArrayList<>(Arrays.asList("Không"));
        list.addAll(alive);
        list.remove(phuthuy.player);
        dropDown2.SetItems(list);
        dropDown2.SetSelected(list.get(0));

        SetTarget();

        Refresh();

        phuthuy.ResetX();
        BtNext(this,()->{
            if (dropDown1.GetIndexSelected()==0) phuthuy.Save(targetSoi.Get());
            if (dropDown2.GetIndexSelected()!=0) phuthuy.Kill(dropDown2.selected);
            leftName.remove(phuthuy.player);
        });

        CheckGiaLangDie(phuthuy,this);
        CheckDie(phuthuy,this);

        Click("btInfo",()->new PlayerInfoScreen().Show());

        SetBack(this);
    }
}
