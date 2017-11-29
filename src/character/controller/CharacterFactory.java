package character.controller;

import character.model.CharacterObserver;
import character.model.NonPlayerCharacter;
import character.model.PlayerCharacter;
import character.model.RPGCharacter;

import java.util.List;

/**
 * Factory for creating characters.
 */
public class CharacterFactory
{
    private RPGCharacter testCharacter;
    private List<CharacterObserver> characterObservers;

    public CharacterFactory(List<CharacterObserver> characterObservers)
    {
        testCharacter = null;
        this.characterObservers = characterObservers;
    }

    /**
     * Set a test character for unit testing purposes only
     * @param testCharacter mock character object
     */
    public void setTestCharacter(RPGCharacter testCharacter)
    {
        this.testCharacter = testCharacter;
    }

    /**
     * Creates a character as either a player or non-player character
     * @param type character type
     * @return created character
     */
    public RPGCharacter makeCharacter(char type)
    {
        RPGCharacter character = null;

        if(testCharacter == null) //no test character - continue as normal
        {
            if (type == RPGCharacter.PLAYER)
                character = new PlayerCharacter();

            else if (type == RPGCharacter.NON_PLAYER)
                character = new NonPlayerCharacter();

            character.setCharacterObservers(characterObservers);
        }
        else
        {
            character = testCharacter;
        }

        return character;
    }
}
