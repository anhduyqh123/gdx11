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
    public String GetPlayer(int id){
        return id==1?player1:player2;
    }

    @Override
    public void Reset() {
        super.Reset();
        player1 = null;
        player2 = null;
    }

    @Override
    public boolean Valid() {
        return player!=null && player1!=null && player2!=null && !player1.equals(player2);
    }

    public void Check(){
        if (willDead.contains(player1)||willDead.contains(player2))
        {
            willDead.add(player1);
            willDead.add(player2);
            events.add("Cặp đôi cupid "+player1+","+player2+" chết");
        }
    }
    public boolean PheThu3(){
        if (!alive.contains(player1)) return false;
        return map.get(player1).IsSoi() && !map.get(player2).IsSoi()
                || map.get(player2).IsSoi() && !map.get(player1).IsSoi();
    }
    public boolean Contains(String name){
        return name.equals(player1) || name.equals(player2);
    }
}
