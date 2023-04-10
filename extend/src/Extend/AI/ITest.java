package Extend.AI;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IComponent.IComponent;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ITest extends IComponent {
    private transient SteeringAgent character;

    @Override
    public void SetIActor(IActor iActor) {
        super.SetIActor(iActor);
    }

    @Override
    public void Refresh() {
        character = new SteeringAgent(GetActor(),false);
        character.setMaxLinearSpeed(100);
        character.setMaxLinearAcceleration(250);
        character.setMaxAngularAcceleration(0); // used by Wander; set to 0 because independent facing is disabled
        character.setMaxAngularSpeed(5);

        Wander<Vector2> wanderSB = new Wander<Vector2>(character) //
                // Don't use Face internally because independent facing is off
                .setFaceEnabled(false) //
                // No need to call setAlignTolerance, setDecelerationRadius and setTimeToTarget for the same reason
                .setWanderOffset(110) //
                .setWanderOrientation(10) //
                .setWanderRadius(64) //
                .setWanderRate(MathUtils.PI2 * 3.5f);
        character.setSteeringBehavior(wanderSB);

    }

    @Override
    public void Update(float delta) {
        if (character ==null) return;
        character.Update(delta);
    }
}
