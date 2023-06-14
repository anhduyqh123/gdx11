package Wolvesville.Screen;

import Extend.IDropDown;
import Wolvesville.Global;
import Wolvesville.Model.Card;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.List;

public class ThoiSaoScreen extends BaseScreen implements Global {
    private IDropDown dropDown,dropDown1,dropDown2;
    public ThoiSaoScreen() {
        super("ThoiSao",thoisao);
    }
    private void SetDrop(int index,IDropDown dropDown,List<String> list){
        dropDown.onSelect = vl->{
            thoisao.SetPair(index,vl);
            FindActor("btNext").setVisible(thoisao.Die() || thoisao.Valid());
        };
        dropDown.SetItems(list);
        dropDown.SetView("?");
    }
    private void Refresh(){
        List<String> list = thoisao.GetList();
        SetDrop(1,dropDown1,list);
        SetDrop(2,dropDown2,list);
    }

    @Override
    public void Init(Card fc) {
        FindActor("lbInfo").setVisible(thoisao.BiVoHieu());

        dropDown = FindIGroup("dropDown0").iComponents.GetIComponent("dropDown");
        dropDown1 = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");
        dropDown2 = FindIGroup("dropDown2").iComponents.GetIComponent("dropDown");
        FindActor("dropDown0").setTouchable(thoisao.Empty()? Touchable.enabled:Touchable.disabled);

        dropDown.onSelect = vl->{
            thoisao.SetPlayer(vl);
            map.put(vl,thoisao);
            FindActor("btNext").setVisible(thoisao.Valid());
            Refresh();
            FindActor("lbInfo").setVisible(thoisao.BiVoHieu());
        };
        dropDown.SetItems(leftName);
        dropDown.SetView(thoisao.Empty()?"?":thoisao.player);
        dropDown1.SetView("?");
        dropDown2.SetView("?");
        thoisao.NewTurn();
        Refresh();

        Click("btNext",()->{
            thoisao.Thoi();
            leftName.remove(thoisao.player);
            ThoiNienListScreen listScreen = new ThoiNienListScreen();
            SetNext(listScreen,iGroup.iParam.GetRun("next"));
            this.onHide = listScreen::Show;
            Hide();
        });

        FindActor("btNext").setVisible(false);
        CheckDie(thoisao,this);

        Click("btInfo",()->new PlayerInfoScreen().Show());
    }
}
