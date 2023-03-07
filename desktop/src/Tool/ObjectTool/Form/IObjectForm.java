package Tool.ObjectTool.Form;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Reflect;
import Tool.JFrame.GTree;
import Tool.JFrame.UI;
import Tool.ObjectTool.Data.Content;
import Tool.ObjectTool.Data.ObjectData;
import Tool.ObjectTool.Data.ObjectPack;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class IObjectForm {
    private JTree tree;
    private JComboBox cbPack;
    private JTextField tfName;
    private JComboBox cbType;
    private JButton btNew;
    private JButton btPrefab;
    private JButton deleteButton;
    private JButton btPaste;
    private JButton cloneButton;
    private JButton upButton;
    private JButton downButton;
    private JButton addToButton;
    private JButton saveButton;
    private JButton btReload;
    public JPanel panel1;

    private ObjectData data = new ObjectData();
    private ObjectPack selectedPack;
    private IActor selectedIActor;
    private IActor mainIActor;
    private final List<String> allPack = new ArrayList<>(Asset.i.data.GetKeys());
    private final Class[] types = Content.GetTypes();
    private GTree<IActor> gTree = new GTree<>(tree,tfName);

    //event
    public GDX.Runnable1<IActor> onSelectIActor;

    public IObjectForm()
    {
        Collections.sort(allPack);
        data.Load(allPack);

        UI.ComboBox(cbType,UI.ClassToNames(types));

        gTree.getChildren = this::GetChildren;
        gTree.getName = IActor::GetName;
        gTree.onSelect = this::OnSelectIActor;
        gTree.parentAdd = this::ParentAdd;
        gTree.parentRemove = this::ParentRemove;
        gTree.rename = this::Rename;
        gTree.clone = this::CloneIActor;
        gTree.move = (i,d)->i.GetIParent().Move(i.GetName(),d);
        gTree.refreshObject = IActor::Refresh;
        RefreshData();

        UI.Button(btNew,this::NewIActor);
        UI.Button(saveButton,this::Save);
    }
    private Collection GetChildren(IActor iActor)
    {
        if (iActor instanceof IGroup) return ((IGroup)iActor).GetIChildren();
        return null;
    }
    private void RefreshData()
    {
        String[] pack = allPack.toArray(new String[0]);
        UI.ComboBox(cbPack,pack,pack[0], vl->{
            Asset.i.ForceLoadPackages(null,vl);
            selectedPack = data.Get(vl);
            selectedPack.Renew();
            gTree.SetRoot(selectedPack);
            gTree.SetSelection(selectedPack);
        });
    }
    private void OnSelectIActor(IActor iActor)
    {
        SetMainIActor(gTree.GetMainObject());
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

    //Edit
    private void NewIActor()
    {
        IActor newObject = Reflect.NewInstance(types[cbType.getSelectedIndex()]);
        String name = tfName.getText();
        IActor iActor = gTree.GetSelectedObject();
        if (!(iActor instanceof IGroup)) iActor = iActor.GetIParent();
        IGroup group = (IGroup) iActor;
        if (group.Contains(name))
        {
            UI.NewDialog("tên biến trong 1 group không được trùng nhau!",panel1);
            return;
        }
        group.AddChild(name,newObject);
        group.Refresh();
        gTree.Refresh();
        gTree.SetSelection(newObject);
    }
    private void Save()
    {
        selectedPack.Save(mainIActor.GetName(),()->UI.NewDialog("Save success!",panel1));
    }
    //extend
    private void ParentAdd(IActor parent,IActor child)
    {
        IGroup iGroup = (IGroup) parent;
        iGroup.AddChild(child.GetName(),child);
    }
    private void ParentRemove(IActor parent,IActor child)
    {
        IGroup iGroup = (IGroup) parent;
        iGroup.Remove(child.GetName());
    }
    private IActor CloneIActor(IActor iActor)
    {
        IActor clone = Reflect.Clone(iActor);
        clone.SetName(iActor.GetName());
        return clone;
    }
    private void Rename(String name, IActor iActor)
    {
        iActor.GetIParent().Rename(iActor.GetName(),name);
    }
}
