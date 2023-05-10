package Tool.Puzzle.Form;

import SDK.SDK;
import Tool.Puzzle.Core.MyGame;
import Tool.Swing.UI;
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
        BoardForm jigsawForm = new BoardForm("jigsawData");
        BoardForm sudoForm = new BoardForm("sudoData");
        BoardForm puzzForm = new BoardForm("puzzData");
        tabbedPane.addChangeListener(changeEvent -> {
            BoardForm boardForm = UI.GetUserObject(tabbedPane.getSelectedComponent());
            boardForm.OnTab();
        });
        tabbedPane.add("Jigsaw", jigsawForm.panel1);
        //tabbedPane.add("sudoForm", sudoForm.panel1);
        tabbedPane.add("PuzzForm", puzzForm.panel1);
    }
}
