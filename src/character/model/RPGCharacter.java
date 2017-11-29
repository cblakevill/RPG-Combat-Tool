package character.model;

import abilities.model.Ability;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Container class for character attributes
 */
public abstract class RPGCharacter implements Targetable
{
    public static final char PLAYER = 'P';
    public static final char NON_PLAYER = 'N';

    private List<CharacterObserver> characterObservers;
    private String name;
    private int currentHealth;
    private int maxHealth;
    private char type;
    private Team team;

    //List of abilities to be accessed by index.  Uniqueness ensured since abilities are added as a set.
    private List<Ability> abilities;

    public RPGCharacter()
    {
        characterObservers = new ArrayList<>();
        name = null;
        currentHealth = -1;
        maxHealth = -1;
        type = '\0';
        team = null;
    }

    public abstract void teamHeal();

    /**
     * change characters health by a given amount. Notify observers if character dies
     * @param initiator character causing health change
     * @param hp affect amount
     */
    @Override
    public void changeHealth(RPGCharacter initiator, int hp)
    {
        notifyHealthChange(initiator, hp);

        setCurrentHealth(currentHealth + hp);

        if(currentHealth == 0)
            notifyDeath(initiator);
    }

    /**
     * Set characters health to a new value bounded by zero and maxHealth
     * @param health new health value
     */
    public void setCurrentHealth(int health)
    {
        if(currentHealth != 0)
        {
            currentHealth = health;

            if(currentHealth > maxHealth)
                currentHealth = maxHealth;

            else if (currentHealth <= 0)
            {
                currentHealth = 0;
            }
        }
    }

    /**
     * Checks to see if a given character is allied with this character
     * @param character character to check if an ally
     * @return True if characters are allies
     */
    @Override
    public boolean alliedWith(RPGCharacter character)
    {
        return team.getActiveCharacters().contains(character);
    }

    /**
     * Check to see if this character is dead
     * @return Return true if character is dead
     */
    public boolean isDead()
    {
        return currentHealth == 0;
    }

    public Team getTeam()
    {
        return team;
    }

    public void setTeam(Team team)
    {
        this.team = team;
    }

    public void setType(char type)
    {
        this.type = type;
    }

    public char getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getCurrentHealth()
    {
        return currentHealth;
    }

    public void setCharacterObservers(List<CharacterObserver> characterObservers)
    {
        this.characterObservers = characterObservers;
    }

    public void addCharacterObserver(CharacterObserver observer)
    {
        characterObservers.add(observer);
    }

    public void removeCharacterObserver(CharacterObserver observer)
    {
        characterObservers.remove(observer);
    }

    public int getMaxHealth()
    {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth)
    {
        this.maxHealth = maxHealth;
    }

    public List<Ability> getAbilities()
    {
        return abilities;
    }

    public Ability getAbility(int select)
    {
        return abilities.get(select);
    }

    public void setAbilities(Set<Ability> abilities)
    {
        this.abilities = new ArrayList<>(abilities);
    }

    /**
     * Notify observer when this characters health has changed
     * @param initiator character who caused the health change
     * @param hp affect amount
     */
    private void notifyHealthChange(RPGCharacter initiator, int hp)
    {
        for(CharacterObserver observer : characterObservers)
        {
            observer.notifyHealthChange(initiator,this, hp);
        }
    }

    /**
     * Notify observers when this character has died and who did the killing
     * @param killer killer of this character
     */
    private void notifyDeath(RPGCharacter killer)
    {
        for(CharacterObserver observer: characterObservers)
        {
            observer.notifyDeath(killer,this);
        }
    }

    @Override
    public String toString()
    {
        if(currentHealth == 0)
            return name + " - ELIMINATED";
        else
            return name + " - " + currentHealth + "/" + maxHealth;
    }
}
