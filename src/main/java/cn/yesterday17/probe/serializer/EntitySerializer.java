package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityType;

import java.lang.reflect.Type;

public class EntitySerializer implements JsonSerializer<EntityType> {
    @Override
    public JsonElement serialize(EntityType src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject entity = new JsonObject();

        try {
            entity.addProperty("id", src.getRegistryName().toString());
            entity.addProperty("name", I18n.format(src.getTranslationKey()));
            entity.add("resourceLocation", context.serialize(src.getRegistryName()));
        } catch (Exception e) {
            Probe.logger.error("Failed serializing Entities!");
            Probe.logger.error(e, e);
        }
        return entity;
    }
}
