package Tool.Puzzle;

import Tool.JFrame.UI;
import Tool.Puzzle.Core.MyGame;
import Tool.Puzzle.Form.MainForm;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import javax.swing.*;
import java.awt.*;

public class Puzzle {
    private static MainForm mainForm = new MainForm();

    public static void main (String[] arg) {
        SwingUtilities.invokeLater(()->{
            JFrame frame = UI.NewJFrame("ui editor");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocation(0,0);

            frame.add(mainForm.panel1);
            frame.pack();
        });
        LoadGame();
    }
    private static void LoadGame()
    {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 720;
        config.height = 1280;
        config.x = width/2;
        new LwjglApplication(new MyGame(), config);
    }
}
