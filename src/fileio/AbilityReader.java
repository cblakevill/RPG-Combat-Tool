package fileio;

import abilities.controller.AbilityHandler;
import abilities.model.Ability;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static abilities.model.Ability.*;

/**
 * File reader for creating abilities and storing them in the ability handler
 */
public class AbilityReader extends FileReader
{
    public static final int NUM_FIELDS = 6;

    private Set<Integer> validDiceFaces;
    private AbilityHandler abilityHandler;

    public AbilityReader(AbilityHandler abilityHandler)
    {
        this.abilityHandler = abilityHandler;
        validDiceFaces = new HashSet<>(Arrays.asList(2,3,4,6,8,10));
    }

    @Override
    protected void preprocess() throws FileFormatException
    {
        //no pre-processing required
    }

    /**
     * Split the line by comma, create the ability object and add it to the ability handler
     * @param line line to be processed
     * @throws FileFormatException
     */
    @Override
    protected void processLine(String line) throws FileFormatException
    {
        String[] split = line.split(",(\\s)?");

        if(split.length != NUM_FIELDS)
            throw new FileFormatException("Invalid number of fields in ability file" + split.length);

        char abilityType = parseType(split[0]);
        String name = parseName(split[1]);
        char target = parseTarget(split[2]);
        int base = parseBase(split[3]);
        int numDice = parseNumDice(split[4]);
        int diceFaces = parseDiceFaces(split[5]);

        Ability ability = new Ability();

        ability.setType(abilityType);
        ability.setName(name);
        ability.setTarget(target);
        ability.setBaseAffect(base);
        ability.setDiceRolls(numDice);
        ability.setDiceFaces(diceFaces);

        abilityHandler.addAbility(ability);
    }

    /**
     * validate the target type.
     * @param s
     * @return
     * @throws FileFormatException
     */
    private char parseTarget(String s) throws FileFormatException
    {
        char target = s.charAt(0);

        if(target != SINGLE && target != MULTI)
            throw new FileFormatException("Invalid target type");

        return target;
    }

    /**
     * validate the ability type.
     * @param s
     * @return
     * @throws FileFormatException
     */
    private char parseType(String s) throws FileFormatException
    {
        char type = s.charAt(0);

        if(type != DAMAGE && type != HEAL)
            throw new FileFormatException("Invalid ability type");


        return type;
    }

    /**
     * validate the the number of dice faces.
     * @param s
     * @return
     * @throws FileFormatException
     */    
    private int parseDiceFaces(String s) throws FileFormatException
    {
        int diceFaces;

        try
        {
            diceFaces = Integer.parseInt(s);
        }
        catch(NumberFormatException e)
        {
            throw new FileFormatException("invalid dice face value in abilities file", e);
        }

        if(!validDiceFaces.contains(diceFaces))
            throw new FileFormatException("dice face value must be between in the set {2,3,4,6,8,10}");

        return diceFaces;
    }

    /**
     * validate the the number of dice.
     * @param s
     * @return
     * @throws FileFormatException
     */
    private int parseNumDice(String s) throws FileFormatException
    {
        int numDice;

        try
        {
            numDice = Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            throw new FileFormatException("invalid number of dice value in abilities file", e);
        }

        if (numDice < 1 || numDice > 10)
            throw new FileFormatException("number of dice value must be between 1-10");

        return numDice;
    }

    /**
     * validate the the ability's base effect
     * @param s
     * @return
     * @throws FileFormatException
     */
    private int parseBase(String s) throws FileFormatException
    {
        int base;

        try
        {
            base = Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            throw new FileFormatException("invalid base value in abilities file", e);
        }

        if (base < 0)
            throw new FileFormatException("base value must be positive");

        return base;
    }

    /**
     * validate the ability's name
     * @param s
     * @return
     * @throws FileFormatException
     */
    private String parseName(String s) throws FileFormatException
    {
        if(s.equals(""))
        {
            throw new FileFormatException("Ability must be assigned a name");
        }

        return s;
    }
}
