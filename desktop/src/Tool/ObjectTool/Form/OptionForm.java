package Tool.ObjectTool.Form;

import Tool.JFrame.UI;
import Tool.ObjectTool.Data.ClipBoard;
import Tool.ObjectTool.Data.MyGame;
import com.badlogic.gdx.graphics.Color;

import javax.swing.*;

public class OptionForm {
    private JButton BGColorButton;
    private JButton editButton;
    private JComboBox cbCode;
    private JButton btGlyphs;
    private JCheckBox cbPhysics;
    private JCheckBox cbBox2d;
    private JCheckBox cbDrag;
    public JPanel panel1;
    private JComboBox cbClipboard;

    public OptionForm()
    {
        new ClipBoard(cbClipboard);
        BGColorButton.addActionListener(e->{
            UI.NewColorChooserWindow(Color.WHITE, hex->
                    MyGame.bg.set(Color.valueOf(hex)));
        });
    }
}
