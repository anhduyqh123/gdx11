package Wolvesville.Model;

import java.util.ArrayList;
import java.util.List;

public class BaoVe extends HumanX{
    public BaoVe() {
        super("Bảo Vệ","Muốn bảo vệ");
    }
    @Override
    public void Run(){
        if (VoHieuBoiGiaLangDie()) return;
        map.get(target).DuocBaoVe();
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

    public boolean IsCover(String player){
        return baove.Valid() && baove.Alive() && baove.target.equals(player);
    }
}
