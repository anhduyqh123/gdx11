package Wolvesville;

import GDX11.GDX;
import GDX11.Screen;
import Wolvesville.Model.Card;
import Wolvesville.Model.HumanX;
import Wolvesville.Screen.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Wolves1 implements Global1{

    public Wolves1(){
        Card.getCard = map::get;

        new PlayerScreen(allName,()->{
            new SettingScreen(this::FirstSetting).Show();
        }).Show();
    }
    private void FirstSetting(){
        InitPlayers();
        leftName.addAll(allName);
        AskPlayer();
    }
    private void SetVaiTro(){
        for (Card c : cards){
            if (!c.Empty()) continue;
            c.SetPlayer(leftName.get(0));
            leftName.remove(c.player);
            map.put(c.player,c);
        }
    }
    private void Morning(){
        SetVaiTro();
        for (String player : alive) map.get(player).Morning();
        cupid.Check();

        Screen screen = new GameScreen();
        screen.onHide = ()->{};//next
        screen.Show();
    }
    private void AskSoi(){
        List<Card> list = Arrays.asList(phuthuy,tientri,thosan,thoisao,silence);
        Screen screen = new SoiListScreen();
        screen.onHide = ()->AskPlayer(0,list,this::Morning);
        screen.Show();
    }
    private void AskPlayer(){
        List<Card> list = Arrays.asList(cupid,chandoi,bansoi,gialang,hiepsi,nguyetnu,baove);
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
        screen.onHide = ()->AskPlayer(index+1,list,done);
        screen.Show();
    }
    private Screen GetScreen(Card fc){
        if (fc.equals(tientri)) return new TienTriScreen();
        if (fc.equals(thoisao)) return new ThoiSaoScreen();
        if (fc.equals(phuthuy)) return new PhuThuyScreen();
        if (fc.equals(cupid)) return new CupidScreen();
        if (fc instanceof HumanX) return new HumanXScreen(fc);
        return new AskPlayerScreen(fc);
    }
}
