package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.lang.reflect.Type;

public class CreativeTabSerializer implements JsonSerializer<ItemGroup> {
    @Override
    public JsonElement serialize(ItemGroup src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject tab = new JsonObject();
        try {
            tab.addProperty("label", src.getTabLabel());
            tab.addProperty("translatedLabel", src.getTranslationKey());
            tab.addProperty("hasSearchBar", src.hasSearchBar());
            tab.addProperty("itemIcon", Item.getIdFromItem(src.getIcon().getItem()));
        } catch (Exception e) {
            Probe.logger.error("Failed serializing CreativeTabs!");
            Probe.logger.error(e, e);
        }
        return tab;
    }
}
