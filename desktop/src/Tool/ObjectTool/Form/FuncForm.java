package Tool.ObjectTool.Form;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import Tool.Swing.UI;

import javax.swing.*;
import java.util.Collections;

public class FuncForm {
    private JComboBox cbFunc;
    private JButton btRun;
    private JPanel pnVar;
    public JPanel panel1;

    private IActor iActor;
    private Func func;
    public FuncForm(GDX.Runnable1<IActor> refresh)
    {
        Class[] types = {Clone.class,Reverse.class,Other.class};
        UI.ComboBox(cbFunc,UI.ClassToNames(types),"Clone", st->{
            int index = cbFunc.getSelectedIndex();
            if (index==0) func = new Clone();
            if (index==1) func = new Reverse();
            if (index==2) func = new Other();
            UI.ClearPanel(pnVar);
            UI.InitComponents(func,pnVar);
        });
        UI.Button(btRun,()->{
            func.Run();
            refresh.Run(iActor);
        });
    }
    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
    }
    public abstract class Func
    {
        public abstract void Run();
    }
    public class Clone extends Func
    {
        public int amount = 1;
        public void Run()
        {
            IGroup iGroup = iActor.GetIGroup();
            IActor child = iGroup.iMap.Get(0);
            for (int i=0;i<amount;i++)
            {
                IActor clone = child.Clone();
                clone.name +=i;
                iGroup.iMap.Add(clone);
            }
            iGroup.Refresh();
        }
    }
    public class Reverse extends Func
    {
        public void Run()
        {
            IGroup iGroup = iActor.GetIGroup();
            Collections.reverse(iGroup.iMap.list);
            iGroup.Refresh();
        }
    }
    public class Other extends Func
    {
        public void Run()
        {

        }
    }
}
