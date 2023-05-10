package Tool.ObjectToolV2.Form;

import Extend.IExtend;
import Extend.IPutEvent;
import Extend.Spine.IAnimation;
import GDX11.GDX;
import GDX11.IObject.IAction.*;
import GDX11.IObject.IActor.IActor;
import Tool.Swing.GTree2;
import Tool.Swing.UI;
import Tool.Swing.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class IActionForm {
    private JTree tree;
    private JPanel pnInfo;
    public JPanel panel1;

    private GTree2<IAction> gTree = new GTree2<>(tree){
        @Override
        protected void OnKeyTyped(KeyEvent e) {
            super.OnKeyTyped(e);
            if (e.getKeyChar()=='') Run();
        }
    };
    private IActor iActor;

    public IActionForm()
    {
        pnInfo.setLayout(new WrapLayout());

        gTree.onSelect = this::OnSelectIAction;
        gTree.SetTypes("GDX",Arrays.asList(IDelay.class, IMove.class, IProperty.class, IMulAction.class, XAction.class,
                IRepeat.class, IAudioAction.class, IUtil.class, IClone.class, IImageAction.class, IAddParent.class,ICountAction.class,
                IParamAction.class));
        gTree.SetTypes("Spine",Arrays.asList(IAnimation.class));
        gTree.SetTypes("Extend",Arrays.asList(IExtend.class, IPutEvent.class));
        gTree.NewMenuItem(0,"Run","Ctrl+R",this::Run);
    }
    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
        gTree.SetRoot(iActor.iAction);
        gTree.SetSelection(iActor.iAction);
    }
    private void OnSelectIAction(IAction iAction)
    {
        pnInfo.removeAll();
        List<String> list = UI.GetFields(iAction);
        list.removeAll(Arrays.asList("iMap","name"));
        UI.InitComponents(list,iAction,pnInfo);
        UI.NewLabel(iAction.getClass().getSimpleName(),pnInfo).setForeground(Color.BLUE);
        UI.Repaint(pnInfo);
    }
    private void Run()
    {
        iActor.RunAction(gTree.GetMainObject().name);
    }
}
