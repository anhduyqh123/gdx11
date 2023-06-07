package Wolvesville;

import GDX11.Screen;
import Wolvesville.Screen.*;

import java.util.*;

public class Wolvesville implements Global {

//    public Wolvesville(){
//        new PlayerScreen(allName,()->{
//            new SettingScreen(this::AskPlayer).Show();
//        }).Show();
//    }
//    private void AskPlayer(){
//        alive.addAll(allName);
//        leftName.addAll(allName);
//        List<String> list = Arrays.asList(cupid,chandoi,bansoi,gialang,hiepsi,nguyetnu,baove);
//        AskPlayer(0,list,this::AskSoi);
//    }
//    private void AskSoi(){
//        List<String> list = Arrays.asList(phuthuy,tientri,thosan,thoisao,silence);
//        Screen screen = new SoiListScreen();
//        screen.onHide = ()->AskPlayer(0,list,this::Morning);
//        screen.Show();
//    }
//    private void Morning(){
//        Screen screen = new GameScreen();
//        screen.onHide = ()->{};//next
//        screen.Show();
//    }
//    private void AskPlayer(int index,List<String> list,Runnable done){
//        if (index>=list.size()){
//            done.run();
//            return;
//        }
//        String name = list.get(index);
//        if (!func.contains(name)){
//            AskPlayer(index+1,list,done);
//            return;
//        }
//        Screen screen = GetScreen(name);
//        screen.onHide = ()->AskPlayer(index+1,list,done);
//        screen.Show();
//    }
//    private final List<String> xxx = Arrays.asList(baove,nguyetnu,thosan,tientri,silence);
//    private Screen GetScreen(String name){
//        if (name.equals(tientri)) return new TienTriScreen();
//        if (name.equals(thoisao)) return new ThoiSaoScreen();
//        if (name.equals(phuthuy)) return new PhuThuyScreen();
//        if (name.equals(cupid)) return new CupidScreen();
//        if (xxx.contains(name)) return new HumanXScreen(name);
//        return new AskPlayerScreen(name);
//    }
}
