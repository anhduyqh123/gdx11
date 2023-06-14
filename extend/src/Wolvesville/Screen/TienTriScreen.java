package Wolvesville.Screen;

import Wolvesville.Model.Card;

public class TienTriScreen extends HumanXScreen {
    public TienTriScreen() {
        super(tientri);
    }

    @Override
    protected void ShowFunc(String name) {
        if (tientri.Die() || tientri.VoHieuBoiGiaLangDie()) return;
        FindActor("lb2").setVisible(!tientri.BiVoHieu());
        FindILabel("lb2").SetText(Get(name));
    }
    private String Get(String name){
        Card card = map.get(name);
        if (card==null) return "Người";
        if (wolves.contains(card)) return "Sói";
        return "Người";
    }
}
