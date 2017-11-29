package abilities.model;

import character.model.RPGCharacter;
import character.model.Targetable;

import java.util.Iterator;
import java.util.List;

/**
 * Heal ability strategy. Makes abilities target exclusively allies and effect positively impact health
 */
public class HealAbility implements EffectStrategy
{
    /**
     * Removes targets from the list that the character is not allied with
     * @param character character performing the ability
     * @param targets targets retrieved from initial targeting stage
     * @return list of targets not allied with the character
     */
    @Override
    public List<Targetable> restrictTargets(RPGCharacter character, List<Targetable> targets)
    {
        Iterator<Targetable> targetItr = targets.iterator();
        Targetable target;

        while(targetItr.hasNext())
        {
            target = targetItr.next();

            if(!target.alliedWith(character))
                targetItr.remove();
        }

        return targets;
    }

    /**
     * Positively affect the targets hp
     * @param initiator Character performing the ability
     * @param target Target the ability is being applied to
     * @param ability ability being performed
     */
    @Override
    public void doEffect(RPGCharacter initiator, Targetable target, Ability ability)
    {
        target.changeHealth(initiator, ability.roll());
    }
}
