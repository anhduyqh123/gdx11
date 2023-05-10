package Tool.ObjectToolV2.Form;

import Extend.PagedScroll.IPagedScroll;
import Extend.Spine.ISpine;
import GDX11.GDX;
import GDX11.IObject.IActor.*;
import GDX11.Json;
import GDX11.Reflect;
import Tool.Swing.GTree2;
import Tool.ObjectToolV2.Core.PackObject;
import Tool.Swing.UI;

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
            if (e.getKeyChar()=='') RefreshIActor();
            if (e.getKeyChar()=='' && IsMainChanged()) Save();
        }

        @Override
        protected void OnShowPopupMenu() {
            super.OnShowPopupMenu();
            GetItem("Save").setEnabled(IsMainChanged());
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
    }
    private List<Class> GetTypes()
    {
        return Arrays.asList(IGroup.class, IImage.class, ILabel.class, ITable.class, IActor.class, ISpine.class,
                IScrollImage.class, IProgressBar.class,IScrollPane.class, IPagedScroll.class, ITextField.class);
    }

    private void RefreshIActor()
    {
        iActor.iParam.Dispose();
        iActor.Refresh();
    }
    public void SetData(PackObject pack)
    {
        mainIActor = null;
        this.pack = pack;
        pack.Renew();
        gTree.SetRoot(pack);
        gTree.SetSelection(pack);
    }

    private void OnSelectIActor(IActor iActor)
    {
        if (iActor instanceof PackObject) return;
        this.iActor = iActor;
        SetMainIActor(iActor.GetIRoot());
        onSelectIActor.Run(iActor);
    }
    private void SetMainIActor(IActor iActor)
    {
        if (iActor==null) return;
        if (mainIActor!=null && mainIActor.equals(iActor)) return;
        if (mainIActor!=null) mainIActor.GetActor().remove();
        mainIActor = iActor;
        iActor.Refresh();
    }
    public void Save()
    {
        pack.Save(mainIActor.name,()-> UI.NewDialog("Save success!",panel1));
    }
    private boolean IsMainChanged()
    {
        if (mainIActor==null) return false;
        String data = GDX.GetStringByKey(mainIActor.name);
        if (data==null) return true;
        IActor mainFromData = Json.ToObject(data);
        return !mainIActor.equals(mainFromData);
    }
}
