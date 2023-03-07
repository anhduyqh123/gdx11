package Tool.ObjectTool.Form;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import Tool.JFrame.UI;
import Tool.JFrame.WrapLayout;
import Tool.ObjectTool.Data.Content;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IActorForm {
    public JPanel panel1;
    private JButton btRefresh;
    private JPanel pnContent;
    private JPanel pInfo;
    private JPanel pSize;
    private JPanel pPos;

    private IActor iActor;

    public IActorForm()
    {
        pInfo.setLayout(new WrapLayout());

        UI.Button(btRefresh,()-> {
            iActor.Refresh();
            SetIActor(iActor);
        });
    }
    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
        UI.ClearPanel(pInfo);
        UI.ClearPanel(pSize);
        UI.ClearPanel(pPos);

        Content.InitIActor(iActor,pInfo);
        InitISize();
        //UI.InitComponents(Arrays.asList("width","height","origin","scale","rotate"),iActor.iSize,pSize);
        UI.NewLabel("size:"+(int)iActor.GetActor().getWidth()+"-"+(int)iActor.GetActor().getHeight(),pSize);
        UI.InitComponents(iActor.iPos,pPos);
    }
    private void InitISize()
    {
        UI.NewComboBox("width",SizeValues(iActor.iSize.width),iActor.iSize.width,pSize,
                vl-> iActor.iSize.width = vl).setEditable(true);
        UI.NewComboBox("height",SizeValues(iActor.iSize.height),iActor.iSize.height,pSize,
                vl-> iActor.iSize.height = vl).setEditable(true);
        UI.InitComponents(Arrays.asList("origin","scale","rotate"),iActor.iSize,pSize);
    }
    private String[] SizeValues(String value0)
    {
        if (iActor instanceof IGroup)
        {
            List<String> list = new ArrayList<>(iActor.GetIGroup().list);
            list.add(0,value0);
            return list.toArray(new String[0]);
        }
        return new String[]{value0};
    }
}
