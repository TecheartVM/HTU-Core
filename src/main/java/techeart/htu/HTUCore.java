package techeart.htu;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import techeart.htu.registration.RegistryHandler;
import techeart.htu.registration.units.RegistryBoat;
import techeart.htu.registration.units.RegistryWood;
import techeart.htu.render.CustomBoatRenderer;
import techeart.htu.utils.RenderHandler;


@Mod("hightechuniverse")
public class HTUCore
{
    public static final String MODID = "hightechuniverse";

    public static final RegistryHandler REGISTRY = new RegistryHandler(MODID);
    @OnlyIn(Dist.CLIENT)
    public static final RenderHandler RENDER = new RenderHandler();


    private static final RegistryWood TEST = REGISTRY.register("test", new RegistryWood.Builder().withTab(CreativeModeTab.TAB_BUILDING_BLOCKS).withNoFoliage());
    //TODO: move this to another class
    public static final EntityType<? extends Boat> BOAT_TYPE = REGISTRY.register("boat",EntityType.Builder.<Boat>of(RegistryBoat.EntityBoat::new, MobCategory.MISC).sized(1.375f, 0.5625f).clientTrackingRange(10), CustomBoatRenderer::new);

    public static final Logger LOGGER = LogManager.getLogger();

    public HTUCore()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        RENDER.init();
    }
}
