package Wolvesville;

import GDX11.Screen;
import Wolvesville.Model.Card;
import Wolvesville.Model.HumanX;
import Wolvesville.Model.SoiNguyen;
import Wolvesville.Screen.*;

import java.util.*;

public class Wolves implements Global {

    public Wolves(){
        NewGame();
    }
    private void NewGame(){
        allName.clear();
        new PlayerScreen(allName,()->{
            new SettingScreen(this::FirstSetting).Show();
        }).Show();
    }
    private void FirstSetting(){
        InitPlayers();
        leftName.clear();
        leftName.addAll(allName);
        AskPlayer();
    }
    private void SetVaiTro(){
        for (String name : leftName){
            Card card = new Card();
            card.SetPlayer(name);
            map.put(name,card);
            cards.add(card);
        }
        leftName.clear();
    }
    private void Morning(){
        SetVaiTro();
        if (targetSoi.size()>0){
            for (String player : targetSoi)
                map.get(player).BiSoiCan();
        }
        else events.add("sói không cắn ai");
        for (String player : alive) map.get(player).Run();
        for (String player : alive) map.get(player).Morning();
        for (String player : nextDie){
            if (!alive.contains(player)) continue;
            willDead.add(player);
            events.add(player+" chết do là nạn nhân của kiếm gỉ ván");
        }
        nextDie.clear();

        cupid.Check();
        if (hiepsi.Alive() && hiepsi.biSoiCan) eventMap.put(soicanKiemGi,true);
        NightReport();
    }
    private void NightReport(){
        if (eventMap.containsKey(nghichluaX)){
            eventMap.remove(nghichluaX);
            new NghichLuaScreen(this::NightReport).Show();
            return;
        }
        if (eventMap.containsKey(soicanKiemGi)){
            eventMap.remove(soicanKiemGi);
            new KiemGiScreen(this::NightReport).Show();
            return;
        }
        chube.Check();
        Screen screen = new GameScreen(this::StartDay);
        screen.Show();
        ClearData();
    }
    private void DayReport(){
        Screen screen = new GameScreen(this::EndDay);
        screen.FindILabel("lb").iParam.Set("day","Sáng nay");
        screen.Show();
        ClearData();
    }
    private void ClearData(){
        for (String player : willDead)
            map.get(player).Dead();
        willDead.clear();
        events.clear();
        voting.clear();
    }
    private void StartDay(){
        day.Set(day.Get()+1);
        if (thoisao.Win()){
            GameOver("Thổi sáo thắng!, thổi sáo đã thôn miên tất cả dân!");
            return;
        }
        if (cupid.PheThu3() && alive.size()<=3){
            GameOver("Cặp đôi cupid thắng! Quá bá đạo!!!");
            return;
        }
        if (wolves.size()>=alive.size()-wolves.size() && !cupid.PheThu3()){
            GameOver("Bên phe sói thắng cmnr, số lượng sói lớn hơn hoặc bằng dân");
            return;
        }
        new EventScreen(()->{
            new VoteScreen(this::EndDay).Show();
        }).Show();
    }
    private void EndDay(){
        if (voting.size()>0){
            willDead.addAll(voting);
            voted.addAll(voting);
            for (String player : voting)
                events.add(player+" bị dân làng vote chết");
            cupid.Check();

            if (chandoi.Win()){
                GameOver("[GREEN]"+chandoi.player+"[WHITE] là Thằng chán đời đã thắng do dân làng treo cổ ngu");
                return;
            }
            DayReport();
            return;
        }
        if (cupid.PheThu3() && alive.size()<=3){
            GameOver("Cặp đôi cupid thắng! Quá bá đạo!!!");
            return;
        }
        if (cupid.PheThu3()){
            Reset();
            AskPlayer();
            return;
        }
        if (wolves.size()==0){
            GameOver("Sói đã chết hết, Dân thắng!!!");
            return;
        }
        if (wolves.size()>=alive.size()-wolves.size()){
            GameOver("Bên phe sói thắng cmnr, số lượng sói lớn hơn hoặc bằng dân");
            return;
        }
        Reset();
        AskPlayer();
    }
    private void Reset(){
        events.clear();
        targetSoi.clear();
    }
    private void AskSoi(){
        List<Card> list = Arrays.asList(phuthuy,tientri,thosan,thoisao,silence);
        SoiListScreen screen = new SoiListScreen(0);
        SetNext(screen,()->AskPlayer(0,list,this::Morning));
        screen.SetNext();
        screen.Show();
    }
    private void AskPlayer(){
        screens.clear();
        List<Card> list0 = Arrays.asList(cupid,chandoi,bansoi,gialang,hiepsi,soinguyen,chube,soitrang);
        List<Card> list = new ArrayList<>(Arrays.asList(cupid,chandoi,chube,bansoi,soinguyen,soitrang,gialang,hiepsi,baove,nguyetnu));
        if (leftName.size()==0) list.removeAll(list0);

        AskPlayer(0,list,this::AskSoi);
    }
    private void AskPlayer(int index, List<Card> list, Runnable done){
        if (index>=list.size()){
            done.run();
            return;
        }
        Card fc = list.get(index);
        if (!func.contains(fc)){
            AskPlayer(index+1,list,done);
            return;
        }
        Screen screen = GetScreen(fc);
        SetNext(screen,()->AskPlayer(index+1,list,done));
        screen.Show();
    }
    private Screen GetScreen(Card fc){
        if (fc.equals(tientri)) return new TienTriScreen();
        if (fc.equals(thoisao)) return new ThoiSaoScreen();
        if (fc.equals(phuthuy)) return new PhuThuyScreen();
        if (fc.equals(cupid)) return new CupidScreen();
        if (fc.equals(soitrang)) return new AskPlayerScreen(fc);
        if (fc instanceof SoiNguyen) return new AskPlayerScreen(fc);
        if (fc instanceof HumanX) return new HumanXScreen(fc);
        return new AskPlayerScreen(fc);
    }
    private void GameOver(String content){
        Screen screen = new Screen("GameOver");
        screen.FindILabel("lb0").SetText(content);
        screen.Click("btNext",()->{
            screen.Hide();
            NewGame();
        });
        screen.Show();
    }
}
