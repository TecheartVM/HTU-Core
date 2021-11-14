package techeart.htu.registration.units;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.registries.DeferredRegister;
import techeart.htu.registration.RegistryHandler;

import java.util.List;
import java.util.function.Predicate;

public class RegistryBoat
{
    private final net.minecraft.world.item.Item item;

    public RegistryBoat(String name, CreativeModeTab tab, RegistryHandler rh, DeferredRegister<Item> ir)
    {
        item = new ItemBoat(name, tab, rh.getOrCreateBoatType());
        ir.register("boat_" + name, () -> item);
    }

    public net.minecraft.world.item.Item getItem() { return item; }

    public static class EntityBoat extends Boat
    {
        private ItemBoat boatItem;

        //used by type registration method
        public EntityBoat(EntityType<? extends Boat> type, Level world) { super(type, world); }

        public EntityBoat(ItemBoat item, EntityType<? extends Boat> type, Level world, double x, double y, double z)
        {
            super(type, world);
            setPos(x, y, z);
            xo = x;
            yo = y;
            zo = z;
            setDeltaMovement(Vec3.ZERO);
            boatItem = item;
        }

        @Override
        public Item getDropItem() { return boatItem == null ? super.getDropItem() : boatItem; }

        @Override
        public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    }

    public static class ItemBoat extends Item
    {
        private static final Predicate<Entity> ENTITY_PREDICATE =
                EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

        private final String type;
        private final EntityType<Boat> entityType;

        public ItemBoat(String type, CreativeModeTab tab, EntityType<Boat> entityType)
        {
            super(new Properties().stacksTo(1).tab(tab));
            this.type = type;
            this.entityType = entityType;
        }

        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
        {
            ItemStack itemstack = player.getItemInHand(hand);
            HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
            if (hitresult.getType() == HitResult.Type.MISS) return InteractionResultHolder.pass(itemstack);
            else
            {
                Vec3 playerView = player.getViewVector(1.0F);
                List<Entity> list = level.getEntities(
                        player,
                        player.getBoundingBox().expandTowards(playerView.scale(5.0D)).inflate(1.0D),
                        ENTITY_PREDICATE
                );
                if (!list.isEmpty())
                {
                    Vec3 playerEyePos = player.getEyePosition();
                    for(Entity entity : list)
                    {
                        AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
                        if (aabb.contains(playerEyePos))
                            return InteractionResultHolder.pass(itemstack);
                    }
                }

                if (hitresult.getType() == HitResult.Type.BLOCK)
                {
                    Boat boat = new EntityBoat(this, level, hitresult.getLocation().x, hitresult.getLocation().y, hitresult.getLocation().z);
                    boat.setType(this.type);
                    boat.setYRot(player.getYRot());
                    if (!level.noCollision(boat, boat.getBoundingBox().inflate(-0.1D))) return InteractionResultHolder.fail(itemstack);
                    else
                    {
                        if (!level.isClientSide)
                        {
                            level.addFreshEntity(boat);
                            level.gameEvent(player, GameEvent.ENTITY_PLACE, new BlockPos(hitresult.getLocation()));
                            if (!player.getAbilities().instabuild)
                                itemstack.shrink(1);
                        }

                        player.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                    }
                }
                else return InteractionResultHolder.pass(itemstack);
            }
        }
    }
}
