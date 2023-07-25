package Wolvesville.Screen;

import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Screen;
import Wolvesville.Global;

import java.util.ArrayList;
import java.util.List;

public class VoteScreen extends Screen implements Global {
    private final ITable iTable = FindITable("table");
    private Runnable next;
    public VoteScreen(Runnable next) {
        super("PlayerVote");
        this.next = next;
        List<String> all = new ArrayList<>(alive);
        all.removeAll(voting);
        iTable.CloneChild(all,this::InitName);
        FindILabel("lbList").iParam.Set("count",alive.size());
        Click("btNext",()->{
            Hide();
            next.run();
        });
    }
    private void InitName(String name, IGroup iGroup){
        iGroup.FindILabel("lb").SetText(name);
        iGroup.FindIActor("btVote").Click(()->{
            Hide();
            CheckVote(name);
        });
    }
    private void CheckVote(String name){
        voting.add(name);
        if (thosan.Alive() && thosan.player.equals(name)){
            new ThoSanScreen(()->{
                new VoteScreen(next).Show();
            }).Show();
        }
        else new VoteScreen(next).Show();
    }
}
