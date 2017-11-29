package game.model;

import character.model.CharacterObserver;
import character.model.RPGCharacter;
import character.model.Targetable;
import character.model.Team;

import java.util.*;

/**
 * Model for managing the current state of the game
 */
public class GameState implements CharacterObserver
{
    private List<GameStateObserver> gameStateObservers;

    private Set<RPGCharacter> characters;
    private Set<RPGCharacter> activeCharacters;
    private Set<RPGCharacter> eliminatedCharacters;

    private Map<String, Team> teams;
    private Set<Team> activeTeams;
    private Set<Team> eliminatedTeams;

    private Iterator<RPGCharacter> charItr;
    private RPGCharacter currentCharacter;


    public GameState()
    {
        gameStateObservers = new ArrayList<>();

        characters = new LinkedHashSet<>();
        activeCharacters = new HashSet<>();
        eliminatedCharacters = new HashSet<>();

        teams = new HashMap<>();
        activeTeams = new HashSet<>();
        eliminatedTeams = new HashSet<>();

        charItr = characters.iterator();
        currentCharacter = null;
    }

    /**
     * Gets the next character whos turn it is
     * @return next character
     */
    public RPGCharacter getNextCharacter()
    {
        //if the iterator reaches the end of the character set start from the beginning
        if(!charItr.hasNext())
            charItr = characters.iterator();

        currentCharacter = charItr.next();
        return currentCharacter;
    }

    /**
     * Get the current character performing their turn
     * @return current character
     */
    public RPGCharacter getCurrentCharacter()
    {
        return currentCharacter;
    }

    /**
     * Add a new character to the game and put them in the appropriate team. If team does not yet exist
     * add the new team
     * @param character Character to add to the game
     * @param teamName Name of team the character belongs to
     */
    public void addCharacter(RPGCharacter character, String teamName)
    {
        characters.add(character);

        if(character.isDead())
            eliminatedCharacters.add(character);
        else
            activeCharacters.add(character);

        if(!teams.containsKey(teamName))
        {
            addTeam(teamName);
            activeTeams.add(teams.get(teamName));
        }

        Team team = teams.get(teamName);
        team.addCharacter(character);
        character.setTeam(team);
    }

    /**
     * Add a new team to the game
     * @param teamName name of new team
     */
    public void addTeam(String teamName)
    {
         teams.put(teamName, new Team(teamName));
    }

    @Override
    public void notifyHealthChange(RPGCharacter initiator, Targetable target, int hp)
    {
        //dont need to update anything
    }

    /**
     * When a character dies the game state controller removes them from the active player set
     * and adds them to the eliminated characters set. A check is done to see if death caused a team to be eliminated.
     * @param killer Character that killed
     * @param dead Character that died
     */
    @Override
    public void notifyDeath(RPGCharacter killer, RPGCharacter dead)
    {
        //update the character sets
        activeCharacters.remove(dead);
        eliminatedCharacters.add(dead);

        //update team's character set
        Team team = dead.getTeam();
        team.getActiveCharacters().remove(dead);
        team.getDeadCharacters().add(dead);

        //heal the team responsible for the kill
        killer.getTeam().teamHeal();

        //check if the dead character's team has been eliminated
        if(team.getActiveCharacters().size() == 0)
        {
            activeTeams.remove(team);
            eliminatedTeams.add(team);

            notifyTeamElimated(team);

            //check if only 1 team remains
            if(activeTeams.size() == 1)
            {
                notifyTeamWon(activeTeams.iterator().next());
            }
        }
    }

    public Set<RPGCharacter> getCharacters()
    {
        return characters;
    }

    public Set<RPGCharacter> getActiveCharacters()
    {
        return activeCharacters;
    }

    public Set<RPGCharacter> getEliminatedCharacters()
    {
        return eliminatedCharacters;
    }

    public Map<String, Team> getTeams()
    {
        return teams;
    }


    public Set<Team> getActiveTeams()
    {
        return activeTeams;
    }

    public Set<Team> getEliminatedTeams()
    {
        return eliminatedTeams;
    }

    /**
     * Add an observer to the observer list
     * @param observer Observer to add
     */
    public void addGameStateObserver(GameStateObserver observer)
    {
        gameStateObservers.add(observer);
    }

    /**
     * Remove an observer from the observer list
     * @param observer Observer to remove
     */
    public void removeGameStateObserver(GameStateObserver observer)
    {
        gameStateObservers.remove(observer);
    }

    /**
     * Notify observers when a team has won the game
     * @param winningTeam winning team
     */
    private void notifyTeamWon(Team winningTeam)
    {
        for(GameStateObserver observer : gameStateObservers)
        {
            observer.notifyGameFinished(winningTeam);
        }
    }

    /**
     * Notify observers when a team is eliminated
     * @param team Team eliminated
     */
    private void notifyTeamElimated(Team team)
    {
        for(GameStateObserver observer : gameStateObservers)
        {
            observer.notifyTeamEliminated(team);
        }
    }
}
