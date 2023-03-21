package Tool.ObjectTool.Form;

import Extend.IShape.ICircle;
import Extend.IShape.IPoints;
import Extend.IShape.IPolygon;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IComponent.IComponent;
import GDX11.Reflect;
import Tool.JFrame.GTree;
import Tool.JFrame.UI;
import Tool.JFrame.WrapLayout;
import Tool.ObjectTool.Point.IPointsEdit;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class IComponentForm {
    public JPanel panel1;
    private JTree tree;
    private JPanel pnInfo;
    private JPanel pnBT;
    private JComboBox cb;
    private JComboBox cbType;
    private JTextField tfName;
    private JButton btNew;
    private JButton btDelete;
    private JButton cloneButton;
    private JButton btPaste;
    private JLabel lbType;
    private JCheckBox edit;

    private GTree<IComponent> gTree = new GTree<>(tree,tfName);
    private Class selectedType;
    private IComponent selected;
    private IActor iActor;
    private IPointsEdit pointsEdit;

    public IComponentForm()
    {
        pnInfo.setLayout(new WrapLayout());

        String[] vl1 = {"GDX","Shape","Extend","Other"};
        Class[] types1 = {IComponent.class};
        Class[] types2 ={ICircle.class, IPoints.class, IPolygon.class};
        Class[] types3 ={};
        Class[] types4 ={};
        Class[][] types = {types1,types2,types3,types4};

        UI.ComboBox(cb,vl1,vl1[0], vl->{
            int index = cb.getSelectedIndex();
            Class[] type = types[index];

            UI.ComboBox(cbType,UI.ClassToNames(type),type[0].getSimpleName(),x->{
                int id = cbType.getSelectedIndex();
                if (id>=type.length) id = 0;
                selectedType = type[id];
            });
        });
        gTree.newObject = this::NewIComponent;
        gTree.onSelect = this::OnSelect;

        UI.Button(btNew,gTree::NewObject);
        UI.Button(cloneButton,()->gTree.Clone());
        UI.CheckBox(edit,vl->{
            if (vl) NewEdit();
            else pointsEdit.remove();;
        });
    }

    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
        gTree.SetRoot(iActor.iComponents);
        gTree.SetSelection(iActor.iComponents);
    }
    private IComponent NewIComponent()
    {
        GDX.Log(selectedType.getSimpleName());
        return Reflect.NewInstance(selectedType);
    }
    private void OnSelect(IComponent cp)
    {
        selected = cp;
        pnInfo.removeAll();
        List<String> list = UI.GetFields(cp);
        list.removeAll(Arrays.asList("name"));
        UI.InitComponents(list,cp,pnInfo);
        lbType.setText(cp.getClass().getSimpleName());
        UI.Repaint(pnInfo);
    }
    private void NewEdit()
    {
        pointsEdit = new IPointsEdit(iActor);
        if (selected instanceof IPoints) pointsEdit.SetData(selected.Get(IPoints.class).list);
    }
}
