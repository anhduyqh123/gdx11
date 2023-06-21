package Wolvesville.Model;

import Wolvesville.Global;

public class Card implements Global {
    public String name = "Dân đen";
    public String player;
    public String wantTo;
    public boolean biSoiCan;
    public boolean dcbaove;
    public boolean vohieu,die;
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
        return alive.contains(player) && !die;
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
        die = false;
    }

    public void Run(){
        //thực hiện chức năng
    }
    public void Morning(){
        if (biSoiCan) willDead.add(player);
    }
    public void BiSoiCan(){
        if (NghichLua()){
            if (DanDen()){
                wolves.add(this);
                events.add("Sói cắn "+player+" là "+name);
                events.add("Nghịch lửa:"+player+" thành sói");
                return;
            }
            events.add("Nghịch lửa: sói cắn trúng "+player+" là "+name);
            eventMap.put(nghichluaX,true);
            return;
        }
        biSoiCan = true;
        if (soitrang.IsCan(player)) events.add("Sói trắng cắn "+player);
        else events.add("Sói cắn "+player+" là "+name);
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
    protected boolean NghichLua(){
        return eventMap.containsKey(nghichlua);
    }
    protected boolean DanDen(){
        return true;
    }

    public boolean PhuThuy_Cuu(){
        if (NghichLua()) return false;
        if (soinguyen.Se_Nguyen() && soinguyen.target.equals(player)) return false;
        if (baove.IsCover(player)) return false;
        return true;
    }
    public void Swap(Card card){
        if (Die() || card.Die()) return;
        String temp = player;
        player = card.player;
        card.player = temp;
        map.put(player,this);
        map.put(card.player,card);
    }
    public void Dead(){
        die = true;
        alive.remove(player);
        wolves.remove(this);
    }
}
