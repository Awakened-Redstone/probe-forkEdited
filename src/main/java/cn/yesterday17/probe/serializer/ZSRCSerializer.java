package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.ZSRCFile;
import cn.yesterday17.probe.config.ProbeConfig;
import com.google.gson.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.lang.reflect.Type;

public class ZSRCSerializer implements JsonSerializer<ZSRCFile> {
    @Override
    public JsonElement serialize(ZSRCFile src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject zsrc = new JsonObject();

        zsrc.add("mcVersion", context.serialize((src.getMcVersion())));
        zsrc.add("forgeVersion", context.serialize(src.getForgeVersion()));
        zsrc.add("probeVersion", context.serialize((src.getProbeVersion())));

        // Config
        JsonObject config = new JsonObject();
        config.addProperty("mods", ProbeConfig.getMods);
        config.addProperty("items", ProbeConfig.getItems);
        config.addProperty("enchantments", ProbeConfig.getEnchantments);
        config.addProperty("entities", ProbeConfig.getEntities);
        config.addProperty("fluids", ProbeConfig.getFluids);
        zsrc.add("config", config);

        // Mods
        JsonArray mods = new JsonArray();
        if (ProbeConfig.getMods) {
            src.getMods().forEach(mod -> mods.add(context.serialize(mod, ModInfo.class)));
        }
        zsrc.add("mods", mods);

        // Items
        JsonArray items = new JsonArray();
        if (ProbeConfig.getItems) {
            src.getJEIItems().forEach(item -> items.add(context.serialize(item, Item.class)));
        }
        zsrc.add("items", items);

        // Enchantments
        JsonArray enchantments = new JsonArray();
        if (ProbeConfig.getEnchantments) {
            src.getEnchantments().forEach(enchantment -> enchantments.add(context.serialize(enchantment, Enchantment.class)));
        }
        zsrc.add("enchantments", enchantments);

        // Entitles
        JsonArray entities = new JsonArray();
        if (ProbeConfig.getEntities) {
            src.getEntities().forEach(entity -> entities.add(context.serialize(entity, EntityType.class)));
        }
        zsrc.add("entities", entities);

        // fluids
        JsonArray fluids = new JsonArray();
        if (ProbeConfig.getFluids) {
            src.getFluids().forEach(fluid -> fluids.add(context.serialize(fluid, Fluid.class)));
        }
        zsrc.add("fluids", fluids);

        return zsrc;
    }
}
