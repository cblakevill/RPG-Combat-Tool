package abilities.model;

import java.util.Random;

/**
 * Container class for ability characteristics and attributes
 */
public class Ability
{
    //Constants stating the ability type
    public static final char HEAL = 'H';
    public static final char DAMAGE = 'D';

    //Constants stating the ability's targeting type
    public static final char SINGLE = 'S';
    public static final char MULTI = 'M';

    private String name;
    private int baseAffect;
    private int diceRolls;
    private int diceFaces;
    private char type;
    private char target;

    public Ability()
    {
        name = null;
        baseAffect = 0;
        diceRolls = 0;
        diceFaces = 0;
        type = '\0';
        target = '\0';
    }

    public Ability(String name, int baseAffect, int diceRolls, int diceFaces, char type, char target)
    {
        this.name = name;
        this.baseAffect = baseAffect;
        this.diceRolls = diceRolls;
        this.diceFaces = diceFaces;
        this.type = type;
        this.target = target;
    }

    /**
     * Gives a value determined by baseAffect, diceRolls and diceFaces
     * @return value rolled
     */
    public int roll()
    {
        Random r = new Random();

        int affect = baseAffect;
        for (int i = 0; i < diceRolls; i++)
        {
            affect += r.nextInt(diceFaces) + 1;
        }
        return affect;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getBaseAffect()
    {
        return baseAffect;
    }

    public void setBaseAffect(int baseAffect)
    {
        this.baseAffect = baseAffect;
    }

    public int getDiceRolls()
    {
        return diceRolls;
    }

    public void setDiceRolls(int diceRolls)
    {
        this.diceRolls = diceRolls;
    }

    public int getDiceFaces()
    {
        return diceFaces;
    }

    public void setDiceFaces(int diceFaces)
    {
        this.diceFaces = diceFaces;
    }

    public char getType()
    {
        return type;
    }

    public void setType(char type)
    {
        this.type = type;
    }

    public char getTarget()
    {
        return target;
    }

    public void setTarget(char target)
    {
        this.target = target;
    }
}
