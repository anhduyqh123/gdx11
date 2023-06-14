package GDX11.IObject.IActor;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IParam;
import GDX11.Translate;
import GDX11.Util;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.HashMap;
import java.util.Map;

public class ILabel extends IActor{

    public String font = "font";
    public String text = "text";
    public String alignment = "center";
    public float fontScale = 1f;
    public boolean bestFix,wrap;

    public ILabel()
    {
        iSize.width = "dw";//default of width
        iSize.height = "dh";//default of height
    }
    //IActor

    @Override
    protected Actor NewActor() {
        return new Label(text,GetStyle(font)){
            @Override
            public void act(float delta) {
                super.act(delta);
                OnUpdate(delta);
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                OnDraw(batch,parentAlpha,()->super.draw(batch, parentAlpha));
            }

            @Override
            public boolean remove() {
                OnRemove();
                return super.remove();
            }
        };
    }
    @Override
    public void Connect() {
        super.Connect();
        DefaultEvent();
    }

    @Override
    public void BaseRefresh() {
        Label lb = GetActor();
        lb.setText(GetText());
        SetFont(font);
        lb.setFontScale(fontScale);
        super.BaseRefresh();
    }

    @Override
    public void RefreshContent() {
        Label lb = GetActor();
        lb.setText(GetText());
        SetFont(font);
        lb.setFontScale(fontScale);
        lb.setWrap(wrap);
        lb.setAlignment(IParam.GetAlign(alignment));
        if (bestFix) BestFix(GetActor());
    }

    @Override
    public void RefreshLanguage() {
        Label lb = GetActor();
        lb.setText(GetText());
        if (bestFix) BestFix(GetActor());
    }

    //ILabel
    protected void DefaultEvent()
    {
        iParam.Set("dw",(GDX.Func<Object>) () ->GetLabel().getPrefWidth());
        iParam.Set("dh",(GDX.Func<Object>) () ->GetLabel().getPrefHeight());
    }
    public void SetText(Object text)
    {
        Label lb = GetActor();
        lb.setText(text+"");
        if (bestFix) BestFix(lb);
    }
    public void ReplaceText(Object value)//default %
    {
        ReplaceText(value,"%");
    }
    public void ReplaceText(Object value,String target)//target = %
    {
        String txt = GetText();
        Label lb = GetActor();
        lb.setText(txt.replace(target,value+""));
    }
    public Label GetLabel()
    {
        return GetActor();
    }
    public String GetText()
    {
        return GetRealText(text);
    }
    public void SetFont(String fontName)
    {
        Label lb = GetActor();
        lb.setStyle(GetStyle(fontName));
    }
    private String GetFormat(String name)
    {
        String key = "format_"+name;
        Object ob = iParam.Get(name);
        if (iParam.Has(key)) return Format(iParam.Get(key),ob);
        return ob+"";
    }
    private String Format(String key,Object ob)
    {
        if (key.equals("int")) return (int)ob+"";
        return String.format(key,ob).replace("\n","").replace("\r","");
    }
    protected String GetSingle(String text)
    {
        if (iParam.Has(text)){
            iParam.SetChangeEvent(text,()-> GetLabel().setText(GetText()));
            return GetFormat(text);
        }
        if (Translate.i.HasKey(text)) return Translate.i.Get(text);
        return text;
    }
    protected String GetMulti(String text)
    {
        Map<String,String> map = new HashMap<>();
        text = Util.FindString(text,"{","}",map);
        for (String key : map.keySet())
        {
            String value = map.get(key).replace("{","").replace("}","");
            String single = GetSingle(value);
            text = text.replace(key,single);
        }
        return text;
    }
    protected String GetRealText(String text)
    {
        if (text.contains("{")) return GetMulti(text);
        return GetSingle(text);
    }

    //static
    private static Label.LabelStyle GetStyle(String fontName)
    {
        return new Label.LabelStyle(GetFont(fontName), Color.WHITE);
    }
    protected static BitmapFont GetFont(String fontName)
    {
        return Asset.i.GetFont(fontName.equals("")?"font":fontName);
    }

    protected static void BestFix(Label label)
    {
        if (label.getWidth()==0){
            GDX.Error(label.getText()+":can't fix cause width=0");
            return;
        }
        float scale = label.getWidth()/label.getPrefWidth();
        if (label.getWrap())
        {
            label.validate();
            float w = label.getGlyphLayout().width,h=label.getGlyphLayout().height,w0=label.getWidth(),h0=label.getHeight();
            scale = (float) Math.sqrt((w0*h0)/(w*h));
            if (scale>1) scale = Math.min(w0/w,h0/h);
        }
        if (scale>1) scale = 1;
        label.setFontScale(scale*label.getFontScaleX());
    }
    public static Label NewLabel(String text, float x, float y, int align, Group parent)
    {
        Label lb = new Label(text,GetStyle("font"));
        lb.setSize(lb.getPrefWidth(),lb.getPrefHeight());
        lb.setPosition(x,y,align);
        parent.addActor(lb);
        return lb;
    }
}
