package game.controller;

import abilities.controller.AbilityHandler;
import character.controller.CharacterFactory;
import character.model.CharacterObserver;
import character.model.RPGCharacter;
import character.model.Team;
import fileio.*;
import game.model.GameState;
import game.model.GameStateObserver;
import view.UserInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Main controller for setting up and running the game
 */
public class GameController implements GameStateObserver
{
    public static final int OPTION_USE_SKILL = 1;
    public static final int OPTION_PASS_TURN = 2;
    public static final int OPTION_OVERVIEW = 3;
    public static final int OPTION_SAVE = 4;
    public static final int OPTION_EXIT = 5;

    private UserInterface userInterface;
    private AbilityHandler abilityHandler;
    private GameState gameState;
    private CombatController combatController;

    private boolean gameFinished;

    public GameController(UserInterface userInterface, AbilityHandler abilityHandler, GameState gameState,
                          CombatController combatController)
    {
        gameFinished = false;

        this.userInterface = userInterface;
        this.abilityHandler = abilityHandler;
        this.gameState = gameState;
        this.combatController = combatController;

        userInterface.addOption(OPTION_USE_SKILL, new UseSkill());
        userInterface.addOption(OPTION_PASS_TURN, new PassTurn());
        userInterface.addOption(OPTION_OVERVIEW, new DisplayOverview());
        userInterface.addOption(OPTION_SAVE, new Save());
        userInterface.addOption(OPTION_EXIT, new Exit());
    }

    public void run()
    {
        while (!gameFinished) //keep cycling through turns until an exit condition is met
        {
            RPGCharacter character = gameState.getNextCharacter();
            if (!character.isDead())
            {
                userInterface.doTurn(character);
            }
        }
    }

    /**
     * Initialse the game using an ability file and character file
     */
    public void setup()
    {
        try
        {
            FileReader fileReader;
            String file;

            //load in abilities
            fileReader = new AbilityReader(abilityHandler);
            file = userInterface.prompt("Enter the ability file");
            fileReader.read(file);

            //create observer list for all characters
            List<CharacterObserver> characterObservers = new ArrayList<>();
            characterObservers.add(userInterface);
            characterObservers.add(gameState);

            //load in characters
            CharacterFactory characterFactory = new CharacterFactory(characterObservers);
            fileReader = new CharacterReader(characterFactory, abilityHandler, gameState);
            file = userInterface.prompt("Enter the character file");
            fileReader.read(file);
        }
        catch(FileReadException | FileFormatException e)
        {
            userInterface.error("Error reading file", e);
            setup();
        }
    }

    /**
     * Save the current game
     */
    private void save()
    {
        try
        {
            FileWriter fileWriter;
            String file;

            //save abilities to file
            fileWriter = new AbilityWriter(abilityHandler.getAbilites().values());
            file = userInterface.prompt("Save the abilities file as: ");
            fileWriter.write(file);

            //save characters to file
            fileWriter = new CharacterWriter(gameState.getCharacters());
            file = userInterface.prompt("Save the character file as: ");
            fileWriter.write(file);
            
            userInterface.output("Save successful!");
        }
        catch(FileWriteException e)
        {
            userInterface.error("Error saving game", e);
        }
    }

    @Override
    public void notifyTeamEliminated(Team eliminated)
    {
        //dont need to update anything
    }

    @Override
    public void notifyGameFinished(Team winningTeam)
    {
        gameFinished = true;
    }

    /**************************************************************************************************************

    Private classes for menu options

    **************************************************************************************************************/

    /**
     * Option to save the games current state
     */
    private class Save implements Option
    {
        @Override
        public void doOption()
        {
            save();
        }

        @Override
        public String getDescription()
        {
            return "Save current game";
        }
    }

    /**
     * Option to display the current state of the game
     */
    private class DisplayOverview implements Option
    {
        @Override
        public void doOption()
        {
            userInterface.overview(gameState.getActiveTeams(), gameState.getEliminatedTeams());
        }

        @Override
        public String getDescription()
        {
            return "Display overview";
        }
    }

    /**
     * Option to perform the current characters turn
     */
    private class UseSkill implements Option
    {
        @Override
        public void doOption()
        {
            combatController.performAbility(gameState.getCurrentCharacter());
        }

        @Override
        public String getDescription()
        {
            return "Use a skill";
        }
    }

    /**
     * Option to pass the current characters turn
     */
    private class PassTurn implements Option
    {
        @Override
        public void doOption()
        {
            userInterface.output(gameState.getCurrentCharacter().getName() + " passed!");
        }

        @Override
        public String getDescription()
        {
            return "Pass turn";
        }
    }

    /**
     * Option to exit the game
     */
    private class Exit implements Option
    {
        @Override
        public void doOption()
        {
            gameFinished = true;
        }

        @Override
        public String getDescription()
        {
            return "Quit";
        }
    }
}
