package DrinkGame.Base;

import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IScrollPane;
import GDX11.IObject.IActor.ITable;
import GDX11.Screen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuScreen extends Screen {
    public static MenuScreen i;
    private final Map<String,Runnable> btMap = new HashMap<>();
    public MenuScreen() {
        super("Menu");
        i = this;
    }
    public void NewGame(String name,Runnable cb){
        btMap.put(name,()->{
            SetEndHideEvent(cb);
            Hide();
        });
    }
    public void Show(List<String> list){
        IScrollPane scroll = FindIActor("scroll");
        ITable table = FindITable("table");
        table.CloneChild(list,this::CreateGameButton);
        table.GetActor().setSize(table.GetTable().getPrefWidth(),table.GetTable().getPrefHeight());
        FindActor("content").setHeight(table.GetActor().getHeight()+1100);
        FindIGroup("content").Runnable(a->a.iPos.Refresh());
        scroll.SetPercentY(1,true);
        scroll.SetPercentY(0,false);
        Show();
    }
    private void CreateGameButton(String game, IGroup iGroup){
        iGroup.FindIImage("img").texture = "icon_"+game;
        iGroup.Refresh();
        iGroup.Click(btMap.get(game));
    }
}
