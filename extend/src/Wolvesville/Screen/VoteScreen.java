package Wolvesville.Screen;

import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Screen;
import Wolvesville.Global;

public class VoteScreen extends Screen implements Global {
    private final ITable iTable = FindITable("table");
    private Runnable next;
    public VoteScreen(Runnable next) {
        super("PlayerVote");
        this.next = next;
        iTable.CloneChild(alive,this::InitName);
        FindILabel("lbList").iParam.Set("count",alive.size());
        Click("btNext",()->{
            Hide();
            next.run();
        });
    }
    private void InitName(String name, IGroup iGroup){
        iGroup.FindILabel("lb").SetText(name);
        iGroup.FindIActor("btVote").Click(()->{
            votedPlayer.Set(name);
            Hide();
            next.run();
        });
    }
}
