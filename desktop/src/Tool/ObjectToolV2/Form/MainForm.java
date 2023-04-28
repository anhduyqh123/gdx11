package Tool.ObjectToolV2.Form;

import GDX11.Config;

import javax.swing.*;
import java.awt.*;

public class MainForm {
    public JPanel panel1;
    private JSplitPane pnTop;
    private JSplitPane pnBot;
    private JTabbedPane tabbedPane1;
    private JPanel pnHeader;

    public MainForm()
    {
        pnTop.setMinimumSize(new Dimension(Config.Get("screen_width"),Config.Get("screen_height")));
        pnTop.setRightComponent(new ViewForm(this::Install).panel1);
    }
    private void Install()
    {

        IObjectForm iObjectForm = new IObjectForm();
        IActorForm iActorForm = new IActorForm();
        IActionForm iActionForm = new IActionForm();
        IComponentForm iComponentForm = new IComponentForm();
        IParamForm iParamForm = new IParamForm();

        HeaderForm headerForm = new HeaderForm(iObjectForm::SetData);
        pnHeader.add(headerForm.panel1);

        pnTop.setLeftComponent(iObjectForm.panel1);
        tabbedPane1.addTab("Info",iActorForm.panel1);
        tabbedPane1.addTab("Action",iActionForm.panel1);
        tabbedPane1.addTab("Component",iComponentForm.panel1);
        tabbedPane1.addTab("Param",iParamForm.panel1);

        iObjectForm.onSelectIActor = iActor->{
            headerForm.SetIActor(iActor);
            iActorForm.SetIActor(iActor);
            iActionForm.SetIActor(iActor);
            iComponentForm.SetIActor(iActor);
            iParamForm.SetIActor(iActor);
        };

        AssetForm assetForm = new AssetForm();
        pnBot.setRightComponent(assetForm.panel1);
    }
}
