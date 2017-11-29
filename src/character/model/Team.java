package character.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A group of characters forming a team. Currently, teams are determined by character type (Players vs. Non-Players)
 */
public class Team implements Targetable
{
    private String name;
    private Set<RPGCharacter> characters;
    private Set<RPGCharacter> activeCharacters;
    private Set<RPGCharacter> deadCharacters;

    public Team(String name)
    {
        this.name = name;

        characters = new HashSet<>();
        activeCharacters = new HashSet<>();
        deadCharacters = new HashSet<>();
    }

    /**
     * Affect the health of each team member that is alive
     * @param initiator character performing the ability
     * @param hp affect amount
     */
    @Override
    public void changeHealth(RPGCharacter initiator, int hp)
    {
        for (RPGCharacter character : characters)
        {
            if(!character.isDead())
                character.changeHealth(initiator, hp);
        }
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public List<RPGCharacter> getCharacters()
    {
        return new ArrayList<>(characters);
    }

    /**
     * Add character to the relevant character sets
     * @param character to be added
     */
    public void addCharacter(RPGCharacter character)
    {
        characters.add(character);

        if(character.isDead())
            deadCharacters.add(character);
        else
            activeCharacters.add(character);
    }

    public Set<RPGCharacter> getActiveCharacters()
    {
        return activeCharacters;
    }

    public Set<RPGCharacter> getDeadCharacters()
    {
        return deadCharacters;
    }

    /**
     * heal all active characters in the team
     */
    public void teamHeal()
    {
        for(RPGCharacter character : characters)
        {
            if(!character.isDead())
                character.teamHeal();
        }
    }

    /**
     * Check if a character is an allie of this team
     * @param character character to being checked whether ally or not
     * @return Returns true if the character is an ally
     */
    @Override
    public boolean alliedWith(RPGCharacter character)
    {
        return activeCharacters.contains(character);
    }
}
