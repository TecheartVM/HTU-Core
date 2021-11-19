package techeart.htu.registration.units;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.grower.OakTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import techeart.htu.HTUCore;
import techeart.htu.objects.WoodStuff.*;
import techeart.htu.registration.RegistryHandler;

public class RegistryWood
{
    private final WoodType type;

    private final RegistryBlock log;
    private final RegistryBlock wood;
    private final RegistryBlock strippedLog;
    private final RegistryBlock strippedWood;
    private final RegistryBlock planks;
    private final RegistryBlock stairs;
    private final RegistryBlock slab;
    private final RegistryBlock fence;
    private final RegistryBlock gate;
    private final RegistryBlock door;
    private final RegistryBlock trapdoor;
    private final RegistryBlock button;
    private final RegistryBlock plate;
    private final RegistryBlock leaves;
    private final RegistryBlock sapling;
    private final RegistryBlock saplingPotted;

    private final RegistrySign sign;
    private final RegistryItem boat;

    public RegistryWood(
            WoodType type,
            RegistryBlock log, RegistryBlock wood, RegistryBlock strippedLog, RegistryBlock strippedWood,
            RegistryBlock planks, RegistryBlock stairs, RegistryBlock slab, RegistryBlock fence, RegistryBlock gate,
            RegistryBlock door, RegistryBlock trapdoor, RegistryBlock button, RegistryBlock plate,
            RegistryBlock leaves, RegistryBlock sapling, RegistryBlock saplingPotted,
            RegistrySign sign, RegistryItem boat
    )
    {
        this.type = type;
        this.log = log;
        this.wood = wood;
        this.strippedLog = strippedLog;
        this.strippedWood = strippedWood;
        this.planks = planks;
        this.stairs = stairs;
        this.slab = slab;
        this.fence = fence;
        this.gate = gate;
        this.door = door;
        this.trapdoor = trapdoor;
        this.button = button;
        this.plate = plate;
        this.leaves = leaves;
        this.sapling = sapling;
        this.saplingPotted = saplingPotted;
        this.sign = sign;
        this.boat = boat;

        if(leaves != null) RegistryHandler.addCompostableItem(leaves::getItem, 0.3f);
        if(sapling != null) RegistryHandler.addCompostableItem(sapling::getItem, 0.3f);

        initRenderers();
    }

    public static class Builder
    {
        private boolean unprocessable = false;
        private boolean noFoliage = false;
        private float strength = 2.0f;
        private final int[] flammability = { 5,5,20,5,60,30 };
        private MaterialColor barkColor = MaterialColor.PODZOL;
        private MaterialColor coreColor = MaterialColor.WOOD;
        private MaterialColor leavesColor = MaterialColor.PLANT;
        private SoundType woodSound = SoundType.WOOD;
        private SoundType leavesSound = SoundType.GRASS;
        private CreativeModeTab tab = CreativeModeTab.TAB_BUILDING_BLOCKS;

        private AbstractTreeGrower tree = new OakTreeGrower();

        public Builder unprocessable() { unprocessable = true; return this; }
        public Builder withNoFoliage() { noFoliage = true; return this; }

        public Builder withStrength(float value) { strength = value; return this; }
        public Builder withWoodFlammability(int flammability, int fireSpreadSpeed) { this.flammability[0] = flammability; this.flammability[1] = fireSpreadSpeed; return this; }
        public Builder withPlanksFlammability(int flammability, int fireSpreadSpeed) { this.flammability[2] = flammability; this.flammability[3] = fireSpreadSpeed; return this; }
        public Builder withLeavesFlammability(int flammability, int fireSpreadSpeed) { this.flammability[4] = flammability; this.flammability[5] = fireSpreadSpeed; return this; }

        public Builder withBarkColor(MaterialColor color) { barkColor = color; return this; }
        public Builder withCoreColor(MaterialColor color) { coreColor = color; return this; }
        public Builder withLeavesColor(MaterialColor color) { leavesColor = color; return this; }

        public Builder withWoodSound(SoundType sound) { woodSound = sound; return this; }
        public Builder withLeavesSound(SoundType sound) { leavesSound = sound; return this; }

        public Builder withTreeGrower(AbstractTreeGrower grower) { tree = grower; return this; }

        public Builder withTab(CreativeModeTab tab) { tab = tab; return this; }

