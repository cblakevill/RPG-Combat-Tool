package fileio;

import abilities.model.Ability;
import character.model.RPGCharacter;

import java.util.Collection;
import java.util.List;

/**
 * Writes each characters attributes and skills in csv format
 */
public class CharacterWriter extends FileWriter<RPGCharacter>
{
    public CharacterWriter(Collection<RPGCharacter> collection)
    {
        super(collection);
    }

    @Override
    protected String heading()
    {
        return "Type,Name,current HP,max HP,abilities";
    }

    @Override
    protected String writeLine(RPGCharacter character)
    {
        //create the abilities string
        StringBuilder abilities = new StringBuilder();
        List<Ability> abilityList = character.getAbilities();
        for(int i = 0; i < abilityList.size() - 1; i++)
        {
            abilities.append(abilityList.get(i).getName());
            abilities.append(',');
        }
        //append the last ability without the trailing comma
        abilities.append(abilityList.get(abilityList.size() - 1).getName());

        return character.getType() + ","
                + character.getName() + ","
                + character.getCurrentHealth() + ","
                + character.getMaxHealth() + ","
                + abilities;
    }
}
