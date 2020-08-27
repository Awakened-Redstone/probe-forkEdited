package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModSerializer implements JsonSerializer<ModInfo> {
    @Override
    public JsonElement serialize(ModInfo src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject mod = new JsonObject();

        try {
            mod.addProperty("modid", src.getModId());
            mod.addProperty("displayName", src.getDisplayName());
            mod.addProperty("namespace", src.getNamespace());
            mod.addProperty("version", Probe.getModVersion(src.getVersion()));
            mod.addProperty("description", src.getDescription());
            //mod.add("dependencies", context.serialize(src.getDependencies()));  //Disabled because it ends with a stack overflow error
            //mod.addProperty("updateURL", src.getUpdateURL().toString()); //Not needed
            mod.addProperty("license", src.getOwningFile().getLicense());

            if (src.getLogoFile().isPresent()) mod.addProperty("logo", src.getLogoFile().get());

            src.getConfigElement("displayURL").ifPresent(displayURL -> mod.addProperty("displayURL", (String) displayURL));
            src.getConfigElement("authors").ifPresent(authors -> mod.addProperty("authors", (String) authors));
            src.getConfigElement("credits").ifPresent(credits -> mod.addProperty("credits", (String) credits));

            if (src.getOwningFile() == null || src.getOwningFile().getMods().size()==1)
                mod.add("childMods", context.serialize(emptyList()));
            else
                mod.add("childMods", context.serialize(src.getOwningFile().getMods().stream()
                    .map(IModInfo::getDisplayName).collect(Collectors.joining(","))));

        } catch (Exception e) {
            Probe.logger.error("Failed serializing Mods!");
            Probe.logger.error(e, e);
        }

        return mod;
    }

    private List<String> emptyList() {
        return Collections.emptyList();
    }
}
