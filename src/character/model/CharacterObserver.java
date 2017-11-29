package character.model;

/**
 * Notifies parts of the system when a characters state changes
 */
public interface CharacterObserver
{
    void notifyHealthChange(RPGCharacter initiator, Targetable target, int hp);
    void notifyDeath(RPGCharacter killer, RPGCharacter dead);
}
