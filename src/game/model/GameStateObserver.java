package game.model;

import character.model.Team;

/**
 * Notifies parts of the system when the game's state changes
 */
public interface GameStateObserver
{
    void notifyTeamEliminated(Team eliminated);
    void notifyGameFinished(Team winningTeam);
}
