package Tool.ObjectTool.Form;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TestForm {
    private JPanel panel1;
    private JPopupMenu menu;

    public TestForm()
    {
        System.out.println("123");
        panel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menu.show(panel1,e.getX(),e.getY());
            }
        });
    }
}
