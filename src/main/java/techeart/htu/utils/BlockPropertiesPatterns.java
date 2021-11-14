package techeart.htu.utils;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class BlockPropertiesPatterns
{
    public static final Properties METAL = Properties.of(Material.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops();
    public static final Properties ORE = Properties.of(Material.STONE).strength(3.0F, 3.0F).sound(SoundType.STONE).requiresCorrectToolForDrops();
    public static final Properties ORE_DEEPSLATE = Properties.of(Material.STONE, MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops();
    public static final Properties ORE_NETHER = Properties.of(Material.STONE, MaterialColor.NETHER).strength(3.0F, 3.0F).sound(SoundType.NETHER_ORE).requiresCorrectToolForDrops();
    public static final Properties ORE_THE_END = Properties.of(Material.STONE, MaterialColor.SAND).strength(3.0F, 7.0F).sound(SoundType.STONE).requiresCorrectToolForDrops();
    public static final Properties RAW_ORE = Properties.of(Material.METAL, MaterialColor.RAW_IRON).strength(5.0F, 6.0F).requiresCorrectToolForDrops().sound(SoundType.METAL);
}
