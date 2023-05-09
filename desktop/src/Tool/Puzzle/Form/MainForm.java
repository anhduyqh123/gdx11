package Tool.Puzzle.Form;

import SDK.SDK;
import Tool.Puzzle.Core.MyGame;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import javax.swing.*;

public class MainForm {
    public JPanel panel1;
    private JTabbedPane tabbedPane;
    private JPanel pnGame;
    public MainForm()
    {
        LoadGame();
    }
    private void LoadGame()
    {
        SDK.SetDesktopSDK();
        LwjglCanvas game = new LwjglCanvas(new MyGame(){
            @Override
            protected void FirstLoad() {
                Install();
            }
        });
        game.getCanvas().setSize(pnGame.getPreferredSize());
        pnGame.add(game.getCanvas());
    }
    private void Install()
    {
        //ShapeForm shapeForm = new ShapeForm();
        BoardForm boardForm = new BoardForm("boardData");
        BoardForm sudoForm = new BoardForm("sudoData");
        tabbedPane.addChangeListener(changeEvent -> {
            int index = tabbedPane.getSelectedIndex();
//            if (index==0) boardForm.OnTab();
//            if (index==1) sudoForm.OnTab();
        });
        tabbedPane.add("Jigsaw", boardForm.panel1);
        tabbedPane.add("sudoForm", sudoForm.panel1);
    }
}
