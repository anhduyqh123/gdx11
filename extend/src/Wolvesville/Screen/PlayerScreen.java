package Wolvesville.Screen;

import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Screen;

import java.util.Arrays;
import java.util.List;

public class PlayerScreen extends Screen {
    private static final List<String> all = Arrays.asList("Cường","DuyBa","DuyTran","Thiện","Phúc Stark","Phúc Niểng","Đạt",
            "KimMai","Hương","Tuấn","Hằng","Hà","Bình","Khanh","Danh","Vũ","Hưng","MaiKa","My","BảoNgọc","Kha","Phước","Minh");
    private List<String> list;
    private final ITable iTable = FindITable("table");
    public PlayerScreen(List<String> list,Runnable next) {
        super("PlayerList");
        this.list = list;
        InitTable();
        Click("btReset",this::InitTable);
        Click("btNext",()->{
            Hide();
            next.run();
        });
    }
    private void InitTable(){
        list.clear();
        list.addAll(all);
        iTable.CloneChild(list,this::InitName);
        FindILabel("lbList").iParam.Set("count",list.size());
    }
    private void InitName(String name, IGroup iGroup){
        iGroup.FindILabel("lb").SetText(name);
        iGroup.FindIActor("btDelete").Click(()->{
            list.remove(name);
            FindILabel("lbList").iParam.Set("count",list.size());
            iGroup.GetActor().setVisible(false);
            iTable.RefreshGrid();
        });
    }
}
