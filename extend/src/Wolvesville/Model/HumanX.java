package Wolvesville.Model;

import java.util.List;

public class HumanX extends Card {
    public String target;
    public HumanX(String name,String wantTo) {
        super(name);
        this.wantTo = wantTo;
    }

    @Override
    public void Run() {
        if (VoHieuBoiGiaLangDie()) return;
        super.Run();
    }

    @Override
    public void Reset() {
        super.Reset();
        target = null;
    }

    @Override
    public boolean Valid() {
        return player!=null && target !=null;
    }

    public void SetTarget(String dan){
        target = dan;
    }
    public List<String> GetValidList(List<String> list){
        return list;
    }
    public boolean ForcedTarget(){
        return false;
    }


    @Override
    public void Set(Card card) {
        HumanX x = (HumanX) card;
        player = x.player;
        target = x.target;
    }

    @Override
    public Card Clone() {
        HumanX x = new HumanX(name,wantTo);
        x.player = player;
        x.target = target;
        return x;
    }
}
