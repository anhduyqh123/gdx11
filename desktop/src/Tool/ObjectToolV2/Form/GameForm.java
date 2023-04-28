package Tool.ObjectToolV2.Form;

import GDX11.GDX;
import GDX11.GDXGame;
import Tool.Swing.UI;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import javax.swing.*;

public class GameForm {


    public JPanel panel1;
    public GameForm(Runnable cb)
    {
        LwjglCanvas game = new LwjglCanvas(new GDXGame(){
            @Override
            protected void FirstLoad() {
                cb.run();
            }
        });
        game.getCanvas().setSize(320,200);
        panel1.add(game.getCanvas());
        UI.NewJFrame("view",panel1,GDX::Exit).setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
}
