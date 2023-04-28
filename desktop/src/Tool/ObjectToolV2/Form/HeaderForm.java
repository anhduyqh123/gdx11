package Tool.ObjectToolV2.Form;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import Tool.Swing.UI;
import Tool.ObjectToolV2.Core.Event;
import Tool.ObjectToolV2.Core.MyGame;
import Tool.ObjectToolV2.Core.PackObject;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class HeaderForm {
    private JComboBox cbPack;
    private JCheckBox cbDrag;
    public JPanel panel1;
    private JButton btResetCam;
    private JButton btColor;
    private JButton toolButton;
    private IActor iActor;
    private final InputListener event;

    public HeaderForm(GDX.Runnable1<PackObject> onLoadPack)
    {
        UI.Button(btResetCam, Event::ResetCamera);
        UI.Button(btColor,()->{
            UI.NewColorChooserWindow(UI.GDXColorToColor(MyGame.bg), cl->
                    MyGame.bg.set(UI.ColorToGDXColor(cl)));
        });

        event = new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!cbDrag.isSelected()) return false;
                Event.dragIActor = iActor;
                return true;
            }
        };

        Map<String, PackObject> map = new HashMap<>();
        String[] packs = Asset.i.data.GetKeys().toArray(new String[0]);
        for (String key : packs)
            map.put(key,new PackObject(key));
        UI.ComboBox(cbPack,packs,packs[0], pack->{
            Asset.i.ForceLoadPackages(null,pack);
            onLoadPack.Run(map.get(pack));
        });
    }
    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;

        iActor.GetActor().addListener(event);
    }
}
