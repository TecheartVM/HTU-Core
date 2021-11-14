package techeart.htu.objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;

public class WoodStuff
{
    public static class AbstractWood extends RotatedPillarBlock
    {
        private final int flammability;
        private final int fireSpreadSpeed;
        private final Block stripped;

        public AbstractWood(Properties props) { this(props, 5, 5, null); }
        public AbstractWood(Properties props, @Nullable Block stripped) { this(props, 5, 5, stripped); }
        public AbstractWood(Properties props, int flammability, int fireSpreadSpeed) { this(props, flammability, fireSpreadSpeed, null); }
        public AbstractWood(Properties props, int flammability, int fireSpreadSpeed, @Nullable Block stripped)
        {
            super(props);
            this.flammability = flammability;
            this.fireSpreadSpeed = fireSpreadSpeed;
            this.stripped = stripped;
        }

        @Nullable
        @Override
        public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction)
        {
            return stripped == null ?
                    super.getToolModifiedState(state, world, pos, player, stack, toolAction) :
                    stripped.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return fireSpreadSpeed;
        }
    }

    public static class Leaves extends LeavesBlock
    {
        private final int flammability;
        private final int fireSpreadSpeed;

        public Leaves(Properties props) { this(props, 30, 60); }
        public Leaves(Properties props, int flammability, int fireSpreadSpeed)
        {
            super(props);
            this.flammability = flammability;
            this.fireSpreadSpeed = fireSpreadSpeed;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return fireSpreadSpeed;
        }
    }

    public static class Stairs extends StairBlock
    {
        private final int flammability;
        private final int fireSpreadSpeed;

        public Stairs(Block planks) { this(planks, 20, 5); }
        public Stairs(Block planks, int flammability, int fireSpreadSpeed)
        {
            super(planks::defaultBlockState, Properties.copy(planks));
            this.flammability = flammability;
            this.fireSpreadSpeed = fireSpreadSpeed;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return fireSpreadSpeed;
        }
    }

    public static class Planks extends Block
    {
        private final int flammability;
        private final int fireSpreadSpeed;

        public Planks(Properties props) { this(props, 20, 5); }
        public Planks(Properties props, int flammability, int fireSpreadSpeed)
        {
            super(props);
            this.flammability = flammability;
            this.fireSpreadSpeed = fireSpreadSpeed;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return fireSpreadSpeed;
        }
    }

    public static class Slab extends SlabBlock
    {
        private final int flammability;
        private final int fireSpreadSpeed;

        public Slab(Properties props) { this(props, 20, 5); }
        public Slab(Properties props, int flammability, int fireSpreadSpeed)
        {
            super(props);
            this.flammability = flammability;
            this.fireSpreadSpeed = fireSpreadSpeed;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return fireSpreadSpeed;
        }
    }

    public static class Fence extends FenceBlock
    {
        private final int flammability;
        private final int fireSpreadSpeed;

        public Fence(Properties props) { this(props, 20, 5); }
        public Fence(Properties props, int flammability, int fireSpreadSpeed)
        {
            super(props);
            this.flammability = flammability;
            this.fireSpreadSpeed = fireSpreadSpeed;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return fireSpreadSpeed;
        }
    }

    public static class FenceGate extends FenceGateBlock
    {
        private final int flammability;
        private final int fireSpreadSpeed;

        public FenceGate(Properties props) { this(props, 20, 5); }
        public FenceGate(Properties props, int flammability, int fireSpreadSpeed)
        {
            super(props);
            this.flammability = flammability;
            this.fireSpreadSpeed = fireSpreadSpeed;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return flammability;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
        {
            return fireSpreadSpeed;
        }
    }
}
