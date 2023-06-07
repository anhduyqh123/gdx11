package Wolvesville.Model;

public class Cupid extends Card {
    public String player1,player2;
    public Cupid() {
        super("Cupid");
    }
    public void SetPair(int id,String player){
        if (id==1) player1 = player;
        else player2 = player;
    }
    public void Check(){
        if (willDead.contains(player1)||willDead.contains(player2))
        {
            willDead.add(player1);
            willDead.add(player2);
        }
    }
}
