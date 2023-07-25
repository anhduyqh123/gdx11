package Wolvesville;

import Extend.IDropDown;
import GDX11.Config;
import GDX11.GDX;
import GDX11.Screen;
import GDX11.Util;
import Wolvesville.Model.*;
import Wolvesville.Screen.BaseScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.*;

public interface Global {
    Cupid cupid = new Cupid();
    TienTri tientri = new TienTri();
    PhuThuy phuthuy = new PhuThuy();
    ThoiSao thoisao = new ThoiSao();
    GiaLang gialang = new GiaLang();
    BaoVe baove = new BaoVe();
    NguyetNu nguyetnu = new NguyetNu();
    BanSoi bansoi = new BanSoi();
    ThoSan thosan = new ThoSan();
    Silence silence = new Silence();
    ChanDoi chandoi = new ChanDoi();
    HiepSi hiepsi = new HiepSi();
    SoiNguyen soinguyen = new SoiNguyen();
    Chube chube = new Chube();
    SoiTrang soitrang = new SoiTrang();

    List<Card> allFunc = Arrays.asList(cupid,tientri,baove,thosan,phuthuy,gialang,thoisao,bansoi,hiepsi,chandoi,nguyetnu,silence,soinguyen,chube,soitrang);
    List<Card> func = new ArrayList<>();

    List<Card> wolves = new ArrayList<>();
    List<Card> cards = new ArrayList<>();

    List<String> allName = new ArrayList<>();//name
    List<String> leftName = new ArrayList<>();
    List<String> alive = new ArrayList<>();
    List<String> voted = new ArrayList<>();
    Map<String, Card> map = new HashMap<>();
    //GDX.Ref<String> targetSoi = new GDX.Ref<>();
    HashSet<String> targetSoi = new HashSet<>();
    //GDX.Ref<String> votedPlayer = new GDX.Ref<>();
    HashSet<String> voting = new HashSet<>();

    List<String> events = new ArrayList<>();
    HashSet<String> willDead = new HashSet<>();
    HashSet<String> nextDie = new HashSet<>();
    List<BaseScreen> screens = new ArrayList<>();
    GDX.Ref<Integer> day = new GDX.Ref<>(0);

    String kytich = "kỳ ",nghichlua="nghịch lửa",cathangtu="cá tháng 4",laygo="lấy gỗ",
            nghichluaX = "trúng chức năng",soicanKiemGi="Sói cắn kiếm gỉ";
    Map<String,Boolean> eventMap = new HashMap<>();

    default void InitPlayers(){
        day.Set(0);
        for (Card card : allFunc) card.Reset();
        wolves.clear();
        cards.clear();
        alive.clear();

        Util.For(0,GetSoiCount()-1,i->wolves.add(new Card("Sói")));
        if (soinguyen.Active()){
            wolves.remove(wolves.size()-1);
            wolves.add(0,soinguyen);
        }
        if (soitrang.Active()){
            wolves.remove(wolves.size()-1);
            wolves.add(0,soitrang);
        }

        cards.addAll(func);
        cards.addAll(wolves);
        //int total = allName.size()- cards.size();
        //Util.For(0,total-1,i-> cards.add(new Card()));
        alive.addAll(allName);
        voted.clear();
    }
    default void SetMainDropdown(Card fc, IDropDown iDropDown, Runnable onSelect){

        iDropDown.GetActor().setTouchable(fc.Empty()? Touchable.enabled:Touchable.disabled);
        iDropDown.onSelect = vl->{
            fc.SetPlayer(vl);
            map.put(vl,fc);
            onSelect.run();
            //btNext.setVisible(fc.Valid());
        };
        iDropDown.SetItems(leftName);
        iDropDown.SetView(fc.Empty()?"?":fc.player);
    }
    default void CheckDie(Card fc, Screen screen){
        if (!fc.die || fc.Empty()) return;
        screen.FindActor("btNext").setVisible(true);
        screen.FindActor("lbInfo").setColor(Color.RED);
        screen.FindILabel("lbInfo").SetText("Đã chết!!!");
        screen.FindActor("lbInfo").setVisible(true);
    }
    default void CheckGiaLangDie(Card fc, Screen screen){
        if (fc.Empty() || !fc.VoHieuBoiGiaLangDie()) return;
        screen.FindActor("btNext").setVisible(true);
        screen.FindActor("lbInfo").setColor(Color.RED);
        screen.FindILabel("lbInfo").SetText("Già làng chết, chức năng bị vô hiệu");
        screen.FindActor("lbInfo").setVisible(true);
    }
    default void CheckForcedTarget(HumanX fc, Screen screen){
        if (fc.Empty() || !fc.ForcedTarget()) return;
        screen.FindActor("lbInfo").setColor(Color.YELLOW);
        screen.FindILabel("lbInfo").SetText("Ván trước đã chọn "+fc.target);
        screen.FindActor("lbInfo").setVisible(true);
    }

    default void SetSoiCount(int count){
        Config.i.Set("soiCount",count);
    }
    default int GetSoiCount(){
        return Config.i.Get("soiCount");
    }
    default void BackScreen(){
        BaseScreen screen = screens.get(screens.size()-1);
        screen.OnBackScreen();
        screen.Init(screen.fc);
        screen.Show();
    }
    default void SetBack(BaseScreen screen){
        if (screens.contains(screen)) return;
        screen.FindActor("btBack").setVisible(screens.size()>0);
        screens.add(screen);
        screen.Click("btBack",()->{
            screen.Hide();
            screens.remove(screen);
            screen.OnBackScreen();
            BackScreen();
        });
    }
    default void SetNext(Screen screen,Runnable next){
        screen.GetIGroup().iParam.SetRun("next",next);
    }
    default void BtNext(Screen screen,Runnable cb){
        screen.Click("btNext",()->{
            cb.run();
            screen.Hide();
            screen.GetIGroup().iParam.GetRun("next").run();
        });
    }
}
