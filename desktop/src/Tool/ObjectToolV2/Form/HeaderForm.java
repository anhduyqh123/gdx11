package Tool.ObjectToolV2.Form;

import GDX11.Asset;
import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Json;
import Tool.Swing.UI;
import Tool.ObjectToolV2.Core.Event;
import Tool.ObjectToolV2.Core.MyGame;
import Tool.ObjectToolV2.Core.PackObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.JsonValue;

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
    private JButton btSave;
    private JButton btReload;
    private IActor iActor;
    private final InputListener event;

    public HeaderForm(GDX.Runnable1<PackObject> onLoadPack)
    {
        UI.Button(toolButton, ToolForm::new);
        UI.Button(btResetCam, Event::ResetCamera);
        UI.Button(btColor,()->{
            UI.NewColorChooserWindow(UI.GDXColorToColor(MyGame.bg), cl->
                    MyGame.bg.set(UI.ColorToGDXColor(cl)));
        });
        UI.Button(btSave,this::SaveProject);
        UI.Button(btReload,this::ReloadAssets);

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
        UI.ComboBox(cbPack,packs,packs[0], pack-> onLoadPack.Run(map.get(pack)));
    }
    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
        iActor.GetActor().addListener(event);
    }
    private void SaveProject()
    {
        Vector2 framePos = (Vector2) Config.i.Get("framePos", GDX.Func.class).Run();
        Vector2 screenSize = (Vector2) Config.i.Get("screenSize", GDX.Func.class).Run();
        JsonValue js = Json.StringToJson(GDX.GetString("config.json"));
        js.get("screen_width").set((int)screenSize.x+"");
        js.get("screen_height").set((int)screenSize.y+"");
        js.get("screen_x").set((int)framePos.x+"");
        js.get("screen_y").set((int)framePos.y+"");
        GDX.WriteToFile("config.json",js.toString());
        UI.NewDialog("Save Project Success!",panel1);
    }
    private void ReloadAssets(){
        Config.i.Run("reloadData");//MyGame
        Config.i.Run("reloadAsset");//AssetForm
        Config.i.Run("reloadPack");//IObjectForm
    }
}
