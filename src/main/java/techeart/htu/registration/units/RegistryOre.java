package techeart.htu.registration.units;

import com.google.common.collect.Maps;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import techeart.htu.utils.BlockPropertiesPatterns;

import javax.annotation.Nullable;
import java.util.Map;

public class RegistryOre
{
    private final Map<String, RegistryBlock> oreBlocks = Maps.newHashMap();
    private final RegistryBlock rawBlock;
    private final RegistryItem rawItem;
    private final RegistryItem dust;

    private RegistryOre(RegistryBlock rawBlock, RegistryItem rawItem, @Nullable RegistryItem dust)
    {
        this.rawBlock = rawBlock;
        this.rawItem = rawItem;
        this.dust = dust;
    }

    public Map<String, RegistryBlock> getOreBlocks() { return oreBlocks; }
    public Block getOreBlock(String suffix)
    {
        RegistryBlock rb = oreBlocks.get(suffix);
        return rb == null ? Blocks.AIR : rb.getBlock();
    }
    public BlockItem getOreBlockItem(String suffix)
    {
        RegistryBlock rb = oreBlocks.get(suffix);
        return rb == null ? (BlockItem) Items.AIR : rb.getItem();
    }
    public Block getRawBlock() { return rawBlock.getBlock(); }
    public BlockItem getRawBlockItem() { return rawBlock.getItem(); }
    public Item getRawItem() { return rawItem.get(); }
    public Item getDustItem() { return dust == null ? Items.AIR : dust.get(); }

    private void addOreBlock(String suffix, RegistryBlock block) { oreBlocks.put(suffix, block); }

    public static class Builder
    {
        private final Map<String, RegistryBlock.Builder> ores = Maps.newHashMap();
        private BlockBehaviour.Properties rawBlockProps = BlockPropertiesPatterns.RAW_ORE;
        private CreativeModeTab rawBlockTab = CreativeModeTab.TAB_BUILDING_BLOCKS;
        private CreativeModeTab rawItemTab = CreativeModeTab.TAB_MISC;
        private CreativeModeTab dustTab = CreativeModeTab.TAB_MISC;

        public Builder addOreBlock(String suffix, RegistryBlock.Builder builder)
        {
            ores.put(suffix, builder);
            return this;
        }
        public Builder addOreBlock(String suffix, BlockBehaviour.Properties props)
        {
            ores.put(suffix, new RegistryBlock.Builder().withProperties(props));
            return this;
        }
        public Builder addOreBlock(String suffix, BlockBehaviour.Properties props, CreativeModeTab tab)
        {
            ores.put(suffix, new RegistryBlock.Builder().withProperties(props).withCreativeTab(tab));
            return this;
        }
        public Builder addOreBlock(String suffix, CreativeModeTab tab)
        {
            ores.put(suffix, new RegistryBlock.Builder().withCreativeTab(tab));
            return this;
        }
        public Builder addOreBlock(String suffix, float strength)
        {
            return addOreBlock(suffix, BlockBehaviour.Properties.of(Material.STONE)
                    .strength(strength).sound(SoundType.STONE).requiresCorrectToolForDrops());
        }
        public Builder addOreBlock(String suffix, float hardness, float resist)
        {
            return addOreBlock(suffix, BlockBehaviour.Properties.of(Material.STONE)
                    .strength(hardness, resist).sound(SoundType.STONE).requiresCorrectToolForDrops());
        }
        public Builder addOreBlock(String suffix, float strength, CreativeModeTab tab)
        {
            return addOreBlock(suffix, BlockBehaviour.Properties.of(Material.STONE)
                    .strength(strength).sound(SoundType.STONE).requiresCorrectToolForDrops(), tab);
        }
        public Builder addOreBlock(String suffix, float hardness, float resist, CreativeModeTab tab)
        {
            return addOreBlock(suffix, BlockBehaviour.Properties.of(Material.STONE)
                    .strength(hardness, resist).sound(SoundType.STONE).requiresCorrectToolForDrops(), tab);
        }

        public Builder addOverworldOre() { return addOreBlock("overworld", BlockPropertiesPatterns.ORE); }
        public Builder addDeepslateOre() { return addOreBlock("deepslate", BlockPropertiesPatterns.ORE_DEEPSLATE); }
        public Builder addNetherOre() { return addOreBlock("nether", BlockPropertiesPatterns.ORE_NETHER); }
        public Builder addTheEndOre() { return addOreBlock("theend", BlockPropertiesPatterns.ORE_THE_END); }

        public Builder withRawBlockTab(CreativeModeTab tab)
        {
            rawBlockTab = tab;
            return this;
        }
        public Builder withRawItemTab(CreativeModeTab tab)
        {
            rawItemTab = tab;
            return this;
        }
        public Builder withDustTab(CreativeModeTab tab)
        {
            dustTab = tab;
            return this;
        }
        public Builder withItemsTab(CreativeModeTab tab)
        {
            rawBlockTab = tab;
            rawItemTab = tab;
            return this;
        }
        public Builder withGeneralTab(CreativeModeTab tab)
        {
            rawBlockTab = tab;
            rawItemTab = tab;
            dustTab = tab;
            return this;
        }

        public Builder withColor(MaterialColor color)
        {
            rawBlockProps = rawBlockProps.color(color);
            return this;
        }

        public RegistryOre build(String name, DeferredRegister<Block> br, DeferredRegister<Item> ir)
        {
            RegistryBlock rawBlock = new RegistryBlock.Builder().withProperties(rawBlockProps).withCreativeTab(rawBlockTab)
                    .build("block_raw_"+name, br, ir);
            RegistryItem rawItem = new RegistryItem.Builder().withCreativeTab(rawItemTab).build("raw_"+name, ir);
            RegistryItem dust = new RegistryItem.Builder().withCreativeTab(dustTab).build("dust_"+name, ir);
            RegistryOre res = new RegistryOre(rawBlock, rawItem, dust);

            ores.forEach((suffix, builder) -> res.addOreBlock(
                    suffix,
                    builder.build("ore_"+name+"_"+suffix, br, ir)
            ));

            return res;
        }
    }
}