        public RegistryWood build(String name, String modid, RegistryHandler rh,
                                  DeferredRegister<Block> br, DeferredRegister<Item> ir,
                                  DeferredRegister<BlockEntityType<?>> ber, DeferredRegister<EntityType<?>> er
        )
        {
            WoodType woodType = WoodType.create(modid + ":" + name);

            BlockBehaviour.Properties planksProps = BlockBehaviour.Properties.of(Material.WOOD, coreColor).strength(strength).sound(woodSound);
            BlockBehaviour.Properties saplingProps = BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS);

            RegistryBlock strippedLog = null;
            RegistryBlock strippedWood = null;
            RegistryBlock planks = null;
            RegistryBlock stairs = null;
            RegistryBlock slab = null;
            RegistryBlock fence = null;
            RegistryBlock gate = null;
            RegistryBlock door = null;
            RegistryBlock trapdoor = null;
            RegistryBlock button = null;
            RegistryBlock plate = null;
            RegistryBlock leaves = null;
            RegistryBlock sapling = null;
            RegistryBlock saplingPotted = null;
            RegistrySign sign = null;
            RegistryItem boat_item = null;
            
            if(!unprocessable)
            {
                strippedLog = new RegistryBlock.Builder()
                        .withSupplier(() -> new AbstractWood(createLogProps(strength, woodSound, coreColor, coreColor), flammability[2], flammability[3]))
                        .withCreativeTab(tab)
                        .build("log_stripped_" + name, br, ir);
                strippedWood = new RegistryBlock.Builder()
                        .withSupplier(() -> new AbstractWood(createLogProps(strength, woodSound, coreColor, coreColor), flammability[2], flammability[3]))
                        .withCreativeTab(tab)
                        .build("wood_stripped_" + name, br, ir);
                final RegistryBlock planks1 = new RegistryBlock.Builder()
                        .withSupplier(() -> new Planks(planksProps, flammability[2], flammability[3]))
                        .withCreativeTab(tab)
                        .build("planks_" + name, br, ir);
                planks = planks1;
                stairs = new RegistryBlock.Builder()
                        .withSupplier(() -> new Stairs(planks1.getBlock(), flammability[2], flammability[3]))
                        .withCreativeTab(tab)
                        .build("stairs_" + name, br, ir);
                slab = new RegistryBlock.Builder()
                        .withSupplier(() -> new Planks(planksProps, flammability[2], flammability[3]))
                        .withCreativeTab(tab)
                        .build("slab_" + name, br, ir);
                fence = new RegistryBlock.Builder()
                        .withSupplier(() -> new Fence(planksProps, flammability[2], flammability[3]))
                        .withCreativeTab(tab)
                        .build("fence_" + name, br, ir);
                gate = new RegistryBlock.Builder()
                        .withSupplier(() -> new FenceGate(planksProps, flammability[2], flammability[3]))
                        .withCreativeTab(tab)
                        .build("gate_" + name, br, ir);
                door = new RegistryBlock.Builder()
                        .withSupplier(() -> new DoorBlock(planksProps))
                        .withCreativeTab(tab)
                        .build("door_" + name, br, ir);
                trapdoor = new RegistryBlock.Builder()
                        .withSupplier(() -> new TrapDoorBlock(planksProps))
                        .withCreativeTab(tab)
                        .build("trapdoor_" + name, br, ir);
                button = new RegistryBlock.Builder()
                        .withSupplier(() -> new WoodButtonBlock(planksProps))
                        .withCreativeTab(tab)
                        .build("button_" + name, br, ir);
                plate = new RegistryBlock.Builder()
                        .withSupplier(() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, planksProps))
                        .withCreativeTab(tab)
                        .build("plate_" + name, br, ir);
                sign = new RegistrySign(woodType, planksProps, tab, br, ir, ber);
                boat_item = new RegistryItem.Builder()
                        .withSupplier(()-> new RegistryBoat.ItemBoat("none",HTUCore.BOAT_TYPE))
                        .build("boat_" + name,ir);
            }

