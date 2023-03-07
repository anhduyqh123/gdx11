package Tool.ObjectTool.Form;

import GDX11.IObject.IAction.IAction;
import GDX11.IObject.IAction.IDelay;
import GDX11.IObject.IAction.IMulAction;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Reflect;
import Tool.JFrame.GTree;
import Tool.JFrame.UI;
import Tool.JFrame.WrapLayout;

import javax.swing.*;
import java.util.*;

public class IActionForm {
    private JTree tree;
    private JPanel pnInfo;
    private JPanel pnBT;
    private JComboBox cb;
    private JComboBox cbType;
    private JButton btNew;
    private JButton btDelete;
    private JButton cloneButton;
    private JButton upButton;
    private JButton downButton;
    private JButton runButton;
    private JButton btPaste;
    private JButton stopButton;
    private JLabel lbType;
    public JPanel panel1;

    private GTree<IAction> gTree = new GTree<>(tree,null);
    private Class selectedType;
    private IAction mainIAction;

    public IActionForm()
    {
        pnInfo.setLayout(new WrapLayout());

        String[] vl1 = {"GDX","Box2D","Spine","Extend"};
        Class[] types1 = {IDelay.class, IMulAction.class};
        Class[] types2 ={};
        Class[] types3 ={};
        Class[] types4 ={};
        Class[][] types = {types1,types2,types3,types4};
        UI.ComboBox(cb,vl1,vl1[0],vl->{
            int index = cb.getSelectedIndex();
            Class[] type = types[index];

            UI.ComboBox(cbType,UI.ClassToNames(type),IMulAction.class.getSimpleName(),x->{
                int id = cbType.getSelectedIndex();
                if (id>=type.length) id = 0;
                selectedType = type[id];
            });
        });

        gTree.getChildren = this::GetChildren;
        gTree.getName = i->i.name;
        gTree.onSelect = this::OnSelectIAction;
        gTree.parentAdd = this::ParentAdd;
        gTree.parentRemove = this::ParentRemove;
        gTree.rename = this::Rename;
        gTree.clone = this::CloneIActor;
        //gTree.move = (i,d)->i.GetIParent().Move(i.GetName(),d);
        gTree.refreshObject = i->{};

        UI.Button(btNew,this::NewIAction);
    }
    public void SetIActor(IActor iActor)
    {
        gTree.SetRoot(iActor.iAction);
        gTree.SetSelection(iActor.iAction);
    }
    protected void NewIAction()
    {
        IAction newAc = Reflect.NewInstance(selectedType);
        IAction iAction = gTree.GetSelectedObject();
        if (!(iAction instanceof IMulAction)) iAction = gTree.GetParentObject();
        IMulAction iMul = (IMulAction) iAction;
        iMul.Add(newAc);
        gTree.Refresh();
        gTree.SetSelection(newAc);
    }
    private Collection GetChildren(IAction iAction)
    {
        if (iAction instanceof IMulAction) return ((IMulAction)iAction).list;
        return null;
    }
    private void ParentAdd(IAction parent,IAction child)
    {
        IMulAction iMul = (IMulAction) parent;
        iMul.Add(child);
    }
    private void ParentRemove(IAction parent,IAction child)
    {
        IMulAction iMul = (IMulAction) parent;
        iMul.Remove(child);
    }
    private void Rename(String name, IAction child,IAction parent)
    {
        IMulAction iMul = (IMulAction) parent;
        iMul.Remove(child);
        child.name = name;
        iMul.Add(child);
    }
    private IAction CloneIActor(IAction iAction)
    {
        IAction clone = Reflect.Clone(iAction);
        clone.name +="_clone";
        return clone;
    }
    private void OnSelectIAction(IAction iAction)
    {
        List<String> list = UI.GetFields(iAction);
        list.removeAll(Arrays.asList("list","name"));
        UI.InitComponents(list,iAction,pnInfo);
        lbType.setText(iAction.getClass().getSimpleName());
    }
}
