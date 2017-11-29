package view;

import abilities.model.Ability;
import character.model.CharacterObserver;
import character.model.RPGCharacter;
import character.model.Targetable;
import character.model.Team;
import game.controller.Option;
import game.model.GameStateObserver;

import java.util.*;
import java.util.Map.Entry;

import static game.controller.GameController.OPTION_OVERVIEW;
import static game.controller.GameController.OPTION_SAVE;

/**
 * Command line interface for interacting with the user
 */
public class UserInterface implements CharacterObserver, GameStateObserver
{
    private Map<Integer, Option> options;

    public UserInterface()
    {
        options = new HashMap<>();
    }

    /**
     * Perform the current characters turn
     * @param character current character
     */
    public void doTurn(RPGCharacter character)
    {
        beginTurn(character);

        int select = selectOption();
        options.get(select).doOption();

        while(select == OPTION_OVERVIEW || select == OPTION_SAVE)
        {
            select = selectOption();
            options.get(select).doOption();
        }
    }

    /**
     * Displays whos turn it is
     * @param character Current character's turn
     */
    public void beginTurn(RPGCharacter character)
    {
        if(character.getType() == RPGCharacter.NON_PLAYER)
            System.out.println(character.getName() + "'s turn! (Story teller)");
        else
            System.out.println(character.getName() + "'s turn! (Player)");
    }

    /**
     * Print a message to the screen and get input from the user
     * @param message message to be displayed
     * @return user response
     */
    public String prompt(String message)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println(message);
        return sc.nextLine();
    }

    /**
     * Output a message to the screen
     * @param message message to be displayed
     */
    public void output(String message)
    {
        System.out.println(message);
    }

    /**
     * Display a list of abilities to the screen and returns the ability chosen by the user
     * @param abilities list of abilities to choose from
     * @return selected ability
     */
    public int selectAbility(List<Ability> abilities)
    {
        int i = 1;
        for(Ability ability : abilities)
        {
            String type = (ability.getType() == Ability.DAMAGE) ? "Damage" : "Heal";
            String target = (ability.getTarget() == Ability.SINGLE) ? "Single-target" : "Multi-target";
            System.out.println("[" + (i) + "] " + ability.getName() + " (" + type + "/" + target + ")");
            i++;
        }

        return select(abilities.size());
    }

    /**
     * Output the error to the user
     * @param message error description
     * @param e Exception thrown
     */
    public void error(String message, Exception e)
    {
        System.out.println(message + ": " + e.getMessage());
        System.out.println();
    }

    /**
     * Gets users option selection as an int and repeats until a valid value is given
     * @param numOptions number of options to choose from
     * @return option number selected
     */
    public int select(int numOptions)
    {
        Scanner sc = new Scanner(System.in);
        int option;
        try
        {
            option = sc.nextInt();
            while(option < 1 || option > numOptions)
            {
                System.out.println("Invalid choice");
                option = sc.nextInt();
            }
        }
        catch(InputMismatchException e)
        {
            System.out.println("Invalid choice");
            option = select(numOptions) + 1;
        }

        return option - 1; //convert from 1 index to 0 index
    }

    /**
     * Select a target to perform an ability on
     * @param targets targets to choose from
     * @return target chosen
     */
    public int selectTarget(List<Targetable> targets)
    {
        System.out.println("Select target: ");
        for(int i = 0; i < targets.size(); i++)
        {
            System.out.println("[" + (i+1) + "] " + targets.get(i).getName());
        }

        return select(targets.size());
    }

    /**
     * Displays each characters health. Grouped by team
     * @param activeTeams Teams that still have atleast one character alive
     * @param eliminatedTeams Teams whos characters are alll dead
     */
    public void overview(Set<Team> activeTeams, Set<Team> eliminatedTeams)
    {
        for(Team team : activeTeams)
        {
            System.out.println("Team: " + team.getName() + " (" + team.getActiveCharacters().size() + " remaining)");
            for(RPGCharacter character : team.getCharacters())
            {
                System.out.println(character);
            }
        }
        if(eliminatedTeams.size() > 0)
        {
            for(Team team : eliminatedTeams)
            {
                System.out.println("Team: " + team.getName() + " (eliminated)");
            }
        }

    }

    /**
     * Displays a set of options
     * @return Option selected
     */
    public int selectOption()
    {
        for(Entry<Integer, Option> entry : options.entrySet())
        {
            System.out.println("[" + entry.getKey() + "] " + entry.getValue().getDescription());
        }

        return select(options.size()) + 1;
    }

    /**
     * Displays a change in a character's health when notified.
     * @param initiator Character performing the ability
     * @param target Character affected by ability
     * @param hp Amount health changed by
     */
    @Override
    public void notifyHealthChange(RPGCharacter initiator, Targetable target, int hp)
    {
        if(hp == 0)
        {
            System.out.println(target.getName() + " was unaffected");
        }
        else
        {
            String affect;
            if(hp > 0)
            {
                affect = "healed";
            }
            else
            {
                affect = "damaged";
                hp = -hp;
            }

            if(initiator.equals(target))
                System.out.println(initiator.getName() + " " + affect + " themself for " + hp);
            else
                System.out.println(initiator.getName() + " " + affect + " " + target.getName() + " for " + hp);
        }
    }

    /**
     * Displays a message when a character dies
     * @param killer Character that killed
     * @param dead Character that died
     */
    @Override
    public void notifyDeath(RPGCharacter killer, RPGCharacter dead)
    {
        System.out.println(killer.getName() + " killed " + dead.getName());
    }

    /**
     * Disaplys when a team is eliminated
     * @param eliminated Team eliminated
     */
    @Override
    public void notifyTeamEliminated(Team eliminated)
    {
        System.out.println("Team " + eliminated.getName() + " has been eliminated!");
    }

    /**
     * Displays when a team has won the game
     * @param winningTeam Team that won
     */
    @Override
    public void notifyGameFinished(Team winningTeam)
    {
        System.out.println(winningTeam.getName() + " won!");
    }

    /**
     * Add an option to select from
     * @param i option index
     * @param option option to perform
     */
    public void addOption(int i, Option option)
    {
        options.put(i, option);
    }
}
