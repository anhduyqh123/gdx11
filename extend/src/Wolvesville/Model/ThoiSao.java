package Wolvesville.Model;

import java.util.ArrayList;
import java.util.List;

public class ThoiSao extends Card {
    public List<String> list = new ArrayList<>();
    public String player1,player2;
    public ThoiSao() {
        super("Thổi sáo");
    }

    @Override
    public void Reset() {
        list.clear();
        player1 = null;
        player2 = null;
    }
    public void NewTurn(){
        player1 = null;
        player2 = null;
    }

    @Override
    public boolean Valid() {
        if (player==null) return false;
        if (GetList().size()>1) return player1!=null && player2!=null && !player1.equals(player2);
        return player1!=null && player2!=null;
    }

    public void SetPair(int id, String player){
        if (id==1) player1 = player;
        else player2 = player;
    }
    public void Thoi(){
        if (Die() || BiVoHieu()) return;
        if (player1.equals(player2)) list.add(player1);
        else {
            list.add(player1);
            list.add(player2);
        }
    }
    public boolean Win(){
        if (Die()) return false;
        return GetList().size()==0;
    }
    public List<String> GetList(){
        List<String> list1 = new ArrayList<>(alive);
        list1.removeAll(list);
        list1.remove(player);
        return list1;
    }

    @Override
    protected boolean DanDen() {
        return false;
    }
}
