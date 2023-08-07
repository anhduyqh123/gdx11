package Tool.ObjectTool.Form;

import Extend.AI.ISteering;
import Extend.AI.ITest;
import Extend.IMask;
import GDX11.IObject.IComponent.IShape.ICircle;
import GDX11.IObject.IComponent.IShape.IPoints;
import GDX11.IObject.IComponent.IShape.IPolygon;
import GDX11.IObject.IComponent.IShape.IShape;
import Extend.IShapeMask;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IComponent.IComponent;
import GDX11.IObject.IComponent.IShader;
import GDX11.Reflect;
import Tool.Swing.GTree;
import Tool.Swing.UI;
import Tool.Swing.WrapLayout;
import Tool.ObjectTool.Point.ICircleEdit;
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

        String[] vl1 = {"GDX","Shape", "Extend","AI","Other"};
        Class[] types1 = {IComponent.class, IShader.class};
        Class[] types2 ={ICircle.class, IPoints.class, IPolygon.class};
        Class[] types3 ={IMask.class, IShapeMask.class};
        Class[] types4 ={ISteering.class, ITest.class};
        Class[] types5 ={};
        Class[][] types = {types1,types2,types3,types4,types5};

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
        gTree.onSelect = cp->{
            OnSelect(cp);
            CheckEdit();
        };

        UI.Button(btNew,gTree::NewObject);
        UI.Button(cloneButton,()->gTree.Clone());
        UI.CheckBox(edit, vl->CheckEdit());
    }
    private void CheckEdit()
    {
        if (edit.isSelected()) NewEdit();
        else{
            if (pointsEdit!=null) pointsEdit.remove();
        }
    }

    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
        gTree.SetRoot(iActor.iComponents);
        gTree.SetSelection(iActor.iComponents);
    }
    private IComponent NewIComponent()
    {
        IComponent icp = Reflect.NewInstance(selectedType);
        //if (icp instanceof IShape) ((IShape) icp).Init();
        return icp;
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
        if (pointsEdit!=null) pointsEdit.remove();
        if (selected instanceof IShape)
        {
            pointsEdit = NewPointEdit();
            pointsEdit.onDataChange = ()->OnSelect(selected);
        }
    }
    private IPointsEdit NewPointEdit()
    {
        if (selected instanceof ICircle){
            ICircleEdit editCircle = new ICircleEdit(iActor);
            editCircle.SetData((ICircle) selected);
            return editCircle;
        }
        IPointsEdit edit = new IPointsEdit(iActor);
        edit.SetData(selected.Get(IPoints.class));
        return edit;
    }
}
