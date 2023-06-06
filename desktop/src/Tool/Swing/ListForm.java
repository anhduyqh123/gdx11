package Tool.Swing;

import javax.swing.*;
import java.util.List;

public class ListForm {
    public JPanel panel1;
    private JTree tree1;
    public ListForm(List<String> data)
    {
        GList2 gList = new GList2(tree1);
        gList.SetData(data);
        gList.SetRoot("root");
    }
}
