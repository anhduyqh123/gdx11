package Wolvesville.Model;

public class Cupid extends Card {
    public String player1,player2;
    public boolean unPair;
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
        unPair = false;
        player1 = null;
        player2 = null;
    }

    @Override
    public boolean Valid() {
        return player!=null && player1!=null && player2!=null && !player1.equals(player2);
    }

    public void Check(){
        if (!unPair) unPair = UnPair();
        if (eventMap.containsKey(cathangtu)) return;
        if (unPair) return;
        if (willDead.contains(player1)||willDead.contains(player2))
        {
            willDead.add(player1);
            willDead.add(player2);
            events.add("Cặp đôi cupid "+player1+","+player2+" chết");
            thosan.Check();
        }
    }
    private boolean UnPair(){
        if (alive.contains(player1) && !alive.contains(player2)) return true;
        return !alive.contains(player1) && alive.contains(player2);
    }
    public boolean PheThu3(){
        if (unPair) return false;
        if (eventMap.containsKey(cathangtu)) return false;
        if (!alive.contains(player1)) return false;
        return map.get(player1).IsSoi() && !map.get(player2).IsSoi()
                || map.get(player2).IsSoi() && !map.get(player1).IsSoi();
    }
    public boolean ChanDoi_CanWin(String player){
        if (!Valid()) return false;
        if (Contains(player)) return unPair;
        return true;
    }
    public boolean Contains(String name){
        return name.equals(player1) || name.equals(player2);
    }

    @Override
    protected boolean DanDen() {
        return false;
    }
}
