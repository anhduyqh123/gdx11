package Tool.ObjectToolV2.Form;

import Extend.AI.ISteering;
import Extend.AI.ITest;
import Extend.IMask;
import Extend.IShape.ICircle;
import Extend.IShape.IPoints;
import Extend.IShape.IPolygon;
import Extend.IShape.IShape;
import Extend.IShapeMask;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IComponent.IComponent;
import GDX11.IObject.IComponent.IShader;
import Tool.Swing.GTree2;
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
    private JCheckBox edit;
    private GTree2<IComponent> gTree = new GTree2<>(tree);
    private IActor iActor;
    private IComponent iComponent;
    private IPointsEdit pointsEdit;

    public IComponentForm()
    {
        pnInfo.setLayout(new WrapLayout());

        gTree.onSelect = this::OnSelect;
        gTree.SetTypes("GDX",Arrays.asList(IComponent.class, IShader.class));
        gTree.SetTypes("Shape",Arrays.asList(ICircle.class, IPoints.class, IPolygon.class));
        gTree.SetTypes("Extend",Arrays.asList(IMask.class, IShapeMask.class));
        gTree.SetTypes("AI",Arrays.asList(ISteering.class, ITest.class));

        gTree.onSelect = cp->{
            OnSelect(cp);
            CheckEdit();
        };
        gTree.onNew = this::OnNew;
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
    private void OnSelect(IComponent cp)
    {
        iComponent = cp;
        pnInfo.removeAll();
        List<String> list = UI.GetFields(cp);
        list.removeAll(Arrays.asList("name"));
        UI.InitComponents(list,cp,pnInfo);
        UI.Repaint(pnInfo);
    }
    private void OnNew(IComponent cp)
    {
        if (cp instanceof IShape) ((IShape) cp).Init();
    }
    private void NewEdit()
    {
        if (pointsEdit!=null) pointsEdit.remove();
        if (iComponent instanceof IShape)
        {
            pointsEdit = NewPointEdit();
            pointsEdit.onDataChange = ()->OnSelect(iComponent);
        }
    }
    private IPointsEdit NewPointEdit()
    {
        if (iComponent instanceof ICircle){
            ICircleEdit editCircle = new ICircleEdit(iActor);
            editCircle.SetData((ICircle) iComponent);
            return editCircle;
        }
        IPointsEdit edit = new IPointsEdit(iActor);
        edit.SetData(iComponent.Get(IPoints.class).list);
        return edit;
    }
}
