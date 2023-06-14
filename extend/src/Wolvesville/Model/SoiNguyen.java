package Wolvesville.Model;

public class SoiNguyen extends HumanX{
    private boolean nguyen;
    public SoiNguyen() {
        super("Sói Nguyền", "Muốn nguyền");
    }
    public boolean DaNguyen(){
        return nguyen;
    }

    @Override
    public void Run() {
        if (target!=null && !nguyen){
            events.add(target+" đã bị nguyền");
            nguyen = true;
            map.get(target).biSoiCan = false;
        }
    }

    public boolean Nguyen_Soi(){
        if (Die() || target==null) return false;
        Card card = map.get(target);
        if (wolves.contains(card) || card.Die()) return false;
        wolves.add(card);
        return true;
    }
    public boolean Can_Nguyen(){
        if (Die() || DaNguyen()) return false;
        if (BiVoHieu()) return false;
        return true;
    }
}
