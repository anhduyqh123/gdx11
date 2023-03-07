package Tool.ObjectTool;

import Tool.JFrame.Frame;
import Tool.ObjectTool.Data.MyGame;
import Tool.ObjectTool.Form.MainForm;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main (String[] arg) {
        MainForm mainForm = new MainForm();

        SwingUtilities.invokeLater(()->{
            JFrame frame = Frame.New("ui editor");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocation(0,0);

            frame.add(mainForm.panel1);
            frame.pack();
        });


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 360;
        config.height = 720;
        config.x = screenSize.width- 360;
        new LwjglApplication(new MyGame(720,1280,mainForm::Install), config);
    }
}
