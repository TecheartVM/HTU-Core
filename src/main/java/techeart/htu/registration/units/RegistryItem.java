package techeart.htu.registration.units;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RegistryItem
{
    private final RegistryObject<Item> registryObject;

    public RegistryItem(RegistryObject<Item> ro) { registryObject = ro; }

    public Item get() { return registryObject.get(); }

    public static class Builder
    {
        private Supplier<? extends Item> supplier;
        private Item.Properties props;
        private CreativeModeTab creativeTab;

        public Builder withSupplier(Supplier<? extends Item> sup)
        {
            supplier = sup;
            return this;
        }
        public Builder withProperties(Item.Properties props)
        {
            this.props = props;
            return this;
        }
        public Builder withCreativeTab(CreativeModeTab tab)
        {
            creativeTab = tab;
            return this;
        }

        public RegistryItem build(String name, DeferredRegister<Item> ir)
        {
            if(supplier == null)
            {
                Item.Properties p = props != null ? props : new Item.Properties();
                if(creativeTab == null) supplier = () -> new Item(p);
                else supplier = () -> new Item(p.tab(creativeTab));
            }
            return new RegistryItem(ir.register(name, supplier));
        }
    }
}
