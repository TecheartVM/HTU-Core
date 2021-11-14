package techeart.htu.registration.units;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import techeart.htu.utils.BlockPropertiesPatterns;

import javax.annotation.Nullable;

public class RegistryMetal
{
    protected final RegistryBlock block;
    protected final RegistryItem ingot;
    protected final RegistryItem nugget;
    protected final RegistryItem plate;
    protected final RegistryItem rod;
    protected final RegistryItem dust;
    protected final RegistryItem dustUnrefined;

    public RegistryMetal(RegistryBlock block, RegistryItem ingot, RegistryItem nugget,
                         RegistryItem dust, RegistryItem plate, RegistryItem rod, @Nullable RegistryItem unrefined)
    {
        this.block = block;
        this.ingot = ingot;
        this.nugget = nugget;
        this.plate = plate;
        this.rod = rod;
        this.dust = dust;
        this.dustUnrefined = unrefined;
    }

    public Block getBlock() { return block.getBlock(); }
    public BlockItem getBlockItem() { return block.getItem(); }
    public Item getIngot() { return ingot.get(); }
    public Item getNugget() { return nugget.get(); }
    public Item getPlate() { return plate.get(); }
    public Item getRod() { return rod.get(); }
    public Item getDust() { return dust.get(); }
    public Item getDustUnrefined() { return dustUnrefined == null ? Items.AIR : dustUnrefined.get(); }

    public static class Builder
    {
        private boolean isRefractory = false;
        private BlockBehaviour.Properties blockProps = BlockPropertiesPatterns.METAL;
        private RegistryBlock.Builder blockBuilder;
        private CreativeModeTab blockTab = CreativeModeTab.TAB_BUILDING_BLOCKS;
        private CreativeModeTab itemsTab = CreativeModeTab.TAB_MISC;
        private boolean needUnrefined = false;
        private MaterialColor color = MaterialColor.METAL;

        public Builder withBlockBuilder(RegistryBlock.Builder builder)
        {
            blockBuilder = builder;
            return this;
        }
        public Builder withBlockProps(BlockBehaviour.Properties props)
        {
            blockProps = props;
            return this;
        }

        public Builder withUnrefinedDust()
        {
            needUnrefined = true;
            return this;
        }

        public Builder withBlockTab(CreativeModeTab tab)
        {
            blockTab = tab;
            return this;
        }
        public Builder withItemsTab(CreativeModeTab tab)
        {
            itemsTab = tab;
            return this;
        }
        public Builder withGeneralTab(CreativeModeTab tab)
        {
            blockTab = tab;
            itemsTab = tab;
            return this;
        }

        public Builder refractory()
        {
            isRefractory = true;
            return this;
        }

        public Builder withColor(MaterialColor color)
        {
            this.color = color;
            return this;
        }

        public RegistryMetal build(String name, DeferredRegister<Block> br, DeferredRegister<Item> ir)
        {
            Item.Properties itemsProps = new Item.Properties().tab(itemsTab);
            if(isRefractory) itemsProps = itemsProps.fireResistant();
            if(blockBuilder == null) blockBuilder = new RegistryBlock.Builder()
                        .withProperties(blockProps.color(color))
                        .withItemProperties(itemsProps).withCreativeTab(blockTab);
            return new RegistryMetal(
                    blockBuilder.build("block_"+name, br, ir),
                    new RegistryItem.Builder().withProperties(itemsProps).build("ingot_"+name, ir),
                    new RegistryItem.Builder().withProperties(itemsProps).build("nugget_"+name, ir),
                    new RegistryItem.Builder().withProperties(itemsProps).build("plate_"+name, ir),
                    new RegistryItem.Builder().withProperties(itemsProps).build("rod_"+name, ir),
                    new RegistryItem.Builder().withCreativeTab(itemsTab).build("dust_"+name, ir),
                    needUnrefined ? new RegistryItem.Builder().withCreativeTab(itemsTab).build("dust_unrefined_"+name, ir) : null
            );
        }
    }
}
