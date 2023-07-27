package Tool.Swing;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.List;

public class ListForm {
    public JPanel panel1;
    protected JTree tree1;
    protected JScrollPane scroll;
    public GList2 gList = NewGList();

    public ListForm(String name,List<String> data)
    {
        TitledBorder titledBorder = (TitledBorder)scroll.getBorder();
        titledBorder.setTitle(name);
        gList.SetData(data);
    }
    protected GList2 NewGList(){
        return new GList2(tree1);
    }
}
