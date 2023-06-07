package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.Screen;
import Wolvesville.Global1;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhuThuyScreen extends Screen implements Global1 {
    public PhuThuyScreen() {
        super("PhuThuy");
        IDropDown dropDown = FindIGroup("dropDown0").iComponents.GetIComponent("dropDown");
        SetMainDropdown(phuthuy,dropDown);

        IDropDown dropDown1 = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");
        FindActor("dropDown1").setTouchable(phuthuy.save>0? Touchable.enabled:Touchable.disabled);

        IDropDown dropDown2 = FindIGroup("dropDown2").iComponents.GetIComponent("dropDown");
        List<String> list = new ArrayList<>(Arrays.asList("Không"));
        if (phuthuy.kill>0) list.addAll(alive);
        list.remove(phuthuy.player);
        dropDown2.SetItems(list);
        dropDown2.SetSelected(list.get(0));

        String biCan = targetSoi.Get();
        FindILabel("lb1").iParam.Set("xname",biCan!=null?biCan:"None");

        phuthuy.Reset();
        AddClick("btNext",()->{
            if (dropDown1.GetIndexSelected()==0) phuthuy.Save(targetSoi.Get());
            if (dropDown2.GetIndexSelected()!=0) phuthuy.Kill(dropDown2.selected);
            leftName.remove(phuthuy.player);
            Hide();
        });
    }
}
