package Wolvesville.Model;

import java.util.ArrayList;
import java.util.List;

public class ThoiSao extends Card {
    public List<String> list = new ArrayList<>();
    public String player1,player2;
    public ThoiSao() {
        super("Thổi sáo");
    }
    public void SetPair(int id,String player){
        if (id==1) player1 = player;
        else player2 = player;
    }
    public void Thoi(){
        list.add(player1);
        list.add(player2);
    }
}
