package Tool.ObjectToolV2.Core;

import Extend.Spine.GSpine;
import Extend.Spine.ISpine;
import GDX11.Actors.Particle;
import GDX11.IObject.IActor.*;
import GDX11.Reflect;
import Tool.Swing.UI;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class JActor {
    public static Runnable reselect;
    public static JActor InitIActor(IActor iActor, JPanel panel)
    {
        if (iActor instanceof IGroup) return new JGroup(iActor, panel);
        if (iActor instanceof ISpine) return new JSpine(iActor, panel);
        if (iActor instanceof IParticle) return new JParticle(iActor, panel);
        return new JActor(iActor,panel);
    }
    protected List<String> fields;
    public JActor(IActor iActor, JPanel panel)
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

    static class JGroup extends JActor
    {
        public JGroup(IActor iActor, JPanel panel) {
            super(iActor, panel);
        }

        @Override
        protected void ExcludeFields() {
            super.ExcludeFields();
            fields.removeAll(Arrays.asList("iMap"));
        }
    }
    static class JSpine extends JActor
    {
        public JSpine(IActor iActor, JPanel panel) {
            super(iActor, panel);
            ISpine iSpine = (ISpine)iActor;
            GSpine gSpine = iSpine.GetActor();
            Reflect.AddEvent(iSpine,Reflect.GetField(ISpine.class,"spine"), vl->{
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
    static class JParticle extends JActor{

        public JParticle(IActor iActor, JPanel panel) {
            super(iActor, panel);
            Particle par = iActor.GetActor();
            UI.NewButton("Play",panel, par::Play);
        }
    }
}
