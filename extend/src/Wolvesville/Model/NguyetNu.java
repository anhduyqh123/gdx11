package Wolvesville.Model;

import java.util.ArrayList;
import java.util.List;

public class NguyetNu extends HumanX{
    public NguyetNu() {
        super("Nguyệt nữ","Muốn vô hiệu");
    }

    @Override
    public void Run() {
        if (VoHieuBoiGiaLangDie()) return;
        map.get(target).VoHieu();
    }
    public boolean VoHieu(Card card){
        if (VoHieuBoiGiaLangDie()) return false;
        if (target==null) return false;
        return target.equals(card.player);
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
