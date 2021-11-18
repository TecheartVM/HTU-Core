package techeart.htu.render;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import techeart.htu.registration.units.RegistryBoat;

public class CustomBoatRenderer extends BoatRenderer
{
    private final BoatModel model;

    public CustomBoatRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        model = new BoatModel(context.bakeLayer(ModelLayers.createBoatModelName(Boat.Type.OAK)));
    }

    @Override
    public Pair<ResourceLocation, BoatModel> getModelWithLocation(Boat entity)
    {
        RegistryBoat.EntityBoat boat = (RegistryBoat.EntityBoat) entity;
        String[] type = boat.getMaterial().split(":");
        return Pair.of(
                new ResourceLocation(
                        type[0],
                        "textures/entity/boats/" + type[1] + ".png"
                ),
                model
        );
    }
}
