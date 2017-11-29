package character.model;

/**
 * Non-player character
 */
public class NonPlayerCharacter extends RPGCharacter
{

    /**
     * heal self by 5% upon team receiving a kill
     */
    @Override
    public void teamHeal()
    {
        changeHealth(this, (int)(0.05 * super.getMaxHealth()));
    }
}
