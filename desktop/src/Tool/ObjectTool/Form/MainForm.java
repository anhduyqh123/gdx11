package Tool.ObjectTool.Form;

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
        IComponentForm iComponentForm = new IComponentForm();
        IParamForm iParamForm = new IParamForm();
        FuncForm funcForm = new FuncForm(dataForm::RefreshAt);

        tabbedPane.add("Info", iActorForm.panel1);
        tabbedPane.add("Action", iActionForm.panel1);
        tabbedPane.add("Component", iComponentForm.panel1);
        tabbedPane.add("Param", iParamForm.panel1);
        tabbedPane.add("Function", funcForm.panel1);

        dataForm.onSelectIActor = iActor->{
            iActorForm.SetIActor(iActor);
            iActionForm.SetIActor(iActor);
            iComponentForm.SetIActor(iActor);
            iParamForm.SetIActor(iActor);
            funcForm.SetIActor(iActor);
        };
    }
}
