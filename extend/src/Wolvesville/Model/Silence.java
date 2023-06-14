package Wolvesville.Model;

import java.util.ArrayList;
import java.util.List;

public class Silence extends HumanX{
    public Silence() {
        super("SILENCE","Muốn câm lặng");
    }

    @Override
    public void Run(){
        if (VoHieuBoiGiaLangDie()) return;
        events.add(target+" bị câm lặng");
    }

    @Override
    public List<String> GetValidList(List<String> list) {
        List<String> all = new ArrayList<>(list);
        all.remove(target);
        return all;
    }

    @Override
    public boolean VoHieuBoiGiaLangDie() {
        return gialang.Valid() && gialang.Die();
    }

    @Override
    public boolean ForcedTarget() {
        return true;
    }
}
