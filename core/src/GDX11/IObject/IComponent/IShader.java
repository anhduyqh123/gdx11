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
    public String vertName = "";
    public List<String> uniforms = new ArrayList<>();
    protected transient ShaderProgram shader;
    protected transient Vector2 resolution = new Vector2();
    @Override
    public void Refresh() {
        GDX.PostRunnable(()->GDX.Try(this::Init));
    }
    protected void Init()
    {
        resolution.set(GetActor().getWidth(),GetActor().getHeight());
        ShaderProgram.pedantic = false;
        shader = NewShader();
    }
    protected ShaderProgram NewShader()
    {
        String fragment = GDX.GetString(Asset.i.GetNode(fragName).url);
        if (vertName.equals(""))
        {
            Batch batch = GetActor().getStage().getBatch();
            return new ShaderProgram(batch.getShader().getVertexShaderSource(),fragment);
        }
        String vertex = GDX.GetStringByKey(vertName);
        return new ShaderProgram(vertex,fragment);
    }

    @Override
    public void Draw(Batch batch, float parentAlpha, Runnable superDraw) {
        if (shader==null){
            superDraw.run();
            return;
        }
        batch.setShader(shader);
        shader.bind();
        DefaultUniform();
        UpdateUniform();
        superDraw.run();

        batch.setShader(null);
    }
    protected void DefaultUniform()
    {
        shader.setUniformf("resolution", resolution);
    }
    protected void UpdateUniform()
    {
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
            if (n.startsWith("v4_"))
            {
                GDX.Vector4 v4 = (GDX.Vector4) ob;
                shader.setUniformf(n,v4.x,v4.y,v4.z,v4.w);
            }
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
