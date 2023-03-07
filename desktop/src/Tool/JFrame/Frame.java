package Tool.JFrame;

import javax.swing.*;

public class Frame {
    public static JFrame New(String name)
    {
        JFrame frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        return frame;
    }
}
