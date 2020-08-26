package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraftforge.fml.ModContainer;

import java.lang.reflect.Type;

public class ModSerializer implements JsonSerializer<ModContainer> {
    @Override
    public JsonElement serialize(ModContainer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject mod = new JsonObject();

        try {
            mod.addProperty("modid", src.getModId());
            mod.addProperty("name", src.getNamespace());
            mod.addProperty("description", src.getModInfo().getDescription());
            mod.addProperty("url", src.getModInfo().getUpdateURL().toString());
            // mod.addProperty("logoFile", src.logoFile);
            mod.addProperty("version", src.getModInfo().getVersion().getMajorVersion() + src.getModInfo().getVersion().getMinorVersion() + src.getModInfo().getVersion().getIncrementalVersion() + src.getModInfo().getVersion().getBuildNumber());
            //mod.add("authorList", context.serialize(src.getModInfo().authorList));
            //mod.addProperty("credits", src.getModInfo().credits);
            // mod.add("screenshots", context.serialize(src.screenshots));
            // mod.add("parentMod", context.serialize(src.getMetadata().parentMod, SimpleModSerializer.class));
            // mod.add("childMods", context.serialize(src.getMetadata().childMods));
            //mod.add("requiredMods", context.serialize(src.getRequirements()));
            mod.add("dependencies", context.serialize(src.getModInfo().getDependencies()));
            //mod.add("dependants", context.serialize(src.getDependants()));
        } catch (Exception e) {
            Probe.logger.error("Failed serializing Mods!");
            Probe.logger.error(e, e);
        }

        return mod;
    }
}
