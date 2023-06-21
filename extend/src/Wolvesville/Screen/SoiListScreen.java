package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Screen;
import Wolvesville.Global;
import Wolvesville.Model.Card;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoiListScreen extends BaseScreen implements Global {
    private Map<Card,IDropDown> dropMap;
    private int count;
    public SoiListScreen(int count) {
        super("SoiList",new Card());
        this.count = count;
    }
    private void InitSoi(Card soi, IGroup iGroup){
        IDropDown dropDown = iGroup.iComponents.GetIComponent("dropDown");
        iGroup.GetActor().setTouchable(soi.Empty()? Touchable.enabled:Touchable.disabled);
        dropDown.onSelect = vl->{
            soi.SetPlayer(vl);
            map.put(vl,soi);
            RefreshList();
            //FindActor("btNext").setVisible(Valid());
            RefreshInfo();
        };
        dropMap.put(soi,dropDown);
        dropDown.SetItems(leftName);
        dropDown.SetView(soi.Empty()?"?":soi.player);
    }
    private void CheckNew(){
        if (!bansoi.BeCome_Soi()) return;
        FindActor("lbInfo").setVisible(true);
        FindActor("lbInfo").setColor(Color.WHITE);
        FindILabel("lbInfo").SetText("[ORANGE]gọi [BLUE]"+bansoi.player+"[ORANGE] thức dậy chung với bầy sói!");
    }
    private void SoiNguyenNew(){
        if (!soinguyen.Nguyen_Soi()) return;
        FindActor("lbInfo").setVisible(true);
        FindActor("lbInfo").setColor(Color.WHITE);
        FindILabel("lbInfo").SetText("[ORANGE]gọi [BLUE]"+soinguyen.target+"[ORANGE] thức dậy chung với bầy sói!");
    }
    private void ChubeNew(){
        if (!chube.Check_MeNuoi()) return;
        FindActor("lbInfo").setVisible(true);
        FindActor("lbInfo").setColor(Color.WHITE);
        FindILabel("lbInfo").SetText("[ORANGE]gọi [BLUE]"+chube.player+"[ORANGE] thức dậy chung với bầy sói!");
    }
    private void RefreshList(){
        List<String> names = new ArrayList<>();
        for (Card soi : wolves)
            if (soi.Valid()) names.add(soi.player);
        for (Card soi : dropMap.keySet()){
            IDropDown drop = dropMap.get(soi);
            List<String> l = new ArrayList<>(leftName);
            l.removeAll(names);
            if (soi.Valid()) l.add(0,soi.player);
            drop.SetItems(l);
        }
    }
    private boolean Valid(){
        for (Card soi : wolves)
            if (soi.Empty()) return false;
        return true;
    }
    private void RefreshInfo(){
        Card soi = SoiVoHieu();
        FindActor("lbInfo").setVisible(soi!=null);
        if (soi==null) return;
        FindIActor("lbInfo").iParam.Set("xname",soi.player);
    }
    private Card SoiVoHieu(){
        for (Card soi : wolves)
            if (soi.BiVoHieu()) return soi;
        return null;
    }
    private boolean CanBite(){
        if (wolves.size()!=1) return true;
        return !wolves.get(0).equals(SoiVoHieu());
    }

    @Override
    public void Init(Card fc) {
        dropMap = new HashMap<>();
        RefreshInfo();
        CheckNew();
        SoiNguyenNew();
        ChubeNew();

        ITable iTable = FindIGroup("group").FindITable("table");
        iTable.CloneChild(wolves,this::InitSoi);

        FindILabel("lbList").iParam.Set("count",wolves.size());
        IGroup cbNguyen = FindIGroup("cbNguyen");

        FindActor("btNext").setVisible(false);
        IDropDown dropDown = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");
        dropDown.onSelect = vl->{
            boolean bite = CanBite();
            int index = dropDown.GetIndexSelected();
            if (count<=0) targetSoi.clear();
            if (bite && index>0) targetSoi.add(vl);
            FindActor("btNext").setVisible(Valid());
            cbNguyen.GetActor().setVisible(soinguyen.Can_Nguyen() && targetSoi.size()>0);
        };
        List<String> list = new ArrayList<>(alive);
        list.add(0,"Không");
        dropDown.SetItems(list);
        dropDown.SetView("?");

        cbNguyen.GetActor().setVisible(false);

        BtNext(this,()->{
            for (Card soi : wolves)
                leftName.remove(soi.player);
            List<String> target = new ArrayList<>(targetSoi);
            if (cbNguyen.iParam.Get("check",false))
                soinguyen.SetTarget(target.get(0));
        });
        Click("btInfo",()->new PlayerInfoScreen().Show());
        SetBack(this);

        Click("btNext2",()->{
            Hide();
            Screen screen = new SoiListScreen(count+1);
            SetNext(screen,iGroup.iParam.GetRun("next"));
            screen.Show();
        });
    }
    public void SetNext(){
        if (soitrang.Die()) return;
        Runnable next = iGroup.iParam.GetRun("next");
        if (day.Get()%2==1) SetNext(this,()->ShowSoiTrang(next));
    }
    private void ShowSoiTrang(Runnable next){
        SoiTrangScreen screen = new SoiTrangScreen();
        SetNext(screen,next);
        screen.Show();
    }
}
