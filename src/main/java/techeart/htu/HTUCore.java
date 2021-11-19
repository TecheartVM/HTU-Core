package techeart.htu;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import techeart.htu.registration.RegistryHandler;
import techeart.htu.registration.units.RegistryBlock;
import techeart.htu.registration.units.RegistryBoat;
import techeart.htu.render.CustomBoatRenderer;
import techeart.htu.utils.RenderHandler;

import java.util.HashSet;
import java.util.Set;


@Mod("htucore")
public class HTUCore
{
    public static final String MODID = "htucore";

    private static final RegistryHandler registryHandler = new RegistryHandler(MODID);
    @OnlyIn(Dist.CLIENT)
    public static final RenderHandler RENDER_HANDLER = new RenderHandler();
    public static final Set<WoodType> BasicSignTypes = new HashSet<>();

    private static final RegistryBlock TEST = registryHandler.register("test", new RegistryBlock.Builder().withCreativeTab(CreativeModeTab.TAB_BUILDING_BLOCKS));
    public static final EntityType<? extends Boat> BOAT_TYPE = registryHandler.register("boat",EntityType.Builder.<Boat>of(RegistryBoat.EntityBoat::new, MobCategory.MISC).sized(1.375f, 0.5625f).clientTrackingRange(10), CustomBoatRenderer::new);
//    public static BlockEntityType<SignBlockEntity> SIGN_TYPE;
    public static final Logger LOGGER = LogManager.getLogger();

    public HTUCore()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        registryHandler.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RENDER_HANDLER.init();
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {

        }
    }
}
