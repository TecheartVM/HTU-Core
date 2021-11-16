package techeart.htu.registration.units;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import techeart.htu.render.CustomBoatRenderer;
import techeart.htu.utils.RenderHandler;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RegistryBoat
{
    private final Item item;
    private final Supplier<EntityType<Boat>> entityType;

    public RegistryBoat(String name, CreativeModeTab tab, RegistryHandler rh, DeferredRegister<Item> ir)
    {
        entityType = rh.getBoatEntityType();
        item = new ItemBoat(name, tab, entityType);
        ir.register("boat_" + name, () -> item);

        RenderHandler.setEntityRenderer(entityType.get(), CustomBoatRenderer::new);
    }

    public EntityType<Boat> getType() { return entityType.get(); }

    public Item getItem() { return item; }

    public static class EntityBoat extends Boat
    {
        private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(EntityBoat.class, EntityDataSerializers.STRING);
        private ItemBoat boatItem;

        //used by type registration method
        public EntityBoat(EntityType<? extends Boat> type, Level level) { super(type, level); }

        public EntityBoat(ItemBoat item, EntityType<? extends Boat> type, Level level, double x, double y, double z)
        {
            super(type, level);
            setPos(x, y, z);
            xo = x;
            yo = y;
            zo = z;
            setDeltaMovement(Vec3.ZERO);
            boatItem = item;
        }

        @Override
        protected void defineSynchedData()
        {
            super.defineSynchedData();
            entityData.define(TYPE, "none");
        }

        public void setTypeData(String type) { entityData.set(TYPE, type); }
        public String getTypeData() { return entityData.get(TYPE); }

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
        private final Supplier<EntityType<Boat>> entityType;

        public ItemBoat(String type, CreativeModeTab tab, Supplier<EntityType<Boat>> entityType)
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
                    EntityBoat boat = new EntityBoat(this, entityType.get(), level, hitresult.getLocation().x, hitresult.getLocation().y, hitresult.getLocation().z);
                    boat.setTypeData(type);
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
