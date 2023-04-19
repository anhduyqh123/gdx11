package Tool.Puzzle;

import GDX11.GDX;
import Tool.JFrame.UI;
import Tool.Puzzle.Form.MainForm;

import javax.swing.*;

public class Puzzle {
    public static void main (String[] arg) {
        SwingUtilities.invokeLater(()-> UI.NewJFrame("ui editor",new MainForm().panel1, GDX::Exit));
    }
}
