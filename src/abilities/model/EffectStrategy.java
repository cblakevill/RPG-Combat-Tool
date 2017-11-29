package abilities.model;

import character.model.RPGCharacter;
import character.model.Targetable;

import java.util.List;

/**
 * Restricts who the ability is able to target and determines what effect the ability has on the target
 */
public interface EffectStrategy
{
    List<Targetable> restrictTargets(RPGCharacter character, List<Targetable> targets);
    void doEffect(RPGCharacter initiator, Targetable target, Ability ability);
}
