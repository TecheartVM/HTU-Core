package techeart.htu.registration;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import techeart.htu.objects.HTUBlock;
import techeart.htu.objects.HTUItemBlock;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class RegistryBlock extends DoubleRegistryObject<HTUBlock, HTUItemBlock>
{
    private boolean noItem = false;

    public RegistryBlock(RegistryObject<HTUBlock> block, RegistryObject<HTUItemBlock> item) { super(block, item); }
    public RegistryBlock(RegistryObject<HTUBlock> block, RegistryObject<HTUItemBlock> item, boolean noItem)
    {
        super(block, item);
        this.noItem = noItem;
    }

    public HTUBlock getBlock() { return getPrimary(); }
    public HTUItemBlock getItem()
    {
        if(noItem) return (HTUItemBlock)Items.AIR;
        return getSecondary();
    }

    public static class Builder
    {
        private BlockBehaviour.Properties props = BlockBehaviour.Properties.of(Material.AIR);
        @Nullable
        private Supplier<? extends HTUBlock> blockSupplier;
        private CreativeModeTab tab = CreativeModeTab.TAB_BUILDING_BLOCKS;
        private boolean noItem = false;
        private Item.Properties itemProps = new Item.Properties();

        public Builder withProperties(BlockBehaviour.Properties props)
        {
            this.props = props;
            return this;
        }
        public Builder withSupplier(Supplier<? extends HTUBlock> sup)
        {
            blockSupplier = sup;
            return this;
        }
        public Builder noItem()
        {
            noItem = true;
            return this;
        }
        public Builder withItemProperties(Item.Properties props)
        {
            itemProps = props;
            return this;
        }
        public Builder withCreativeTab(CreativeModeTab tab)
        {
            this.tab = tab;
            return this;
        }

        public RegistryBlock build(String name, DeferredRegister<Block> br, DeferredRegister<Item> ir)
        {
            if(blockSupplier == null) blockSupplier = () -> new HTUBlock(props);
            RegistryObject<HTUBlock> block = br.register(name, blockSupplier);
            RegistryObject<HTUItemBlock> item = null;
            if(!noItem)
            {
                final Item.Properties itemProps = this.itemProps.tab(tab);
                item = ir.register(name, () -> new HTUItemBlock(block.get(), itemProps));
            }
            return new RegistryBlock(block, item, noItem);
        }
    }
}
