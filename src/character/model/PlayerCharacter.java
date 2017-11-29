package character.model;

/**
 * Player character
 */
public class PlayerCharacter extends RPGCharacter
{
    /**
     * heal self by 10% upon team receiving a kill
     */
    @Override
    public void teamHeal()
    {
        changeHealth(this, (int)(0.1 * super.getMaxHealth()));
    }
}
