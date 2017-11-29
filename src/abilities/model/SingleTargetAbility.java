package abilities.model;

import character.model.Targetable;
import game.model.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Single target ability strategy. Returns all active characters as individuals
 */
public class SingleTargetAbility implements TargetStrategy
{
    @Override
    public List<Targetable> getTargets(Ability ability, GameState gameState)
    {
        //Return as arraylist so characters can be selected via index
        List<Targetable> targets = new ArrayList<>();
        for(Targetable target : gameState.getActiveCharacters())
        {
            targets.add(target);
        }
        return targets;
    }
}
