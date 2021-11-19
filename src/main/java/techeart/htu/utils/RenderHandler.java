package techeart.htu.utils;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    private final Map<Supplier<Block>, RenderType> BLOCK_RENDERERS = new HashMap<>();
    private final Map<EntityType<? extends Entity>, EntityRendererProvider<Entity>> ENTITY_RENDERERS = new HashMap<>();
    private final Map<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider<BlockEntity>> BE_RENDERERS =new HashMap<>();

    public void init()
    {
        //registering BLOCK render types
        for (Supplier<Block> block : BLOCK_RENDERERS.keySet())
            ItemBlockRenderTypes.setRenderLayer(block.get(), BLOCK_RENDERERS.get(block));

        for(BlockEntityType<? extends BlockEntity> type : BE_RENDERERS.keySet())
            BlockEntityRenderers.register(type, BE_RENDERERS.get(type));

        for (EntityType<? extends Entity> t : ENTITY_RENDERERS.keySet())
            EntityRenderers.register(t, ENTITY_RENDERERS.get(t));
    }

    public void setBlockRenderType(RegistryObject<Block> block, RenderType type) { BLOCK_RENDERERS.put(block, type); }
    public void setBlockRenderType(RegistryBlock block, RenderType type) { BLOCK_RENDERERS.put(block::getBlock, type); }

    public <T extends Entity> void setEntityRenderer(EntityType<? extends T> et, EntityRendererProvider<T> rt)
    {
        ENTITY_RENDERERS.put(et, (EntityRendererProvider<Entity>) rt);
    }
    public <T extends BlockEntity> void addBlockEntityRenderer(BlockEntityType<? extends T> tileEntityType, BlockEntityRendererProvider<T> renderType)
    {
        BE_RENDERERS.put(tileEntityType, (BlockEntityRendererProvider<BlockEntity>) renderType);
    }
}
