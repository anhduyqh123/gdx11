package Tool.ObjectToolV2.Form;

import Extend.AI.ISteering;
import Extend.AI.ITest;
import Extend.Box2D.GBox2D;
import Extend.Box2D.IBody;
import Extend.Box2D.IFixture;
import Extend.Box2D.IPlatform;
import Extend.IDropDown;
import Extend.IMask;
import GDX11.GDX;
import GDX11.IObject.IComponent.IComponents;
import GDX11.IObject.IComponent.IShape.ICircle;
import GDX11.IObject.IComponent.IShape.IPoints;
import GDX11.IObject.IComponent.IShape.IPolygon;
import GDX11.IObject.IComponent.IShape.IShape;
import Extend.IShapeMask;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IComponent.IComponent;
import GDX11.IObject.IComponent.IShader;
import Tool.ObjectToolV2.Point.ICircleEdit;
import Tool.ObjectToolV2.Point.IPointsEdit;
import Tool.Swing.*;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class IComponentForm {
    public JPanel panel1;
    private JTree tree;
    private JPanel pnInfo;
    private JCheckBox edit;
    private final GTree2<IComponent> gTree = new GTree2<>(tree);
    private IActor iActor;
    private IComponent iComponent;
    private IPointsEdit pointsEdit;

    public IComponentForm()
    {
        pnInfo.setLayout(new WrapLayout());

        gTree.onSelect = this::OnSelect;
        gTree.SetTypes("GDX",Arrays.asList(IComponent.class, IShader.class));
        gTree.SetTypes("Shape",Arrays.asList(ICircle.class, IPoints.class, IPolygon.class));
        gTree.SetTypes("Extend",Arrays.asList(IMask.class, IShapeMask.class, IDropDown.class));
        gTree.SetTypes("Box2D",Arrays.asList(IBody.class, IFixture.class, IPlatform.class));
        gTree.SetTypes("AI",Arrays.asList(ISteering.class, ITest.class));

        gTree.refreshObject = IComponent::Refresh;
        gTree.onSelect = cp->{
            OnSelect(cp);
            CheckEdit();
        };
        gTree.onNew = this::OnNew;
        UI.CheckBox(edit, vl->CheckEdit());
    }
    private void CheckEdit()
    {
        if (pointsEdit!=null) pointsEdit.remove();
        if (edit.isSelected()) NewEdit();
    }

    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
        gTree.SetRoot(iActor.iComponents);
        gTree.SetSelection(iActor.iComponents);
        UI.ClearPanel(pnInfo);
    }
    private void OnSelect(IComponent cp)
    {
        iComponent = cp;
        pnInfo.removeAll();
        if (cp instanceof IFixture) NewFixtureView((IFixture) cp);
        else NewView(cp);
        UI.NewLabel("type:"+cp.getClass().getSimpleName(),pnInfo).setForeground(Color.BLUE);
        UI.Repaint(pnInfo);
    }
    private void OnNew(IComponent cp) {
        cp.OnNew();
    }
    //View
    private void NewView(IComponent cp){
        if (cp instanceof IComponents) return;
        List<String> list = UI.GetFields(cp);
        list.removeAll(Arrays.asList("name","active"));
        UI.InitComponents(list,cp,pnInfo);
        UI.InitComponent("active",cp,pnInfo);
    }
    private void NewFixtureView(IFixture iFix){
        List<String> list = UI.GetFields(iFix);
        list.removeAll(Arrays.asList("name","category","mark"));
        UI.InitComponents(list,iFix,pnInfo);
        GDX.Try(()->{
            GBox2D box2D = iFix.GetIActor().IRootFind("box2d").GetActor();
            String[] arr = box2D.categories.toArray(new String[0]);
            UI.NewComboBox("category",arr,box2D.GetCategory(iFix.category),pnInfo,
                    vl-> iFix.category = box2D.GetCategory(vl));
            InitMark(iFix,box2D,pnInfo);
        });
    }
    private void InitMark(IFixture iFix,GBox2D box2D,JPanel pn){
        List<String> caList = box2D.GetCategoryList(iFix.mark);
        JPanel panel = new ListForm("mark",caList){
            @Override
            protected GList2 NewGList() {
                return new GList2(tree1){
                    @Override
                    protected void InitPopupMenu() {
                        NewMenuItem("Delete","Delete",()->{
                            Delete();
                            iFix.mark = box2D.GetBit(caList);
                        });
                        NewMenuItem("Reset","",()->{
                            caList.clear();
                            caList.addAll(box2D.categories);
                            iFix.mark = box2D.GetBit(caList);
                            gList.SetData(caList);
                        });
                    }
                };
            }
        }.panel1;
        pn.add(panel);
    }

    private void NewEdit()
    {
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
        edit.SetData(iComponent.Get(IPoints.class));
        return edit;
    }
}
