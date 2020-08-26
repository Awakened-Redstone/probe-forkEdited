package cn.yesterday17.probe;

import cn.yesterday17.probe.serializer.ZSRCSerializer;
import com.google.gson.annotations.JsonAdapter;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.gui.ingredients.IIngredientListElement;
import mezz.jei.gui.ingredients.IIngredientListElementInfo;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@JsonAdapter(ZSRCSerializer.class)
public class ZSRCFile {
    String mcVersion;
    String forgeVersion;
    String probeVersion;

    List<ModInfo> Mods = new LinkedList<>();

    public static class JEIItem {
        ItemStack stack;
        IIngredientListElementInfo ingredient;

        JEIItem(IIngredientListElementInfo i) {
            this.ingredient = i;
            this.stack = (ItemStack) i.getElement().getIngredient();
        }

        public ItemStack getStack() {
            return stack;
        }

        public IIngredientListElementInfo getIngredient() {
            return ingredient;
        }

        public String getSortName() {
            return ingredient.getModNameForSorting() + ingredient.getDisplayName();
        }
    }

    List<JEIItem> JEIItems = new LinkedList<>();

    List<Enchantment> Enchantments = new LinkedList<>();

    List<EntityType> Entities = new LinkedList<>();

    List<Fluid> Fluids = new LinkedList<>();

    List<String> OreDictionary = new LinkedList<>();

    List<String> ZenType = new LinkedList<>();

    Map<String, ZenType> ZenPackages = new HashMap<>();

    Map<String, String> GlobalFields = new HashMap<>();

    Map<String, IJavaMethod> GlobalMethods = new HashMap<>();

    Map<String, IJavaMethod> GlobalGetters = new HashMap<>();

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
        return Mods;
    }

    public List<JEIItem> getJEIItems() {
        return JEIItems;
    }

    public List<Enchantment> getEnchantments() {
        return Enchantments;
    }

    public List<EntityType> getEntities() {
        return Entities;
    }

    public List<Fluid> getFluids() {
        return Fluids;
    }

    public List<String> getOreDictionary() {
        return OreDictionary;
    }

    public List<String> getZenType() {
        return ZenType;
    }

    public Map<String, ZenType> getZenPackages() {
        return ZenPackages;
    }

    public Map<String, String> getGlobalFields() {
        return GlobalFields;
    }

    public Map<String, IJavaMethod> getGlobalMethods() {
        return GlobalMethods;
    }

    public Map<String, IJavaMethod> getGlobalGetters() {
        return GlobalGetters;
    }
}
