package Wolvesville.Screen;

import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;
import Wolvesville.Global;
import Wolvesville.Model.Card;

public class EventScreen extends Screen implements Global {
    public EventScreen(Runnable next) {
        super("Event");

        IGroup cbKytich = FindIGroup("event0").FindIGroup("check");
        IGroup cbNghichLuu = FindIGroup("event1").FindIGroup("check");
        IGroup cbThang4 = FindIGroup("event2").FindIGroup("check");
        IGroup cbLaygo = FindIGroup("event3").FindIGroup("check");

        if (eventMap.containsKey(cathangtu)) Swap();

        Click("btNext",()->{
            eventMap.clear();
            Set(kytich,cbKytich);
            Set(nghichlua,cbNghichLuu);
            Set(cathangtu,cbThang4);
            Set(laygo,cbLaygo);
            Run();
            Hide();
            next.run();
        });
    }
    private void Set(String key,IGroup cb){
        boolean vl = cb.iParam.Get("check");
        if (vl) eventMap.put(key,true);
    }
    private void Run(){
        if (eventMap.containsKey(cathangtu)){
            Swap();
            events.add("event cá tháng tư!!!");
        }
        if (eventMap.containsKey(laygo)){
            phuthuy.save=1;
            events.add("phù thủy được hồi bình cứu!");
        }
        if (eventMap.containsKey(kytich)) HoiSinh();
        if (eventMap.containsKey(nghichlua)){
            //events.add("event nghịch lửa sẽ thực hiện đêm nay!!!");
        }
    }

    private void Swap(){
        baove.Swap(gialang);
        phuthuy.Swap(tientri);
    }
    private void HoiSinh(){
        for (String player : targetSoi){
            if (alive.contains(player)) continue;
            //if (soitrang.IsCan(player)) continue;
            Card card = new Card();
            card.SetPlayer(player);
            map.put(player,card);
            cards.add(card);
            events.add(player+" đc hồi sinh!");
            alive.add(player);
        }
    }
}
