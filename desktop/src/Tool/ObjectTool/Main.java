package Tool.ObjectTool;

import GDX11.Config;
import Tool.JFrame.Frame;
import Tool.ObjectTool.Data.MyGame;
import Tool.ObjectTool.Form.MainForm;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static MainForm mainForm = new MainForm();
    public static void main (String[] arg) {
        SwingUtilities.invokeLater(()->{
            JFrame frame = Frame.New("ui editor");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocation(0,0);

            frame.add(mainForm.panel1);
            frame.pack();
        });
        LoadGame();
    }
    private static void LoadGame()
    {
        Config.data = Config.LoadData(new FileHandle("config.json").readString());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Config.Get("screen_width",360);
        config.height = Config.Get("screen_height",720);
        config.x = screenSize.width- config.width;
        new LwjglApplication(new MyGame(mainForm::Install), config);
    }
}
