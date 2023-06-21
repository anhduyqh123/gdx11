package Wolvesville.Model;

public class ChanDoi extends Card{
    public ChanDoi() {
        super("Chán đời");
    }
    public boolean Win(){
        if (!cupid.Valid()) return false;
        if (player==null) return false;
        return cupid.ChanDoi_Win(player) && voted.contains(player);
    }

    @Override
    protected boolean DanDen() {
        return false;
    }
}
