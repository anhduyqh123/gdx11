package Wolvesville;

import Extend.IDropDown;
import GDX11.Config;
import GDX11.GDX;
import GDX11.Util;
import Wolvesville.Model.*;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.*;

public interface Global1 {
    Cupid cupid = new Cupid();
    TienTri tientri = new TienTri();
    PhuThuy phuthuy = new PhuThuy();
    ThoiSao thoisao = new ThoiSao();
    GiaLang gialang = new GiaLang();
    BaoVe baove = new BaoVe();
    HumanX thosan = new HumanX("Thợ săn","Chọn mục tiêu"),
            nguyetnu = new HumanX("Nguyệt nữ","Muốn vô hiệu"), silence = new HumanX("SILENCE","Muốn câm lặng");
    Card bansoi = new Card("Bán sói"),hiepsi = new Card("Hiệp sĩ"),
            chandoi = new Card("Chán đời");

    List<Card> allFunc = Arrays.asList(cupid,tientri,baove,thosan,phuthuy,gialang,thoisao,bansoi,hiepsi,chandoi,nguyetnu,silence);
    List<Card> func = new ArrayList<>();

    List<Soi> wolves = new ArrayList<>();
    List<Card> cards = new ArrayList<>();

    List<String> allName = new ArrayList<>();//name
    List<String> leftName = new ArrayList<>();
    List<String> alive = new ArrayList<>();
    Map<String, Card> map = new HashMap<>();
    GDX.Ref<String> targetSoi = new GDX.Ref<>();

    default void InitPlayers(){
        Util.For(0,GetSoiCount()-1,i->wolves.add(new Soi()));
        cards.addAll(func);
        cards.addAll(wolves);
        int total = allName.size()- cards.size();
        Util.For(0,total-1,i-> cards.add(new Card()));
        alive.addAll(allName);
    }
    default void SetMainDropdown(Card fc, IDropDown iDropDown){
        iDropDown.GetActor().setTouchable(fc.Empty()? Touchable.enabled:Touchable.disabled);
        iDropDown.onSelect = vl->{
            fc.SetPlayer(vl);
            map.put(vl,fc);
        };
        iDropDown.SetItems(leftName);
        iDropDown.SetSelected(leftName.get(0));
    }
    default void SetSoiTarget(String player){
        targetSoi.Set(player);
        map.get(player).SoiCan();
    }

    default void SetSoiCount(int count){
        Config.Set("soiCount",count);
    }
    default int GetSoiCount(){
        return Config.Get("soiCount");
    }
}
