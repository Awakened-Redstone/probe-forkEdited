package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class ItemSerializer implements JsonSerializer<Item> {
    @Override
    public JsonElement serialize(Item src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject item = new JsonObject();
        ItemStack stack = src.getDefaultInstance();

        try {
            item.addProperty("id", src.getRegistryName().toString());
            item.addProperty("name", I18n.format(src.getTranslationKey()));
            item.addProperty("translationKey", src.getTranslationKey());
            item.addProperty("modName", src.getRegistryName().getNamespace());
            item.add("resourceLocation", context.serialize(stack.getItem().getRegistryName(), ResourceLocation.class));
            item.addProperty("maxStackSize", stack.getMaxStackSize());
            item.addProperty("maxDamage", stack.getMaxDamage());
            item.addProperty("canRepair", stack.getItem().isRepairable(stack));
            item.add("tooltips", context.serialize(Probe.getItemTooltips(src)));
            item.add("creativeTabs", context.serialize(Probe.getCreativeTabs(src.getCreativeTabs())));
        } catch (Exception e) {
            Probe.logger.error("Failed serializing JEIItems!");
            Probe.logger.error(e, e);
        }

        return item;
    }
}
