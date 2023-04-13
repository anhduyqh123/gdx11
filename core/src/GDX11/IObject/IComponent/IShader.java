package GDX11.IObject.IComponent;

import GDX11.Asset;
import GDX11.Config;
import GDX11.GDX;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class IShader extends IComponent {
    public String fragName = "";
    public List<String> uniforms = new ArrayList<>();
    private transient ShaderProgram shader;
    private transient Vector2 size = new Vector2();
    @Override
    public void Refresh() {
        GDX.PostRunnable(this::Init);
    }
    private void Init()
    {
        size.set(GetActor().getWidth(),GetActor().getHeight());
        GDX.Try(()->{
            ShaderProgram.pedantic = false;
            Batch batch = GetActor().getStage().getBatch();
            String fragment = GDX.GetString(Asset.i.GetNode(fragName).url);
//            String vertex = GDX.GetStringFromName("vertex");
//            ShaderProgram shader = new ShaderProgram(vertex,fragment);
            shader = new ShaderProgram(batch.getShader().getVertexShaderSource(),fragment);
        });
    }

    @Override
    public void Draw(Batch batch, float parentAlpha, Runnable superDraw) {
        if (shader==null){
            superDraw.run();
            return;
        }
        batch.setShader(shader);
        shader.bind();
        UpdateValue();
        superDraw.run();

        batch.setShader(null);
    }
    private void UpdateValue()
    {
        shader.setUniformf("size",size);

        for (String n : uniforms)
        {
            Object ob = GetUniform(n);
            if (ob==null) continue;
            if (n.startsWith("i_"))
                shader.setUniformi(n,(int) ob);
            if (n.startsWith("f_"))
                shader.setUniformf(n,(float)ob);
            if (n.startsWith("v2_"))
                shader.setUniformf(n,(Vector2) ob);
            if (n.startsWith("v3_"))
                shader.setUniformf(n,(Vector3)ob);
            if (n.startsWith("cl_"))
                shader.setUniformf(n,(Color) ob);
        }
    }
    private <T> T GetUniform(String uni)
    {
        Object ob = Config.Has(uni)?Config.Get(uni):GetIActor().iParam.Get(uni);
        if (ob instanceof String) return GetUniform((String) ob);
        if (ob instanceof GDX.Func) return (T)((GDX.Func<?>) ob).Run();
        return (T)ob;
    }
}
