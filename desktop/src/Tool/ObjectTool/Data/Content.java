package Tool.ObjectTool.Data;

import GDX11.IObject.IActor.*;
import GDX11.Reflect;
import GDX11.Util;
import Tool.JFrame.UI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Content {
    public static Class[] GetTypes()
    {
        Class[] types = {IGroup.class, IImage.class, ILabel.class, ITable.class, IActor.class};
        return types;
    }

    public static XActor InitIActor(IActor iActor, JPanel panel)
    {
        if (iActor instanceof IGroup) return new XGroup(iActor, panel);
        return new XActor(iActor,panel);
    }

    static class XActor
    {
        protected List<String> fields;
        public XActor(IActor iActor, JPanel panel)
        {
            UI.NewLabel("type:"+iActor.getClass().getSimpleName(),panel).setForeground(Color.BLUE);
            fields = UI.GetFields(iActor);
            ExcludeFields();
            UI.InitComponents(fields,iActor,panel);
            UI.NewCheckBox("debug",iActor.GetActor().getDebug(),panel,vl->iActor.GetActor().setDebug(vl));
        }
        protected void ExcludeFields()
        {
            fields.removeAll(Arrays.asList("iParam","iSize","iPos","iRun","iAction","iComponents","name"));
        }
    }
    static class XGroup extends XActor
    {
        public XGroup(IActor iActor, JPanel panel) {
            super(iActor, panel);
        }

        @Override
        protected void ExcludeFields() {
            super.ExcludeFields();
            fields.removeAll(Arrays.asList("iMap"));
        }
    }
}
