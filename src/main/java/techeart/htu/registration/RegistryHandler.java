package techeart.htu.registration;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacerType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import techeart.htu.HTUCore;
import techeart.htu.registration.units.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RegistryHandler
{
    private final DeferredRegister<Activity>                        ACTIVITIES;
    private final DeferredRegister<Attribute>                       ATTRIBUTES;
    private final DeferredRegister<Biome>                           BIOMES;
    private final DeferredRegister<Block>                           BLOCKS;
    private final DeferredRegister<BlockEntityType<?>>              BLOCK_ENTITIES;
    private final DeferredRegister<BlockPlacerType<?>>              BLOCK_PLACER_TYPES;
    private final DeferredRegister<BlockStateProviderType<?>>       BLOCK_STATE_PROVIDER_TYPES;
    private final DeferredRegister<ChunkStatus>                     CHUNK_STATUS;
    private final DeferredRegister<MenuType<?>>                     CONTAINERS;
    private final DeferredRegister<DataSerializerEntry>             DATA_SERIALIZERS;
    private final DeferredRegister<FeatureDecorator<?>>             DECORATORS;
    private final DeferredRegister<Enchantment>                     ENCHANTMENTS;
    private final DeferredRegister<EntityType<?>>                   ENTITIES;
    private final DeferredRegister<Feature<?>>                      FEATURES;
    private final DeferredRegister<Fluid>                           FLUIDS;
    private final DeferredRegister<FoliagePlacerType<?>>            FOLIAGE_PLACER_TYPES;
    private final DeferredRegister<Item>                            ITEMS;
    private final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS;
    private final DeferredRegister<MemoryModuleType<?>>             MEMORY_MODULE_TYPES;
    private final DeferredRegister<MobEffect>                       MOB_EFFECTS;
    private final DeferredRegister<Motive>                          PAINTING_TYPES;
    private final DeferredRegister<ParticleType<?>>                 PARTICLE_TYPES;
    private final DeferredRegister<PoiType>                         POI_TYPES;
    private final DeferredRegister<Potion>                          POTIONS;
    private final DeferredRegister<VillagerProfession>              PROFESSIONS;
    private final DeferredRegister<RecipeSerializer<?>>             RECIPE_SERIALIZERS;
    private final DeferredRegister<Schedule>                        SCHEDULES;
    private final DeferredRegister<SensorType<?>>                   SENSOR_TYPES;
    private final DeferredRegister<SoundEvent>                      SOUND_EVENTS;
    private final DeferredRegister<StatType<?>>                     STAT_TYPES;
    private final DeferredRegister<StructureFeature<?>>             STRUCTURE_FEATURES;
    private final DeferredRegister<SurfaceBuilder<?>>               SURFACE_BUILDERS;
    private final DeferredRegister<TreeDecoratorType<?>>            TREE_DECORATOR_TYPES;
    private final DeferredRegister<WorldCarver<?>>                  WORLD_CARVERS;
    private final DeferredRegister<ForgeWorldType>                  WORLD_TYPES;

    private static final Map<Supplier<Item>, Float> CUSTOM_COMPOSTABLES = new HashMap<>();

    private final String modid;

    public RegistryHandler(String modid)
    {
        this.modid = modid;

        ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, modid);
        ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, modid);
        BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, modid);
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, modid);
        BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, modid);
        BLOCK_PLACER_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_PLACER_TYPES, modid);
        BLOCK_STATE_PROVIDER_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, modid);
        CHUNK_STATUS = DeferredRegister.create(ForgeRegistries.CHUNK_STATUS, modid);
        CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, modid);
        DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, modid);
        DECORATORS = DeferredRegister.create(ForgeRegistries.DECORATORS, modid);
        ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, modid);
        ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, modid);
        FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, modid);
        FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, modid);
        FOLIAGE_PLACER_TYPES = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, modid);
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, modid);
        LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, modid);
        MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, modid);
        MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, modid);
        PAINTING_TYPES = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, modid);
        PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, modid);
        POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, modid);
        POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, modid);
        PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, modid);
        RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, modid);
        SCHEDULES = DeferredRegister.create(ForgeRegistries.SCHEDULES, modid);
        SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, modid);
        SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, modid);
        STAT_TYPES = DeferredRegister.create(ForgeRegistries.STAT_TYPES, modid);
        STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, modid);
        SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, modid);
        TREE_DECORATOR_TYPES = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, modid);
        WORLD_CARVERS = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, modid);
        WORLD_TYPES = DeferredRegister.create(ForgeRegistries.WORLD_TYPES, modid);
    }

    public RegistryBlock register(String blockName, RegistryBlock.Builder blockBuilder) { return blockBuilder.build(blockName, BLOCKS, ITEMS); }
    public RegistryItem register(String itemName, RegistryItem.Builder itemBuilder) { return itemBuilder.build(itemName, ITEMS); }
    public RegistryMetal register(String metalName, RegistryMetal.Builder metalBuilder) { return metalBuilder.build(metalName, BLOCKS, ITEMS); }
    public RegistryOre register(String oreName, RegistryOre.Builder oreBuilder) { return oreBuilder.build(oreName, BLOCKS, ITEMS); }
    public RegistryWood register(String woodName, RegistryWood.Builder woodBuilder) { return woodBuilder.build(woodName, modid, this, BLOCKS, ITEMS, BLOCK_ENTITIES, ENTITIES); }
    public <T extends Entity> EntityType<T> register(String entityName, EntityType.Builder<T> typeBuilder, EntityRendererProvider<T> renderer)
    {
        EntityType<T> type = typeBuilder.build(entityName);
        ENTITIES.register(entityName,()-> type);
        HTUCore.RENDER.setEntityRenderer(type,renderer);
        return type;
    };
    public <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.BlockEntitySupplier<T> sup, BlockEntityRendererProvider<T> render, Block... blocks){
        BlockEntityType<T> type = BlockEntityType.Builder.of(sup,blocks).build(null);
        BLOCK_ENTITIES.register(name,()-> type);
        HTUCore.RENDER.addBlockEntityRenderer(type,render);
        return type;
    };

    public void register(IEventBus bus)
    {
        ACTIVITIES.register(bus);
        ATTRIBUTES.register(bus);
        BIOMES.register(bus);
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
        BLOCK_PLACER_TYPES.register(bus);
        BLOCK_STATE_PROVIDER_TYPES.register(bus);
        CHUNK_STATUS.register(bus);
        CONTAINERS.register(bus);
        DATA_SERIALIZERS.register(bus);
        DECORATORS.register(bus);
        ENCHANTMENTS.register(bus);
        ENTITIES.register(bus);
        FEATURES.register(bus);
        FLUIDS.register(bus);
        FOLIAGE_PLACER_TYPES.register(bus);
        ITEMS.register(bus);
        LOOT_MODIFIER_SERIALIZERS.register(bus);
        MEMORY_MODULE_TYPES.register(bus);
        MOB_EFFECTS.register(bus);
        PAINTING_TYPES.register(bus);
        PARTICLE_TYPES.register(bus);
        POI_TYPES.register(bus);
        POTIONS.register(bus);
        PROFESSIONS.register(bus);
        RECIPE_SERIALIZERS.register(bus);
        SCHEDULES.register(bus);
        SENSOR_TYPES.register(bus);
        SOUND_EVENTS.register(bus);
        STAT_TYPES.register(bus);
        STRUCTURE_FEATURES.register(bus);
        SURFACE_BUILDERS.register(bus);
        TREE_DECORATOR_TYPES.register(bus);
        WORLD_CARVERS.register(bus);
        WORLD_TYPES.register(bus);

        for (Supplier<Item> item : CUSTOM_COMPOSTABLES.keySet())
            ComposterBlock.COMPOSTABLES.put(item.get(), CUSTOM_COMPOSTABLES.get(item));
    }

    public CreativeModeTab registerCreativeTab(Item icon)
    {
        return new CreativeModeTab(modid)
        {
            @Override
            public ItemStack makeIcon() { return new ItemStack(icon); }
        };
    }

    public static void addCompostableItem(Supplier<Item> item, float gain) { CUSTOM_COMPOSTABLES.put(item, gain); }
}
