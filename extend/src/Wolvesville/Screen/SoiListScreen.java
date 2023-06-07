package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Screen;
import GDX11.Util;
import Wolvesville.Global1;
import Wolvesville.Model.Soi;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.List;

public class SoiListScreen extends Screen implements Global1 {
    private List<String> list = new ArrayList<>();
    public SoiListScreen() {
        super("SoiList");
        Util.For(1,GetSoiCount(),i->list.add(leftName.get(i-1)));

        ITable iTable = FindIGroup("group").FindITable("table");
        iTable.CloneChild(wolves,this::InitSoi);

        FindILabel("lbList").iParam.Set("count",GetSoiCount());

        IDropDown dropDown = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");
        dropDown.onSelect = this::SetSoiTarget;
        dropDown.SetItems(alive);
        dropDown.SetSelected(alive.get(0));

        AddClick("btNext",()->{
            for (Soi soi : wolves)
                leftName.remove(soi.player);
            Hide();
        });
    }
    private void InitSoi(Soi soi, IGroup iGroup){
        int index = wolves.indexOf(soi);
        IDropDown dropDown = iGroup.iComponents.GetIComponent("dropDown");
        iGroup.GetActor().setTouchable(soi.Empty()? Touchable.enabled:Touchable.disabled);
        dropDown.onSelect = vl->{
            list.set(index,vl);
            soi.SetPlayer(vl);
            map.put(vl,soi);

            List<String> left = new ArrayList<>(leftName);
            left.removeAll(list);
            left.add(vl);
            dropDown.SetItems(left);
        };
        String name = list.get(index);
        List<String> left = new ArrayList<>(leftName);
        left.removeAll(list);
        left.add(name);
        dropDown.SetItems(left);
        dropDown.SetSelected(name);
    }
}
