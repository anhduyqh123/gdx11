package Wolvesville.Model;

import GDX11.GDX;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Card {
    public static final List<String> events = new ArrayList<>();
    public static final HashSet<String> willDead = new HashSet<>();
    public static GDX.Func1<Card,String> getCard;

    public String name = "Dân đen";
    public String player;
    public String wantTo;
    public boolean biSoiCan;
    public boolean dcbaove;
    public Card(){}
    public Card(String name){
        this.name = name;
    }
    public void SetPlayer(String player){
        this.player = player;
    }
    public boolean Empty(){
        return player==null;
    }

    public void Morning(){
        if (dcbaove) biSoiCan = false;
        if (biSoiCan) willDead.add(player);
    }
    public void SoiCan(){
        biSoiCan = true;
        events.add("Sói cắn "+player);
        if (dcbaove) events.add(player+" Được bảo vệ");
    }
    public void PhuThuySave(){
        biSoiCan = false;
        events.add("Phù thủ cứu "+player);
    }
    public void DuocBaoVe(){
        dcbaove = true;
    }
    public void PhuThuyKill(){
        willDead.add(player);
        events.add("Phù thủy giết "+player);
    }
}
