package Wolvesville.Model;

import Wolvesville.Global;

public class Card implements Global {
    public String name = "Dân đen";
    public String player;
    public String wantTo;
    public boolean biSoiCan;
    public boolean dcbaove;
    public boolean vohieu;
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
    public boolean Valid(){
        return player!=null;
    }
    public boolean Alive(){
        return alive.contains(player);
    }
    public boolean Die(){
        return !Alive();
    }
    public boolean WillDie(){
        return willDead.contains(player);
    }
    public boolean IsSoi(){
        return wolves.contains(this);
    }
    public boolean Active(){
        return func.contains(this);
    }
    public void Reset(){
        player = null;
        biSoiCan=false;
        dcbaove=false;
        vohieu = false;
    }

    public void Run(){
        //thực hiện chức năng
    }
    public void Morning(){
        if (biSoiCan) willDead.add(player);
    }
    public void BiSoiCan(){
        biSoiCan = true;
        events.add("Sói cắn "+player+" là "+name);
    }
    public void PhuThuySave(){
        biSoiCan = false;
        events.add("Phù thủy cứu "+player);
    }
    public void DuocBaoVe(){
        events.add(player+" Được bảo vệ");
        dcbaove = true;
        biSoiCan = false;
    }
    public void PhuThuyKill(){
        willDead.add(player);
        events.add("Phù thủy giết "+player);
    }
    public void VoHieu(){
        vohieu = true;
        events.add("Nguyệt nữ vô hiệu "+player);
    }
    public boolean BiVoHieu(){
        if (player==null) return false;
        return nguyetnu.VoHieu(this);
    }
    public boolean VoHieuBoiGiaLangDie(){
        return false;
    }
    public String ChucNangName(){
        if (IsSoi()) return name.equals("Sói")?"[RED]"+name:name+"[RED](Sói)";
        return name;
    }
    public void Set(Card card){
        player = card.player;;
    }
    public Card Clone(){
        Card card = new Card();
        card.player = player;
        return card;
    }
}
