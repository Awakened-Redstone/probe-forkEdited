package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraftforge.forgespi.language.IModInfo;

import java.lang.reflect.Type;

public class ArtifactVersionSerializer implements JsonSerializer<IModInfo.ModVersion> {
    @Override
    public JsonElement serialize(IModInfo.ModVersion src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject artifactVersion = new JsonObject();
        try {
            artifactVersion.addProperty("modId", src.getModId());
            artifactVersion.addProperty("recommendedVersion", Probe.getModVersion(src.getVersionRange().getRecommendedVersion()));
            artifactVersion.addProperty("range", src.getVersionRange().toString());
        } catch (Exception e) {
            Probe.logger.error("Failed serializing ArtifactVersion!");
            Probe.logger.error(e, e);
        }
        return artifactVersion;
    }
}
