package fileio;

import abilities.model.Ability;

import java.util.Collection;

/**
 * Saves all abilities to a file in csv format
 */
public class AbilityWriter extends FileWriter<Ability>
{
     public AbilityWriter(Collection<Ability> collection)
    {
        super(collection);
    }


    @Override
    protected String heading()
    {
        return "Type,Name,Target,Base,NumDice,Faces";
    }

    @Override
    protected String writeLine(Ability ability)
    {
        return ability.getType() + ","
                + ability.getName() + ","
                + ability.getTarget() + ","
                + ability.getBaseAffect() + ","
                + ability.getDiceRolls() + ","
                + ability.getDiceFaces();
    }
}
