package Tool.JFrame;

import GDX11.GDX;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GList<T> {
    private static Map<String,Object> clipBoard = new HashMap<>();
    private JTextField tfName;
    private JList jList;

    //event
    public GDX.Runnable1<T> onSelect;

    public GList(JList jList,JTextField ftName)
    {
        this.jList = jList;
        this.tfName = ftName;
        jList.addListSelectionListener(e->{


        });
    }
    public void SetData()
    {

    }
    private void Refresh()
    {

    }
}
