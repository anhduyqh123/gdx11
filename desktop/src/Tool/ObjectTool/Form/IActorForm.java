package Tool.ObjectTool.Form;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.ISize;
import GDX11.Reflect;
import Tool.JFrame.UI;
import Tool.JFrame.WrapLayout;
import Tool.ObjectTool.Data.Content;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IActorForm {
    public JPanel panel1;
    private JPanel pnContent;
    private JPanel pInfo;
    private JPanel pSize;
    private JPanel pPos;

    private IActor iActor;

    public IActorForm()
    {
        pInfo.setLayout(new WrapLayout());

//        UI.Button(btRefresh,()-> {
//            iActor.Refresh();
//            SetIActor(iActor);
//        });
    }
    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
        UI.ClearPanel(pInfo);
        UI.ClearPanel(pSize);
        UI.ClearPanel(pPos);

        Content.InitIActor(iActor,pInfo);
        InitISize();
        InitPosition();
    }
    private void InitISize()
    {
        ISize iSize = iActor.iSize;
        UI.NewComboBox("width",iSize,SizeValues(iSize.width),pSize).setEditable(true);
        UI.NewComboBox("height",iSize,SizeValues(iSize.height),pSize).setEditable(true);
        UI.NewAlignComboBox("origin",iSize,pSize).setEditable(true);
        UI.InitComponents(Arrays.asList("scale","rotate"),iSize,pSize);
        UI.NewLabel("size:"+(int)iActor.GetActor().getWidth()+"-"+(int)iActor.GetActor().getHeight(),pSize);
    }
    private void InitPosition()
    {
        UI.InitComponents(Arrays.asList("coordActor","x","y"),iActor.iPos,pPos);
        UI.NewAlignComboBox("align",iActor.iPos,pPos);
    }
    private String[] SizeValues(String value0)
    {
        if (iActor.GetIMap()!=null)
        {
            List<String> list = new ArrayList<>(iActor.GetIMap().GetMap().keySet());
            list.add(0,value0);
            return list.toArray(new String[0]);
        }
        return new String[]{value0};
    }
}
