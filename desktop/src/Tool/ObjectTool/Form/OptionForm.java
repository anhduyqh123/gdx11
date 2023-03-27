package Tool.ObjectTool.Form;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Translate;
import Tool.JFrame.UI;
import Tool.ObjectTool.Data.ClipBoard;
import Tool.ObjectTool.Data.Event;
import Tool.ObjectTool.Data.MyGame;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

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
    private JButton btResetCam;
    private IActor iActor;
    private EventListener event;

    public OptionForm()
    {
        Translate.Init();
        new ClipBoard(cbClipboard);
        BGColorButton.addActionListener(e->{
            UI.NewColorChooserWindow(Color.WHITE, hex->
                    MyGame.bg.set(Color.valueOf(hex)));
        });
        UI.Button(btResetCam,Event::ResetCamera);
        event = new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!cbDrag.isSelected()) return false;
                Event.dragIActor = iActor;
                return true;
            }
        };
    }
    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
        iActor.GetActor().addListener(event);
    }
}
