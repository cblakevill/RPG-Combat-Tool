package abilities.model;

import character.model.Targetable;
import game.model.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Multi-target ability strategy. Returns all active teams currently in game
 */
public class MultiTargetAbility implements TargetStrategy
{
    @Override
    public List<Targetable> getTargets(Ability ability, GameState gameState)
    {
        //Return as arraylist so teams can be selected via index
        List<Targetable> targets = new ArrayList<>();
        for(Targetable target : gameState.getActiveTeams())
        {
            targets.add(target);
        }
        return targets;
    }
}
