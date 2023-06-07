package Wolvesville.Screen;

import Wolvesville.Model.Card;
import Wolvesville.Model.Soi;

public class TienTriScreen extends HumanXScreen {
    public TienTriScreen() {
        super(tientri);
    }

    @Override
    protected void ShowFunc(String name) {
        FindActor("lb2").setVisible(true);
        FindILabel("lb2").SetText(Get(name));
    }
    private String Get(String name){
        Card card = map.get(name);
        if (card==null) return "Người";
        if (card instanceof Soi) return "Sói";
        return "Người";
    }
}
