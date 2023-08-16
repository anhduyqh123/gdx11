package Tool.ObjectToolV2.Form;

import Extend.Box2D.IBox2D;
import Extend.PagedScroll.IPagedScroll;
import Extend.Spine.ISpine;
import GDX11.*;
import GDX11.IObject.IActor.*;
import GDX11.IObject.IPos;
import Tool.Swing.GTree2;
import Tool.ObjectToolV2.Core.PackObject;
import Tool.Swing.UI;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class IObjectForm {
    public JPanel panel1;
    private JTree tree1;
    private IActor iActor,mainIActor;
    private PackObject pack;

    private GTree2<IActor> gTree = new GTree2<>(tree1){
        @Override
        protected void OnKeyTyped(KeyEvent e) {
            super.OnKeyTyped(e);
            if (e.getKeyChar()=='d') Debug();
            if (e.getKeyChar()=='') RefreshIActor();
            if (e.getKeyChar()=='' && IsMainChanged()) Save();
        }

        @Override
        protected void OnShowPopupMenu() {
            super.OnShowPopupMenu();
            GetItem("Prefab").setEnabled(GetClipSelectedList().size()>0);
            GetItem("Save").setEnabled(IsMainChanged());
            GetItem("PastePos").setEnabled(GetClipSelectedList().size()>0);
        }
    };

    public GDX.Runnable1<IActor> onSelectIActor;

    public IObjectForm(){
        gTree.onSelect = this::OnSelectIActor;
        gTree.refreshObject = IActor::Refresh;
        gTree.SetTypes(GetTypes());
        gTree.NewMenuItem(2,"Refresh","Ctrl+F",this::RefreshIActor);
        gTree.NewMenuItemBySelect(2,"Prefab","",gTree::Prefab);
        gTree.NewMenuItem("Save","Ctrl+S",this::Save);
        gTree.NewMenuItem("PastePos","",this::PastePos);
        gTree.NewMenuItem("Debug(D)","",this::Debug);
        Config.i.SetRun("reloadPack",()-> SetData(pack));
    }
    private List<Class> GetTypes() {
        return Arrays.asList(IGroup.class, IImage.class, ILabel.class, ITable.class, IActor.class, IParticle.class, ISpine.class,
                IScrollImage.class, IProgressBar.class,IScrollPane.class, IPagedScroll.class, ITextField.class, IBox2D.class);
    }

    private void RefreshIActor() {
        iActor.Reconnect();
        iActor.Refresh();
    }
    public void SetData(PackObject pack) {
        Asset.i.ForceLoadPackages(pack.name);
        if (mainIActor!=null) mainIActor.GetActor().remove();
        mainIActor = null;
        this.pack = pack;
        pack.Renew();
        gTree.SetRoot(pack);
        gTree.SetSelection(pack);

        Config.i.Run("asset"+pack.name);
    }

    private void OnSelectIActor(IActor iActor) {
        if (iActor instanceof PackObject) return;
        this.iActor = iActor;
        SetMainIActor(iActor.GetIRoot());
        onSelectIActor.Run(iActor);
    }
    private void SetMainIActor(IActor iActor) {
        if (iActor==null) return;
        if (mainIActor!=null && mainIActor.equals(iActor)) return;
        if (mainIActor!=null) mainIActor.GetActor().remove();
        mainIActor = iActor;
        iActor.Refresh();
    }
    private void Save() {
        pack.Save(mainIActor.name,()-> UI.NewDialog("Save success!",panel1),()->{
            Config.i.Run("reloadData");
            SetData(pack);
        });
    }
    private boolean IsMainChanged() {
        if (mainIActor==null) return false;
        return GDX.Try(()->{
            String data0 = Asset.i.GetString(mainIActor.name);
            String data = Json.ToJson(mainIActor).toJson(JsonWriter.OutputType.minimal);
            return !data.equals(data0);
        },()->true);
    }
    private void PastePos(){
        iActor.iPos = Reflect.Clone(gTree.GetClipSelected().iPos);
        RefreshIActor();
        OnSelectIActor(iActor);
    }
    private void Debug(){
        if (iActor==null) return;
        iActor.GetActor().setDebug(!iActor.GetActor().getDebug());
    }
}
