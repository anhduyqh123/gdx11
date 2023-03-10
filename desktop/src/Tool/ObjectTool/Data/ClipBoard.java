package Tool.ObjectTool.Data;

import GDX11.IObject.IObject;
import GDX11.Util;
import Tool.JFrame.UI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ClipBoard {
    public static ClipBoard i;
    private JComboBox cb;
    private List<IObject> list = new ArrayList<>();

    public ClipBoard(JComboBox cb)
    {
        i = this;
        this.cb = cb;
    }

    public void Select(List<IObject> all)
    {
        list.clear();
        list.addAll(all);
        String[] arr = new String[list.size()];
        Util.For(0,arr.length-1,i->arr[i]=list.get(i).name);
        UI.ComboBox(i.cb,arr);
    }
    public List<IObject> GetObjects()
    {
        return list;
    }
}
