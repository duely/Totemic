package pokefenn.totemic.ceremony;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import pokefenn.totemic.api.ceremony.Ceremony;
import pokefenn.totemic.api.ceremony.CeremonyEffectContext;
import pokefenn.totemic.api.music.MusicInstrument;
import pokefenn.totemic.init.ModBlocks;
import pokefenn.totemic.util.EntityUtil;

public class CeremonyFertility extends Ceremony
{


    public CeremonyFertility(String name, int musicNeeded, int maxStartupTime, MusicInstrument... instruments)
    {
        super(name, musicNeeded, maxStartupTime, instruments);
    }

    @Override
    public void effect(World world, BlockPos pos, CeremonyEffectContext context)
    {
        int radius = 8;

        if (!world.isRemote && context.getTime() % 20 * 5 == 0)
        {
            for (Entity entity : EntityUtil.getEntitiesInRange(EntityLiving.class, world, pos, radius, radius))
            {

                if (entity instanceof EntityAnimal || entity instanceof EntityVillager)
                {
                    if (world.rand.nextInt(4) == 0)
                    {
                        if (entity instanceof EntityAnimal && !((EntityAnimal) entity).isInLove())
                        {
                            EntityAnimal animal = (EntityAnimal) entity;
                            animal.setInLove(null);
                        } else
                        {
                            //figure out how to make villagers mate
                            /*
                            EntityVillager villager = (EntityVillager) entity;
                            villager.setIsWillingToMate(true);
                            villager.tasks.addTask(0, new EntityAIVillagerMate(villager));
                            villager.setMating(true);
                            */
                        }

                    }
                }
            }
        }

        if (context.getTime() % 20 == 0)
        {
            for (int i = -radius; i <= radius; i++)
                for (int j = -radius; j <= radius; j++)
                    for (int k = -radius; k <= radius; k++)
                    {
                        BlockPos p = pos.add(i, j, k);
                        IBlockState state = world.getBlockState(p);
                        Block block = state.getBlock();
                        if (block instanceof BlockSapling && block != ModBlocks.cedar_sapling)
                        {
                            world.setBlockState(p, ModBlocks.cedar_sapling.getDefaultState(), 3);
                            spawnParticles(world, p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5);
                        }

                    }
        }
    }

    @Override
    public int getEffectTime()
    {
        return SHORT;
    }

    @Override
    public int getMusicPer5()
    {
        return 6;
    }

    private void spawnParticles(World world, double x, double y, double z)
    {
        if (world.isRemote)
        {
            double dx = world.rand.nextGaussian();
            double dy = world.rand.nextGaussian() * 0.5;
            double dz = world.rand.nextGaussian();
            double velY = world.rand.nextGaussian();
            for (int i = 0; i < 10; i++)
                world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, x + dx, y + dy, z + dz, 0, velY, 0);
        }
    }
}