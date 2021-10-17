package techeart.htu.registration;

import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;


public class DoubleRegistryObject<PRIMARY extends IForgeRegistryEntry<? super PRIMARY>, SECONDARY extends IForgeRegistryEntry<? super SECONDARY>>
{
    private final RegistryObject<PRIMARY> primaryRO;
    private final RegistryObject<SECONDARY> secondaryRO;

    public DoubleRegistryObject(RegistryObject<PRIMARY> primaryRO, RegistryObject<SECONDARY> secondaryRO)
    {
        this.primaryRO = primaryRO;
        this.secondaryRO = secondaryRO;
    }

    public PRIMARY getPrimary()
    {
        if(primaryRO == null)
            throw new NullPointerException("Primary variable is NULL!");
        return primaryRO.get();
    }

    public SECONDARY getSecondary()
    {
        if(secondaryRO == null)
            throw new NullPointerException("Secondary variable is NULL!");
        return secondaryRO.get();
    }
}
