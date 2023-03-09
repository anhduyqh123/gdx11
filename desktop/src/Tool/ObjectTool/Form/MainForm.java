package Tool.ObjectTool.Form;

import GDX11.GDX;

import javax.swing.*;

public class MainForm {
    public JPanel panel1;
    private JPanel pnTop;
    private JPanel pnBot;
    private JTabbedPane tabbedPane;

    public MainForm()
    {

    }
    public void Install()
    {
        IObjectForm dataForm = new IObjectForm();
        OptionForm optionForm = new OptionForm();
        pnTop.add(dataForm.panel1);
        pnTop.add(optionForm.panel1);

        IActorForm iActorForm = new IActorForm();
        IActionForm iActionForm = new IActionForm();
        tabbedPane.add("Info", iActorForm.panel1);
        tabbedPane.add("Action", iActionForm.panel1);

        dataForm.onSelectIActor = iActor->{
            iActorForm.SetIActor(iActor);
            iActionForm.SetIActor(iActor);
        };
    }
}
