/*

Christopher Villegas
18359884
OOSE Assignment Semester 2, 2017

*/

import abilities.controller.AbilityHandler;
import game.controller.CombatController;
import game.controller.GameController;
import game.model.GameState;
import view.UserInterface;

/**
 * Entry point to start the game
 */
public class CombatTool
{
    public static void main(String[] args)
    {
        AbilityHandler abilityHandler = new AbilityHandler();
        GameState gameState = new GameState();
        UserInterface userInterface = new UserInterface();

        //inject dependencies
        CombatController combatController = new CombatController(gameState, userInterface);
        GameController gameController = new GameController(userInterface, abilityHandler, gameState, combatController);

        //update ui and game controller of game state changes
        gameState.addGameStateObserver(userInterface);
        gameState.addGameStateObserver(gameController);

        //begin the game
        gameController.setup();
        gameController.run();
    }
}
