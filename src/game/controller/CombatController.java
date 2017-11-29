package game.controller;

import abilities.model.*;
import character.model.RPGCharacter;
import character.model.Targetable;
import game.model.GameState;
import view.UserInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for handling combat
 */
public class CombatController
{
    private GameState gameState;
    private UserInterface ui;
    private Map<Character, TargetStrategy> targetStrategies;
    private Map<Character, EffectStrategy> effectStrategies;

    public CombatController(GameState gameState, UserInterface ui)
    {
        this.gameState = gameState;
        this.ui = ui;

        targetStrategies = new HashMap<>();
        targetStrategies.put(Ability.SINGLE, new SingleTargetAbility());
        targetStrategies.put(Ability.MULTI, new MultiTargetAbility());

        effectStrategies = new HashMap<>();
        effectStrategies.put(Ability.DAMAGE, new DamageAbility());
        effectStrategies.put(Ability.HEAL, new HealAbility());
    }

    public void performAbility(RPGCharacter character)
    {

        //select the ability to be performed
        int abilitySelect = ui.selectAbility(character.getAbilities());
        Ability ability = character.getAbility(abilitySelect);

        //get the relevant strategies for the selected ability
        TargetStrategy targeting = targetStrategies.get(ability.getTarget());
        EffectStrategy effect = effectStrategies.get(ability.getType());

        //get the target target type (character/team)
        List<Targetable> targets = targeting.getTargets(ability, gameState);
        //restrict the targets depending on the ability type (heal/damage)
        targets = effect.restrictTargets(character, targets);

        //select the target to apply it to
        int targetSelect = ui.selectTarget(targets);
        Targetable target = targets.get(targetSelect);

        //perform the ability
        effect.doEffect(character, target, ability);
    }
}
