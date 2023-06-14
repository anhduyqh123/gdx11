package Wolvesville.Model;

public class BanSoi extends Card {
    public boolean thanhSoi;
    public BanSoi() {
        super("Bán sói");
    }

    @Override
    public void Morning() {
        if (IsSoi()){
            super.Morning();
            return;
        }
        if (biSoiCan){
            //wolves.add(this);
            events.add(player+" đã trở thành sói");
            biSoiCan = false;
            thanhSoi = true;
        }
    }
    public boolean BeCome_Soi(){
        if (Die() || !thanhSoi) return false;
        if (wolves.contains(this)) return false;
        wolves.add(bansoi);
        return true;
    }
}
