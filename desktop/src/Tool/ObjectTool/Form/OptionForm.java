package Tool.ObjectTool.Form;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Scene;
import Tool.JFrame.UI;
import Tool.ObjectTool.Data.ClipBoard;
import Tool.ObjectTool.Data.MyGame;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

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
    private Actor dragActor;

    public OptionForm()
    {
        new ClipBoard(cbClipboard);
        BGColorButton.addActionListener(e->{
            UI.NewColorChooserWindow(Color.WHITE, hex->
                    MyGame.bg.set(Color.valueOf(hex)));
        });
        InitDrag();
    }

    private void InitDrag()
    {
        Vector2 p0 = new Vector2();
        Vector2 p = new Vector2();
        Scene.i.ui.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!cbDrag.isSelected()||button!=0) return false;
                dragActor = Scene.i.ui.hit(x,y,false);
                p0.set(x,y);
                if (dragActor!=null){
                    dragActor.setDebug(true);
                    p.set(dragActor.getX(),dragActor.getY());
                }
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (dragActor==null) return;
                Vector2 pos = new Vector2(x,y).sub(p0).add(p);
                IActor iActor = IActor.GetIActor(dragActor);
                iActor.SetPosition(pos, Align.bottomLeft);
                iActor.iPos.SetPosition(pos);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (dragActor==null) return;
                dragActor.setDebug(false);
                dragActor = null;
            }
        });
    }
}
