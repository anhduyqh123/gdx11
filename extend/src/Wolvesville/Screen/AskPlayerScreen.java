package Wolvesville.Screen;

import Extend.IDropDown;
import Wolvesville.Global;
import Wolvesville.Model.Card;

public class AskPlayerScreen extends BaseScreen implements Global {
    public AskPlayerScreen(Card fc) {
        super("AskPlayer",fc);
    }

    @Override
    public void Init(Card fc) {
        FindILabel("lb").SetText(fc.name);
        IDropDown iDropDown = FindIGroup("dropDown").iComponents.GetIComponent("dropDown");
        FindActor("btNext").setVisible(fc.Valid());
        SetMainDropdown(fc,iDropDown,()-> FindActor("btNext").setVisible(true));

        BtNext(this,()-> leftName.remove(fc.player));

        SetBack(this);
    }
}
