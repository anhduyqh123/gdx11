package Tool.ObjectToolV2;

import GDX11.Config;
import GDX11.GDX;
import SDK.SDK;
import Tool.Swing.UI;
import Tool.ObjectToolV2.Form.MainForm;
import com.badlogic.gdx.files.FileHandle;

import javax.swing.*;

public class Main2 {

    public static void main (String[] arg) {
        SwingUtilities.invokeLater(()->{
            SDK.SetSDK(SDK.i);
            Config.Init(new FileHandle("config.json").readString());

            JFrame frame = UI.NewJFrame("ui editor",new MainForm().panel1,GDX::Exit);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }
}
