package Tool.JFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Panel {
    public static JPanel New(int width, int height,JPanel parent)
    {
        JPanel panel = New(new FlowLayout(),width,height);
        parent.add(panel);
        return panel;
    }
    public static JPanel New(LayoutManager layout, int width, int height)
    {
        JPanel panel = new JPanel(layout);
        panel.setPreferredSize(new Dimension(width,height));
        return panel;
    }
    public static void SetBorder(JPanel panel)
    {
        panel.setBorder(new LineBorder(Color.BLACK));
    }
    public static void SetBorder(String name,JPanel panel)
    {
        panel.setBorder(BorderFactory.createTitledBorder(name));
    }

    public static JScrollPane NewScroll(Component view,int width,int height,JPanel parent)
    {
        JScrollPane scroll = new JScrollPane(view);
        scroll.setPreferredSize(new Dimension(width,height));
        parent.add(scroll);
        return scroll;
    }
    public static JTree NewTree()
    {
        JTree tree = new JTree();
        return tree;
    }
    public static JList NewList()
    {
        JList list = new JList();
        return list;
    }
}
