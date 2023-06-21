package Wolvesville.Model;

import java.util.ArrayList;
import java.util.List;

public class SoiTrang extends HumanX{
    public SoiTrang() {
        super("Sói Trắng", "Muốn cắn");
    }
    public void Can(){
        if (Die() || target==null) return;
        if (BiVoHieu()) return;
        targetSoi.add(target);
    }
    public boolean IsCan(String pl){
        if (!Valid()) return false;
        return target.equals(pl);
    }

    @Override
    public List<String> GetValidList(List<String> list) {
        List<String> all = new ArrayList<>();
        for (Card soi : wolves)
            if (soi.Alive()) all.add(soi.player);
        all.remove(player);
        return all;
    }
}
