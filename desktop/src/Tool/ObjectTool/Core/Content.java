package Tool.ObjectTool.Core;

import Extend.Spine.GSpine;
import Extend.Spine.ISpine;
import GDX11.IObject.IActor.ITextField;
import Extend.PagedScroll.IPagedScroll;
import GDX11.IObject.IActor.*;
import GDX11.Reflect;
import Tool.Swing.UI;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Content {
    public static Runnable reselect;
    public static Class[] GetTypes()
    {
        Class[] types = {IGroup.class, IImage.class, ILabel.class, ITable.class, IActor.class, ISpine.class,
        IScrollImage.class, IProgressBar.class,IScrollPane.class, IPagedScroll.class, ITextField.class};
        return types;
    }

    public static XActor InitIActor(IActor iActor, JPanel panel)
    {
        if (iActor instanceof IGroup) return new XGroup(iActor, panel);
        if (iActor instanceof ISpine) return new XSpine(iActor, panel);
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
            fields.removeAll(Arrays.asList("iParam","iSize","iPos","iEvent","iAction","iComponents","name"));
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
    static class XSpine extends XActor
    {
        public XSpine(IActor iActor, JPanel panel) {
            super(iActor, panel);
            ISpine iSpine = (ISpine)iActor;
            GSpine gSpine = iSpine.GetActor();
            Reflect.AddEvent(iSpine,Reflect.GetField(ISpine.class,"spine"),vl->{
                iSpine.MakeNew();
                reselect.run();
            });
            UI.NewComboBox("skin",gSpine.GetSkinNames(),iSpine.skin,panel,vl->{
                iSpine.skin = vl;
                gSpine.SetSkin(vl);
            });
            UI.NewComboBox("ani",gSpine.GetAnimationNames(),iSpine.animation,panel,vl->{
                iSpine.animation = vl;
                gSpine.SetAnimation(vl,true);
            });
        }
        @Override
        protected void ExcludeFields() {
            super.ExcludeFields();
            fields.removeAll(Arrays.asList("skin","animation"));
        }
    }
}
