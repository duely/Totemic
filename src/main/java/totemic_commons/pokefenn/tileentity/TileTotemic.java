package totemic_commons.pokefenn.tileentity;

import net.minecraft.tileentity.TileEntity;

public class TileTotemic extends TileEntity
{
    public void markForUpdate()
    {
        worldObj.markBlockForUpdate(pos);
    }
}