            final RegistryBlock strippedLog1 = strippedLog;
            RegistryBlock log = new RegistryBlock.Builder()
                    .withSupplier(() -> new AbstractWood(
                                createLogProps(strength, woodSound, coreColor, barkColor),
                                flammability[0], flammability[1],
                                strippedLog1.getBlock()
                            )
                    )
                    .withCreativeTab(tab)
                    .build("log_" + name, br, ir);
            final RegistryBlock strippedWood1 = strippedWood;
            RegistryBlock wood = new RegistryBlock.Builder()
                    .withSupplier(() -> new AbstractWood(
                                createLogProps(strength, woodSound, barkColor, barkColor),
                                flammability[0], flammability[1],
                                strippedWood1.getBlock()
                            )
                    )
                    .withCreativeTab(tab)
                    .build("wood_" + name, br, ir);

            if(!noFoliage)
            {
                 leaves = new RegistryBlock.Builder()
                        .withSupplier(() -> new Leaves(createLeavesProps(leavesSound, leavesColor)))
                        .withCreativeTab(tab)
                        .build("leaves_" + name, br, ir);
                 final RegistryBlock sapling1 = new RegistryBlock.Builder()
                         .withSupplier(() -> new SaplingBlock(tree, saplingProps))
                         .withCreativeTab(tab)
                         .build("sapling_" + name, br, ir);
                 sapling = sapling1;
                 saplingPotted = new RegistryBlock.Builder()
                         .withSupplier(() -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, sapling1::getBlock,
                                 BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()))
                         .withCreativeTab(tab)
                         .noItem()
                         .build("potted_" + name, br, ir);
            }

            return new RegistryWood(
                    woodType,
                    log, wood, strippedLog, strippedWood,
                    planks, stairs, slab, fence, gate, door, trapdoor, button, plate,
                    leaves, sapling, saplingPotted,
                    sign, boat_item
            );
        }
    }

    private void initRenderers()
    {
        HTUCore.RENDER_HANDLER.setBlockRenderType(door, RenderType.cutout());
        HTUCore.RENDER_HANDLER.setBlockRenderType(trapdoor, RenderType.cutout());
        if (sapling != null)
        {
            HTUCore.RENDER_HANDLER.setBlockRenderType(sapling, RenderType.cutout());
            HTUCore.RENDER_HANDLER.setBlockRenderType(saplingPotted, RenderType.cutout());
        }
//        HTUCore.RENDER_HANDLER.setBlockEntityRenderer(sign.getBlockEntityType(), SignRenderer::new);
    }

    private static BlockBehaviour.Properties createLogProps(float strength, SoundType sound, MaterialColor colorTop, MaterialColor colorSide)
    {
        return BlockBehaviour.Properties.of(
                Material.WOOD,
                (state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? colorTop : colorSide
        ).strength(strength).sound(sound);
    }

    private static BlockBehaviour.Properties createLeavesProps(SoundType sound, MaterialColor color)
    {
        return BlockBehaviour.Properties.of(Material.LEAVES, color)
                .sound(sound)
                .strength(0.2F)
                .randomTicks()
                .noOcclusion()
                .isValidSpawn((state, level, pos, entity) -> false)
                .isSuffocating((state, level, pos) -> false)
                .isViewBlocking((state, level, pos) -> false);
    }

    public WoodType getType()
    {
        return type;
    }

    public RegistryBlock getLog()
    {
        return log;
    }

    public RegistryBlock getWood()
    {
        return wood;
    }

    public RegistryBlock getStrippedLog()
    {
        return strippedLog;
    }

    public RegistryBlock getStrippedWood()
    {
        return strippedWood;
    }

    public RegistryBlock getPlanks()
    {
        return planks;
    }

    public RegistryBlock getStairs()
    {
        return stairs;
    }

    public RegistryBlock getSlab()
    {
        return slab;
    }

    public RegistryBlock getFence()
    {
        return fence;
    }

    public RegistryBlock getGate()
    {
        return gate;
    }

    public RegistryBlock getDoor()
    {
        return door;
    }

    public RegistryBlock getTrapdoor()
    {
        return trapdoor;
    }

    public RegistryBlock getButton()
    {
        return button;
    }

    public RegistryBlock getPlate()
    {
        return plate;
    }

    public RegistryBlock getLeaves()
    {
        return leaves;
    }

    public RegistryBlock getSapling()
    {
        return sapling;
    }

    public RegistryBlock getSaplingPotted()
    {
        return saplingPotted;
    }

    public RegistrySign getSign()
    {
        return sign;
    }

    public RegistryItem getBoat()
    {
        return boat;
    }
}
