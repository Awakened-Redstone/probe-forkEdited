package cn.yesterday17.probe;

import cn.yesterday17.probe.serializer.ZSRCSerializer;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("rawtypes")
@JsonAdapter(ZSRCSerializer.class)
public class ZSRCFile {
    String mcVersion;
    String forgeVersion;
    String probeVersion;

    List<ModInfo> mods = new LinkedList<>();
    List<Item> items = new LinkedList<>();
    List<Enchantment> enchantments = new LinkedList<>();
    List<EntityType> entities = new LinkedList<>();
    List<Fluid> fluids = new LinkedList<>();


    public String getMcVersion() {
        return mcVersion;
    }
    public String getForgeVersion() {
        return forgeVersion;
    }
    public String getProbeVersion() {
        return probeVersion;
    }
    public List<ModInfo> getMods() {
        return mods;
    }
    public List<Item> getJEIItems() {
        return items;
    }
    public List<Enchantment> getEnchantments() {
        return enchantments;
    }
    public List<EntityType> getEntities() {
        return entities;
    }
    public List<Fluid> getFluids() {
        return fluids;
    }
}
