package Extend.AI;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IComponent.IComponent;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.math.*;

public class ITest extends IComponent {
    private transient SteeringAgent character;

    @Override
    public void SetIActor(IActor iActor) {
        super.SetIActor(iActor);
    }

    @Override
    public void Refresh() {
        character = new SteeringAgent(GetActor(),false);
        character.setMaxLinearSpeed(150);
        character.setMaxLinearAcceleration(200);

        Wander<Vector2> wanderSB = new Wander<Vector2>(character) //
                // Don't use Face internally because independent facing is off
                .setFaceEnabled(false) //
                // No need to call setAlignTolerance, setDecelerationRadius and setTimeToTarget for the same reason
                .setLimiter(new LinearAccelerationLimiter(30)) //
                .setWanderOffset(60) //
                .setWanderOrientation(0) //
                .setWanderRadius(40) //
                .setWanderRate(MathUtils.PI2 * 4);
        character.setSteeringBehavior(wanderSB);

    }

    @Override
    public void Update(float delta) {
        if (character ==null) return;
        character.Update(delta);
    }
}
