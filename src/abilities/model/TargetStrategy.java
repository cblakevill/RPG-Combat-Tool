package abilities.model;

import character.model.Targetable;
import game.model.GameState;

import java.util.List;

/**
 * Determines whether the ability can apply to individual characters or teams
 */
public interface TargetStrategy
{
    //return set as a list so targets can be accessed via an index
    List<Targetable> getTargets(Ability ability, GameState gameState);
}
