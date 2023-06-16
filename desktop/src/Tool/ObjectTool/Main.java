package Tool.ObjectTool;

import GDX11.Config;
import GDX11.GDX;
import SDK.SDK;
import Tool.Swing.UI;
import Tool.ObjectTool.Core.MyGame;
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
            JFrame frame = UI.NewJFrame("ui editor",mainForm.panel1, GDX::Exit);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocation(0,0);
        });
        LoadGame();
    }
    private static void LoadGame()
    {
        SDK.SetSDK(SDK.i);
        Config.Init(new FileHandle("config.json").readString());
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Config.i.Get("screen_width",360);
        config.height = Config.i.Get("screen_height",720);
        config.x = width- config.width-50;
        new LwjglApplication(new MyGame(mainForm::Install), config);
    }
}
