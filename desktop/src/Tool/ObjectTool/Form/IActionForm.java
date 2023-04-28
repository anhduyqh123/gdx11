package Tool.ObjectTool.Form;

import Extend.IExtend;
import Extend.IPutEvent;
import Extend.Spine.IAnimation;
import GDX11.IObject.IAction.*;
import GDX11.IObject.IActor.IActor;
import GDX11.Reflect;
import Tool.Swing.GTree;
import Tool.Swing.UI;
import Tool.Swing.WrapLayout;

import javax.swing.*;
import java.util.*;

public class IActionForm {
    private JTree tree;
    private JPanel pnInfo;
    private JPanel pnBT;
    private JComboBox cb;
    private JComboBox cbType;
    private JButton btNew;
    private JButton cloneButton;
    private JButton runButton;
    private JLabel lbType;
    public JPanel panel1;
    private JTextField tfName;
    private JButton button1;

    private GTree<IAction> gTree = new GTree<>(tree,tfName);
    private Class selectedType;
    private IActor iActor;

    public IActionForm()
    {
        pnInfo.setLayout(new WrapLayout());

        String[] vl1 = {"GDX","Box2D","Spine", "Extend"};
        Class[] types1 = {IDelay.class, IMove.class, IProperty.class, IMulAction.class, XAction.class, IRepeat.class, IAudioAction.class,
        IUtil.class, IClone.class, IImageAction.class, IAddParent.class,ICountAction.class, IParamAction.class};
        Class[] types2 ={};
        Class[] types3 ={IAnimation.class};
        Class[] types4 ={IExtend.class, IPutEvent.class};
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
        gTree.newObject = this::NewIAction;
        gTree.onSelect = this::OnSelectIAction;

        UI.Button(btNew,gTree::NewObject);
        UI.Button(cloneButton,()->gTree.Clone());
        UI.Button(runButton,()->iActor.RunAction(gTree.GetMainObject().name));
    }
    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
        gTree.SetRoot(iActor.iAction);
        gTree.SetSelection(iActor.iAction);
    }
    protected IAction NewIAction()
    {
        return Reflect.NewInstance(selectedType);
    }
    private void OnSelectIAction(IAction iAction)
    {
        pnInfo.removeAll();
        List<String> list = UI.GetFields(iAction);
        list.removeAll(Arrays.asList("iMap","name"));
        UI.InitComponents(list,iAction,pnInfo);
        lbType.setText(iAction.getClass().getSimpleName());
        UI.Repaint(pnInfo);
    }
}
