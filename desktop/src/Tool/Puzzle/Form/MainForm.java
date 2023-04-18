package Tool.Puzzle.Form;

import GDX11.GDX;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainForm {
    public JPanel panel1;
    private JTabbedPane tabbedPane;

    public MainForm()
    {
        ShapeForm shapeForm = new ShapeForm();
        tabbedPane.addChangeListener(changeEvent -> {
            System.out.println(changeEvent.toString());
        });
        tabbedPane.add("Shape", shapeForm.panel1);
    }
}
