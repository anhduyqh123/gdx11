package Tool.ObjectToolV2.Form;

import Extend.IExtend;
import Extend.IPutEvent;
import Extend.Spine.IAnimation;
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
            if (e.getKeyChar()=='') RunAction();
            if (e.getKeyChar()=='r') Run();
        }
    };
    private IActor iActor;

    public IActionForm()
    {
        pnInfo.setLayout(new WrapLayout());

        gTree.onSelect = this::OnSelectIAction;
        gTree.SetTypes("GDX",Arrays.asList(IDelay.class, IMove.class, IProperty.class, IMulAction.class, XAction.class,
                IRepeat.class, IParAction.class,IAudioAction.class, IUtil.class, IPool.class, IImageAction.class, IToParent.class,
                ICountAction.class, IParamAction.class));
        gTree.SetTypes("Spine",Arrays.asList(IAnimation.class));
        gTree.SetTypes("Extend",Arrays.asList(IExtend.class, IPutEvent.class));
        gTree.NewMenuItem(0,"RunAction","Ctrl+R",this::RunAction);
        gTree.NewMenuItem(0,"Run","R",this::Run);
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
    private void RunAction()
    {
        iActor.RunAction(gTree.GetMainObject().name);
    }
    private void Run()
    {
        iActor.Run(gTree.GetMainObject().name);
    }
}
