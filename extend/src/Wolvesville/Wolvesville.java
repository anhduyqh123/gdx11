package Wolvesville;

import GDX11.Screen;
import Wolvesville.Screen.AskPlayerScreen;
import Wolvesville.Screen.CupidScreen;
import Wolvesville.Screen.PairScreen;
import Wolvesville.Screen.PlayerScreen;

import java.util.*;

public class Wolvesville {
    private List<String> all = new ArrayList<>();
    private List<String> left = new ArrayList<>();
    private List<String> human = new ArrayList<>();
    private List<String> wolves = new ArrayList<>();
    private List<String> pair = new ArrayList<>();
    private String cupid = "CUPID",tientri="TIÊN TRI",baove = "BẢO VỆ",thosan = "THỢ SĂN",
            phuthuy = "PHÙ THỦ",gialang = "GIÀ LÀNG",thoisao = "THỔI SÁO",bansoi = "BÁN SÓI",hiepsi = "HIỆP SĨ",
            chandoi = "CHÁN ĐỜI",nguyetnu="NGUYỆT NỮ",silence = "SILENCE";
    private Map<String,String> map = new HashMap<>();

    public Wolvesville(){
        new PlayerScreen(all,this::AskPlayer).Show();
    }
    private void AskPlayer(){
        left.addAll(all);
        List<String> list = Arrays.asList(cupid,chandoi,bansoi,gialang,hiepsi,nguyetnu,baove);
        AskPlayer(0,list);
    }
    private void AskPlayer(int index,List<String> list){
        if (index>=list.size()) return;
        String name = list.get(index);
        Screen screen = GetScreen(name);
        screen.onHide = ()-> AskPlayer(index+1,list);
        screen.AddClick("btNext",screen::Hide);
        screen.Show();
    }
    private Screen GetScreen(String name){
        if (name.equals(cupid)) return new CupidScreen(name,map,pair,all);
        return new AskPlayerScreen(name,left,vl->map.put(name,vl));
    }
}
