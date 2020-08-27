package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.client.resources.I18n;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class FluidSerializer implements JsonSerializer<Fluid> {
    @Override
    public JsonElement serialize(Fluid src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonFluid = new JsonObject();

        try {
            jsonFluid.addProperty("id", src.getRegistryName().toString());
            jsonFluid.addProperty("name", I18n.format(src.getAttributes().getTranslationKey()));
            jsonFluid.addProperty("translationKey", src.getAttributes().getTranslationKey());
            jsonFluid.add("resourceLocation", context.serialize(new ResourceLocation(src.getRegistryName().toString())));
            jsonFluid.addProperty("luminosity", src.getAttributes().getLuminosity());
            jsonFluid.addProperty("density", src.getAttributes().getDensity());
            jsonFluid.addProperty("temperature", src.getAttributes().getTemperature());
            jsonFluid.addProperty("viscosity", src.getAttributes().getViscosity());
            jsonFluid.addProperty("isGaseous", src.getAttributes().isGaseous());
            jsonFluid.addProperty("rarity", src.getAttributes().getRarity().toString());
            jsonFluid.addProperty("color", src.getAttributes().getColor());
            jsonFluid.add("still", context.serialize(src.getAttributes().getStillTexture()));
            jsonFluid.add("flowing", context.serialize(src.getAttributes().getFlowingTexture()));
        } catch (Exception e) {
            Probe.logger.error("Failed serializing Fluids!");
            Probe.logger.error(e, e);
        }

        return jsonFluid;
    }
}
