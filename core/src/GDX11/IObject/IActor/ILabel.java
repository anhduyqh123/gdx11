package GDX11.IObject.IActor;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IParam;
import GDX11.Translate;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.List;

public class ILabel extends IActor{

    public String font = "";
    public String text = "text";
    public String alignment = "center";
    public float fontScale = 1f;
    public boolean bestFix,wrap,multiLanguage;

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
    protected void Connect() {
        super.Connect();
        iRun.SetFunc("dw",()->GetLabel().getPrefWidth());//width of texture
        iRun.SetFunc("dh",()->GetLabel().getPrefHeight());//height of texture
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
        if (!multiLanguage) return;
        Label lb = GetActor();
        lb.setText(GetText());
        if (bestFix) BestFix(GetActor());
    }

    //ILabel
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
        if (multiLanguage) return GDX.Try(()->GetTranslate(text),()->text);
        return text;
    }
    private Label.LabelStyle GetStyle(String fontName)
    {
        return new Label.LabelStyle(GetFont(fontName), Color.WHITE);
    }
    private BitmapFont GetFont(String fontName)
    {
        return Asset.i.GetFont(fontName.equals("")?"font":fontName);
    }
    public void SetFont(String fontName)
    {
        Label lb = GetActor();
        lb.setStyle(GetStyle(fontName));
    }

    //static
    public static String GetTranslate(String text)//{key}->en-hello,vi->xin chao;
    {
        List<String> keys = GetKeys(text);
        if (keys.size()<=0) return Translate.i.Get(text);
        String txt = text;
        for(String key : keys) txt = txt.replace(key,Translate.i.Get(GetValue(key)));
        return txt;
    }
    private static String GetValue(String value)
    {
        return value.substring(1,value.length()-1);
    }
    private static List<String> GetKeys(String data)
    {
        List<String> list = new ArrayList<>();
        while (true)
        {
            String key = GetKey(data);
            if (key==null) return list;
            list.add(key);
            data = data.replace(key,"");
        }
    }
    private static String GetKey(String data)
    {
        try {
            int s = data.indexOf("{");
            int e = data.indexOf("}");
            if (s==-1 || e==-1) return null;
            return data.substring(s,e+1);
        }catch (Exception ignored){}
        return null;
    }
    private static void BestFix(Label label)
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
}
