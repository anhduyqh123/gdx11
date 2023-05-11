package Tool.ParticleViewer;

import GDX11.GDX;
import Tool.Swing.UI;

import javax.swing.*;

public class ParticleViewer {
    public static void main (String[] arg) {
        SwingUtilities.invokeLater(()->{
            JFrame frame = UI.NewJFrame("Particle Viewer",new ViewForm().panel1, GDX::Exit);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }
}
