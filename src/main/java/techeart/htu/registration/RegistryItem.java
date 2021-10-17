package techeart.htu.registration;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import techeart.htu.objects.HTUItem;

import java.util.function.Supplier;

public class RegistryItem
{
    private final RegistryObject<HTUItem> registryObject;

    public RegistryItem(RegistryObject<HTUItem> ro) { registryObject = ro; }

    public HTUItem get() { return registryObject.get(); }

    public static class Builder
    {
        private Supplier<? extends HTUItem> supplier;
        private Item.Properties props;
        private CreativeModeTab creativeTab;

        public Builder withSupplier(Supplier<? extends HTUItem> sup)
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
                if(creativeTab == null) supplier = () -> new HTUItem(p);
                else supplier = () -> new HTUItem(p.tab(creativeTab));
            }
            return new RegistryItem(ir.register(name, supplier));
        }
    }
}
