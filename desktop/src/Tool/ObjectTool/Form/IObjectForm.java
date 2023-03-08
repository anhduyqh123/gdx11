package Tool.ObjectTool.Form;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IObject;
import GDX11.Reflect;
import Tool.JFrame.GTree;
import Tool.JFrame.UI;
import Tool.ObjectTool.Data.ClipBoard;
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

        gTree.newObject = this::NewIActor;
        gTree.onSelect = this::OnSelectIActor;
        gTree.refreshObject = IActor::Refresh;
        RefreshData();

        UI.Button(btNew,gTree::NewObject);
        UI.Button(btPrefab,this::Prefab);
        UI.Button(cloneButton,()->gTree.Clone(tfName.getText()));
        UI.Button(saveButton,this::Save);
    }
    private void Prefab()
    {
        if (ClipBoard.i.GetObjects().size()<=0) return;
        IObject ob = ClipBoard.i.GetObjects().get(0);
        gTree.Clone(tfName.getText(),ia->ia.prefab=ob.name);
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
        tfName.setText(iActor.name);
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
    private IActor NewIActor()
    {
        IActor newObject = Reflect.NewInstance(types[cbType.getSelectedIndex()]);
        newObject.name = tfName.getText();
        return newObject;
    }
    private void Save()
    {
        selectedPack.Save(mainIActor.name,()->UI.NewDialog("Save success!",panel1));
    }
}
