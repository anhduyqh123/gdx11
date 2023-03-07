package Tool.ObjectTool.Data;

import GDX11.GDX;
import GDX11.Util;
import Tool.JFrame.UI;

import javax.swing.*;
import java.util.List;

public class ClipBoard {
    public static ClipBoard i;
    private JComboBox cb;
    private List<Object> list;

    public ClipBoard(JComboBox cb)
    {
        i = this;
        this.cb = cb;
    }

    public void Select(List<Object> list, GDX.Func1<String,Object> getName)
    {
        this.list = list;
        String[] arr = new String[list.size()];
        Util.For(0,arr.length-1,i->arr[i]=getName.Run(list.get(i)));
        UI.ComboBox(i.cb,arr);
    }
    public List<Object> GetObjects()
    {
        return list;
    }
}
