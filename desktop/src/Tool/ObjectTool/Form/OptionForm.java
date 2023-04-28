package Tool.ObjectTool.Form;

import GDX11.IObject.IActor.IActor;
import GDX11.Translate;
import Tool.Swing.UI;
import Tool.ObjectTool.Core.ClipBoard;
import Tool.ObjectTool.Core.Event;
import Tool.ObjectTool.Core.MyGame;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import javax.swing.*;

public class OptionForm {
    private JButton BGColorButton;
    private JButton toolButton;
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
            UI.NewColorChooserWindow(UI.GDXColorToColor(MyGame.bg), cl->
                    MyGame.bg.set(UI.ColorToGDXColor(cl)));
        });
        UI.Button(toolButton,()->{
            UI.NewJFrame("Tool",new ToolForm().panel1,()->{});
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
