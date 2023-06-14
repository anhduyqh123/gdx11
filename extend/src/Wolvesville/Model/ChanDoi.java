package Wolvesville.Model;

public class ChanDoi extends Card{
    public ChanDoi() {
        super("Chán đời");
    }
    public boolean Win(){
        if (cupid.player1.contains(player)) return false;
        if (cupid.player2.contains(player)) return false;
        return voted.contains(player);
    }
}
