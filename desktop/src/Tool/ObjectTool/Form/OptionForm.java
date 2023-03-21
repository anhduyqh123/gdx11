package Tool.ObjectTool.Form;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IPos;
import GDX11.Reflect;
import GDX11.Scene;
import GDX11.Util;
import Tool.JFrame.UI;
import Tool.ObjectTool.Data.ClipBoard;
import Tool.ObjectTool.Data.MyGame;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
    private JButton btResetCam;
    private Actor dragActor;
    private Runnable backRun = ()->{};

    public OptionForm()
    {
        new ClipBoard(cbClipboard);
        BGColorButton.addActionListener(e->{
            UI.NewColorChooserWindow(Color.WHITE, hex->
                    MyGame.bg.set(Color.valueOf(hex)));
        });
        UI.Button(btResetCam,this::ResetCamera);
        InitDrag();
    }
    private void ResetCamera()
    {
        OrthographicCamera camera  = (OrthographicCamera) Scene.i.GetStage().getCamera();
        camera.zoom = 1;
        camera.position.set(Scene.i.width/2,Scene.i.height/2,0);
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
                    IActor iActor = IActor.GetIActor(dragActor);
                    IPos iPos = Reflect.Clone(iActor.iPos);
                    iPos.SetIActor(iActor);
                    backRun = ()->{
                        iActor.iPos = iPos;
                        iPos.Refresh();
                    };
                }
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (dragActor==null) return;
                Vector2 pos = new Vector2(x,y).sub(p0).add(p);
                Util.Int(pos);
                IActor iActor = IActor.GetIActor(dragActor);
                iActor.SetPosition(pos, Align.bottomLeft);
                iActor.iPos.SetPosition(pos);
                iActor.iPos.OnChange();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (dragActor==null) return;
                dragActor.setDebug(false);
                dragActor = null;
            }
        });
        Scene.i.GetStage().addListener(new InputListener(){
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (event.getKeyCode() == Input.Keys.ESCAPE) backRun.run();
                return true;
            }
        });
    }
}
