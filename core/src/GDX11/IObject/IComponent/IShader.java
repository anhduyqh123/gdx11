package GDX11.IObject.IComponent;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class IShader extends IComponent {
    public String fragName = "";
    private transient ShaderProgram shader;
    @Override
    public void Refresh() {
        GDX.PostRunnable(this::Init);

    }
    private void Init()
    {
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
    public void Draw(Batch batch, float parentAlpha, Runnable onDraw) {
        if (shader==null){
            onDraw.run();
            return;
        }
        batch.setShader(shader);

        shader.bind();
        UpdateValue();
        onDraw.run();

        batch.setShader(null);
    }
    private void UpdateValue()
    {
        IActor iActor = GetIActor();
        for (String n : iActor.iParam.GetData().keySet())
        {
            if (n.startsWith("i_"))
                shader.setUniformi(n,iActor.iParam.Get(n,0));
            if (n.startsWith("f_"))
                shader.setUniformf(n,iActor.iParam.Get(n,0f));
            if (n.startsWith("v2_"))
                shader.setUniformf(n,iActor.iParam.Get(n,new Vector2()));
            if (n.startsWith("v3_"))
                shader.setUniformf(n,iActor.iParam.Get(n,new Vector3()));
            if (n.startsWith("cl_"))
                shader.setUniformf(n,iActor.iParam.Get(n, Color.WHITE));
        }
    }
}
