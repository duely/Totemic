package totemic_commons.pokefenn;

import static totemic_commons.pokefenn.Totemic.logger;

import java.util.Random;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.Display;

import net.minecraft.client.Minecraft;
import net.minecraft.world.ColorizerFoliage;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import totemic_commons.pokefenn.client.rendering.entity.BaykokRendering;
import totemic_commons.pokefenn.client.rendering.entity.BuffaloRendering;
import totemic_commons.pokefenn.client.rendering.entity.InvisArrowRendering;
import totemic_commons.pokefenn.client.rendering.tileentity.TileWindChimeRenderer;
import totemic_commons.pokefenn.configuration.ConfigClient;
import totemic_commons.pokefenn.entity.animal.EntityBuffalo;
import totemic_commons.pokefenn.entity.boss.EntityBaykok;
import totemic_commons.pokefenn.entity.projectile.EntityInvisArrow;
import totemic_commons.pokefenn.handler.GameOverlay;
import totemic_commons.pokefenn.handler.PlayerRender;
import totemic_commons.pokefenn.tileentity.music.TileWindChime;
import totemic_commons.pokefenn.totempedia.LexiconData;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        OBJLoader.INSTANCE.addDomain(Totemic.MOD_ID);
        RenderingRegistry.registerEntityRenderingHandler(EntityBuffalo.class, BuffaloRendering::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInvisArrow.class, InvisArrowRendering::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBaykok.class, BaykokRendering::new);
        initTESRs();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        ModBlocks.setStateMappers();
        ModItems.setItemModels();
        registerBlockColors();
        LexiconData.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
        try
        {
            if(ConfigClient.enableFloweyEasteregg && Loader.isModLoaded("Botania") && new Random().nextInt(8) == 0)
                Display.setTitle(Display.getTitle().replace("Minecraft", "Floweycraft")); //HAHAHAHAHAHAHAHAHAHAHA...
        }
        catch(Exception e)
        {
            logger.catching(Level.WARN, e);
        }
    }

    @Override
    protected void registerEventHandlers()
    {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(new GameOverlay());
        MinecraftForge.EVENT_BUS.register(new PlayerRender());
    }

    private void registerBlockColors()
    {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(
                (state, world, pos, tintIndex) -> ColorizerFoliage.getFoliageColorPine(), ModBlocks.cedar_leaves);
    }

    private void initTESRs()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileWindChime.class, new TileWindChimeRenderer());
    }
}
