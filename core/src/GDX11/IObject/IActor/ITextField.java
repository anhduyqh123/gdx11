package GDX11.IObject.IActor;

import GDX11.IObject.IActor.ILabel;
import GDX11.IObject.IParam;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class ITextField extends ILabel {

    public ITextField()
    {
        iSize.width = "200";//default of width
        iSize.height = "80";//default of height
    }

    @Override
    protected void DefaultEvent() {

    }
    @Override
    protected void Clear() {//don't clear event listener, because of textField input listener
    }

    @Override
    protected Actor NewActor() {
        return new TextField(text,GetStyle(font));
    }

    @Override
    public void RefreshContent() {
        TextField tf = GetActor();
        tf.setStyle(GetStyle(font));
        tf.setText(GetText());
        tf.setAlignment(IParam.GetAlign(alignment));
    }
    //Text Field

    //static
    private static TextField.TextFieldStyle GetStyle(String font)
    {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = GetFont(font);
        style.fontColor = Color.WHITE;
        return style;
    }
    public static TextField NewTextField(String text, float x, float y, int align, Group parent)
    {
        TextField tf = new TextField(text,GetStyle("font"));
        tf.setSize(500,tf.getPrefHeight());
        tf.setPosition(x,y,align);
        parent.addActor(tf);
        return tf;
    }
}
