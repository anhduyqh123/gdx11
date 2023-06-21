package Wolvesville.Model;

import java.util.ArrayList;
import java.util.List;

public class Chube extends HumanX{
    public Chube() {
        super("Chú Bé", "Muốn chọn má nuôi");
    }
    @Override
    public List<String> GetValidList(List<String> list) {
        List<String> all = new ArrayList<>(list);
        all.remove(player);
        return all;
    }

    public void Check() {
        if (Die()) return;
        if (Menuoi_Die()){
            events.add("Mẹ nuôi đã chết!, cậu bé sẽ trờ thành sói đêm nay");
        }
    }
    private boolean Menuoi_Die(){
        if (Die() || target==null) return false;
        if (wolves.contains(this)) return false;
        return !alive.contains(target);
    }

    public boolean Check_MeNuoi(){
        if (Menuoi_Die()){
            wolves.add(this);
            return true;
        }
        return false;
    }
    public String ChucNangName(){
        if (IsSoi()) return name.equals("Sói")?"[RED]"+name:name+"[RED](Sói)";
        return name+"("+target+")";
    }
}
