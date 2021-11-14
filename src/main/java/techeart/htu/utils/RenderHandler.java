package techeart.htu.utils;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.RegistryObject;
import techeart.htu.registration.units.RegistryBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class RenderHandler
{
    private static final Map<Supplier<Block>, RenderType> BLOCK_RENDERERS = new HashMap<>();

    public static void init()
    {
        //registering BLOCK render types
        for (Supplier<Block> block : BLOCK_RENDERERS.keySet())
            ItemBlockRenderTypes.setRenderLayer(block.get(), BLOCK_RENDERERS.get(block));
    }

    public static void setBlockRenderType(RegistryObject<Block> block, RenderType type) { BLOCK_RENDERERS.put(block, type); }
    public static void setBlockRenderType(RegistryBlock block, RenderType type) { BLOCK_RENDERERS.put(block::getBlock, type); }
}
