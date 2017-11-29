package character.model;

/**
 * Interface for objects which can be targeted by an ability (characters or teams)
 */
public interface Targetable
{
    String getName();
    void changeHealth(RPGCharacter initiator, int hp);
    boolean alliedWith(RPGCharacter character);
}
