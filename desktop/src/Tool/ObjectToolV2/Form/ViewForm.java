package Tool.ObjectToolV2.Form;

import GDX11.*;
import Tool.ObjectToolV2.Core.MyGame;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ViewForm {
    public JPanel panel1;
    private JPanel pnView;
    private LwjglCanvas game;

    public ViewForm(Runnable done)
    {
        panel1.setPreferredSize(new Dimension(Config.Get("screen_width"),Config.Get("screen_height")));
        game = new LwjglCanvas(new MyGame(){
            @Override
            protected void FirstLoad() {
                done.run();
            }
        });
        panel1.add(game.getCanvas(),BorderLayout.CENTER);
    }
    private void FitSize()
    {
        int preWidth = (int)pnView.getPreferredSize().getWidth();
        int preHeight = (int)pnView.getPreferredSize().getHeight();
        float preScaleW = preWidth*1f/preHeight;
        pnView.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                float scaleW = panel1.getWidth()*1f/panel1.getHeight();
                int width = panel1.getWidth(),height = panel1.getHeight();
                if (scaleW>preScaleW) width = (int)(height*preScaleW);
                else height = (int)(width/preScaleW);
                pnView.setSize(width,height);
                pnView.setLocation((panel1.getWidth()-width)/2,(panel1.getHeight()-height)/2);
            }
        });
    }
}
