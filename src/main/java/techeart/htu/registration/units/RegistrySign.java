package techeart.htu.registration.units;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class RegistrySign
{
    private final RegistryObject<Block> blockStanding;
    private final RegistryObject<Block> blockWall;
    private final RegistryObject<Item> item;

//    private final RegistryObject<BlockEntityType<SignBlockEntity>> blockEntityType = ;

    public RegistrySign(WoodType woodType, BlockBehaviour.Properties props, CreativeModeTab tab,
                        DeferredRegister<Block> br, DeferredRegister<Item> ir, DeferredRegister<BlockEntityType<?>> ber)
    {
        final Block s = new StandingSignBlock(props, woodType);
        Block w = new WallSignBlock(props, woodType);

        blockStanding = br.register("sign_" + woodType.name(), () -> s);
        blockWall = br.register("sign_wall_" + woodType.name(), () -> w);
        item = ir.register("sign_" + woodType.name(), () -> new SignItem(new Item.Properties().tab(tab), s, w));
//        blockEntityType = ber.register("sign_" + woodType.name(), () -> BlockEntityType.Builder.of(SignBlockEntity::new, s, w).build(null));
    }

    public Block getBlockStanding() { return blockStanding.get(); }
    public Block getBlockWall() { return blockWall.get(); }
    public Item getItem() { return item.get(); }
//    public BlockEntityType<SignBlockEntity> getBlockEntityType() { return blockEntityType.get(); }
}
