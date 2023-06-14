package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.GDX;
import GDX11.Screen;
import Wolvesville.Global;
import Wolvesville.Model.Card;

public class CupidScreen extends BaseScreen implements Global {
    private IDropDown dropDown,dropDown1,dropDown2;
    public CupidScreen() {
        super("Cupid",cupid);
    }
    private void SetDrop(IDropDown dropDown, GDX.Runnable1<String> onSelect){
        dropDown.onSelect = vl->{
            onSelect.Run(vl);
            FindActor("btNext").setVisible(cupid.Valid());
        };
        dropDown.SetItems(leftName);
        dropDown.SetView("?");
    }

    @Override
    public void Init(Card fc) {
        dropDown = FindIGroup("dropDown0").iComponents.GetIComponent("dropDown");
        dropDown1 = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");
        dropDown2 = FindIGroup("dropDown2").iComponents.GetIComponent("dropDown");

        SetMainDropdown(fc,dropDown,()-> FindActor("btNext").setVisible(cupid.Valid()));
//        SetDrop(dropDown,vl->{
//            cupid.SetPlayer(vl);
//            map.put(vl,cupid);
//        });
        SetDrop(dropDown1,vl->cupid.SetPair(1,vl));
        SetDrop(dropDown2,vl->cupid.SetPair(2,vl));

        BtNext(this,()-> leftName.remove(cupid.player));

        FindActor("btNext").setVisible(false);
        SetBack(this);
    }
}
