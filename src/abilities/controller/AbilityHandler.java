package abilities.controller;

import abilities.model.Ability;

import java.util.HashMap;
import java.util.Map;

/**
 *  Handler for storing abilities loaded in to the game
 */
public class AbilityHandler
{
    private Map<String, Ability> abilites;

    public AbilityHandler()
    {
        abilites = new HashMap<>();
    }

    /**
     * Get ability based on its name
     * @param ability name of ability
     * @return ability object
     */
    public Ability getAbility(String ability)
    {
        return abilites.get(ability);
    }

    /**
     * Add a new ability to the handler
     * @param ability ability to add
     */
    public void addAbility(Ability ability)
    {
        abilites.put(ability.getName(), ability);
    }

    public Map<String, Ability> getAbilites()
    {
        return abilites;
    }
}
