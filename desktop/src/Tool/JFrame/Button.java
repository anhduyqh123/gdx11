package Tool.JFrame;

import GDX11.GDX;

import javax.swing.*;

public class Button {

    public static JButton New(String name, JPanel parent,GDX.Runnable cb)
    {
        JButton bt = new JButton(name);
        bt.addActionListener(a->cb.Run());
        parent.add(bt);
        return bt;
    }
}
