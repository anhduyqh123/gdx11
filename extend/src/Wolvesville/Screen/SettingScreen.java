package Wolvesville.Screen;

import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ILabel;
import GDX11.IObject.IActor.ITable;
import GDX11.Screen;
import Wolvesville.Global;
import Wolvesville.Model.Card;

public class SettingScreen extends Screen implements Global {
    private final ITable iTable = FindITable("table");
    private int soiCount = 4;
    public SettingScreen(Runnable next) {
        super("Setting");
        InitSoi();
        InitTable();
        Click("btReset",this::InitTable);
        Click("btNext",()->{
            Hide();
            next.run();
        });
        FindILabel("lbList").iParam.Set("count",allName.size());
        SetSoiCount(soiCount);
    }
    private void InitTable(){
        func.clear();
        func.addAll(allFunc);
        iTable.CloneChild(func,this::InitFunc);
    }
    private void InitFunc(Card fc, IGroup iGroup){
        iGroup.FindILabel("lb").SetText(fc.name);
        iGroup.FindIActor("btDelete").Click(()->{
            func.remove(fc);
            iGroup.GetActor().setVisible(false);
            iTable.RefreshGrid();
        });
    }
    private void InitSoi(){
        IGroup group = FindIGroup("soi");
        ILabel lbNum = group.FindILabel("lbNum");
        lbNum.SetText(soiCount);
        group.FindIActor("btAdd").Click(()->{
            soiCount++;
            lbNum.SetText(soiCount);
            SetSoiCount(soiCount);
        });
        group.FindIActor("btSub").Click(()->{
            soiCount--;
            if (soiCount<0) soiCount=0;
            lbNum.SetText(soiCount);
            SetSoiCount(soiCount);
        });
    }
}
