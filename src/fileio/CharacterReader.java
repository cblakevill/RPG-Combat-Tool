package fileio;

import abilities.controller.AbilityHandler;
import abilities.model.Ability;
import character.controller.CharacterFactory;
import character.model.RPGCharacter;
import game.model.GameState;

import java.util.LinkedHashSet;
import java.util.Set;

import static character.model.RPGCharacter.NON_PLAYER;
import static character.model.RPGCharacter.PLAYER;

/**
 * File reader for creating characters and adding them to the game state
 */
public class CharacterReader extends FileReader
{
    //Type, Name, HP, Abilities
    public static final int MIN_FIELDS_INIT = 4;
    //Type, Name, current HP, max HP, abilities
    public static final int MIN_FIELDS_RELOAD = 5;

    private AbilityHandler abilityHandler;
    private GameState gameState;
    private CharacterFactory characterFactory;

    private boolean existingGame;

    public CharacterReader(CharacterFactory characterFactory, AbilityHandler abilityHandler, GameState gameState)
    {
        this.abilityHandler = abilityHandler;
        this.gameState = gameState;
        this.characterFactory = characterFactory;
    }

    /**
     * Determine whether a file is for initialising or reloading a game
     * @throws FileFormatException
     */
    @Override
    protected void preprocess() throws FileFormatException
    {
        int headingColumns = heading.split(",").length;

        if(headingColumns == MIN_FIELDS_INIT)
            existingGame = false;
        else if(headingColumns == MIN_FIELDS_RELOAD)
            existingGame = true;
        else
            throw new FileFormatException("Invalid character file heading");
    }

    /**
     * Split the line by comma, create the character object and add it to the game state controller
     * @param line line being processed
     * @throws FileFormatException
     */
    @Override
    protected void processLine(String line) throws FileFormatException
    {
        String[] split = line.split(",(\\s)?");

        //ensure that the line contains the minimum number of values
        if(!existingGame && split.length < MIN_FIELDS_INIT)
            throw new FileFormatException("Invalid character entry");

        if(existingGame && split.length < MIN_FIELDS_RELOAD)
            throw new FileFormatException("Invalid character entry");

        char type = parseType(split[0]);
        String name = parseName(split[1]);
        //existing games have 1 extra field to account for current hp
        int maxHealth = existingGame ? parseHealth(split[3]) : parseHealth(split[2]);
        int currentHealth = existingGame ? parseHealth(split[2]) : maxHealth;
        Set<Ability> abilities = parseAbilities(split);
        String team = parseTeam(split[0]); //for now, character type determines team

        RPGCharacter character = characterFactory.makeCharacter(type);

        character.setType(type);
        character.setName(name);
        character.setMaxHealth(maxHealth);
        character.setCurrentHealth(currentHealth);
        character.setAbilities(abilities);

        gameState.addCharacter(character, team);
    }

    /**
     * Determines the character type as either a player or non-player
     * @param s
     * @return
     * @throws FileFormatException
     */
    private char parseType(String s) throws FileFormatException
    {
        char type = s.charAt(0);

        if(!(type == PLAYER || type == NON_PLAYER))
            throw new FileFormatException("Character type must be 'P' or 'N'");

        return type;
    }

    /**
     * Ensures name is non-empty
     * @param s
     * @return
     * @throws FileFormatException
     */
    private String parseName(String s) throws FileFormatException
    {
        if(s.equals(""))
            throw new FileFormatException("No name given to character");

        return s;
    }

    /**
     * Determines the health of the character. A character cant start the game with less than 20 hp
     * @param s
     * @return
     * @throws FileFormatException
     */
    private int parseHealth(String s) throws FileFormatException
    {
        int hp;

        try
        {
            hp = Integer.parseInt(s);

            if(!existingGame && hp < 20)
                throw new FileFormatException("RPGCharacter assigned max HP less than 20");
        }
        catch (NumberFormatException e)
        {
            throw new FileFormatException("RPGCharacter assigned invalid HP");
        }

        return hp;
    }

    /**
     * Uses the ability handler to add abilities to the character
     * @param split
     * @return
     * @throws FileFormatException
     */
    private Set<Ability> parseAbilities(String[] split) throws FileFormatException
    {
        Set<Ability> abilities = new LinkedHashSet<>();

        int startPos = (existingGame ? MIN_FIELDS_RELOAD : MIN_FIELDS_INIT) - 1; //position of first ability

        for(int i = startPos; i < split.length; i++)
        {
            Ability ability;

            ability = abilityHandler.getAbility(split[i]);

            if(ability == null)
                throw new FileFormatException("Ability \"" + split[i] + "\" doesnt exist in abilities file");

            abilities.add(ability);
        }

        return abilities;
    }

    /**
     * Determines the team of the character
     * @param s
     * @return
     * @throws FileFormatException
     */
    private String parseTeam(String s) throws FileFormatException
    {
        String team;
        //uses the characters type to determine the characters team
        char type = parseType(s);

        if(type == RPGCharacter.NON_PLAYER)
            team = "Non-players";
        else
            team = "Players";

        return team;
    }
}
