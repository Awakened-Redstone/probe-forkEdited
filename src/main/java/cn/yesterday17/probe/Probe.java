package cn.yesterday17.probe;

import cn.yesterday17.probe.serializer.*;
import com.google.gson.GsonBuilder;
import mezz.jei.Internal;
import mezz.jei.api.runtime.IIngredientFilter;
import mezz.jei.gui.ingredients.IIngredientListElementInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import stanhebben.zenscript.symbols.SymbolPackage;
import stanhebben.zenscript.symbols.SymbolType;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeNative;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

;

@Mod("probe")
public class Probe {
    static final String MOD_ID = "probe";
    static final String NAME = "Probe";
    static final String VERSION = "@VERSION@";

    public static final Logger logger = LogManager.getLogger();

    private static GsonBuilder gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(ArtifactVersion.class, new ArtifactVersionSerializer())
            .registerTypeHierarchyAdapter(ResourceLocation.class, new ResourceLocationSerializer())
            .registerTypeHierarchyAdapter(ItemGroup.class, new CreativeTabSerializer())
            .registerTypeHierarchyAdapter(ModContainer.class, new ModSerializer())
            .registerTypeHierarchyAdapter(ZSRCFile.JEIItem.class, new JEIItemSerializer())
            .registerTypeHierarchyAdapter(Enchantment.class, new EnchantmentSerializer())
            .registerTypeHierarchyAdapter(EntityType.class, new EntitySerializer())
            .registerTypeHierarchyAdapter(Fluid.class, new FluidSerializer())
            .registerTypeHierarchyAdapter(IJavaMethod.class, new IJavaMethodSerializer())
            .registerTypeHierarchyAdapter(ZenTypeNative.class, new ZenTypeNativeSerializer())

            .serializeNulls();

    private static ZSRCFile rcFile = new ZSRCFile();

    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        rcFile.mcVersion = Minecraft.getInstance().getVersion();
        rcFile.forgeVersion = ForgeVersion.getVersion();
        rcFile.probeVersion = VERSION;

        // Mods
        rcFile.Mods = ModList.get().getMods().stream()
                .sorted(Comparator.comparing(ModInfo::getModId))
                .collect(Collectors.toList());

        // Items
        if (Internal.getRuntime() != null) {
            IIngredientFilter f = Internal.getRuntime().getIngredientFilter();
            try {
                Field field = f.getClass().getDeclaredField("elementList");
                field.setAccessible(true);
                NonNullList<IIngredientListElementInfo> elementList = (NonNullList<IIngredientListElementInfo>) field.get(f);
                rcFile.JEIItems = elementList.stream()
                        .filter(e -> e.getElement().getIngredient() instanceof ItemStack)
                        .map(ZSRCFile.JEIItem::new)
                        .sorted(Comparator.comparing(ZSRCFile.JEIItem::getSortName))
                        .collect(Collectors.toList());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // Enchantments
        rcFile.Enchantments = ForgeRegistries.ENCHANTMENTS.getValues().stream()
                .sorted(Comparator.comparing(e -> e.getRegistryName().toString()))
                .collect(Collectors.toList());

        // Entities
        rcFile.Entities = ForgeRegistries.ENTITIES.getValues().stream()
                .sorted(Comparator.comparing(e -> e.getRegistryName().toString()))
                .collect(Collectors.toList());

        // Fluids
        rcFile.Fluids = ForgeRegistries.FLUIDS.getValues().stream()
                .sorted(Comparator.comparing(f -> f.getRegistryName().toString()))
                .collect(Collectors.toList());

        // OreDictionary {not being used anymore since oreDictionary isn't a thing on latest versions}
        /*rcFile.OreDictionary = Arrays.stream(OreDictionary.getOreNames())
                .sorted()
                .collect(Collectors.toList());*/

        // ZenType
        /*rcFile.ZenType = GlobalRegistry.getTypes().getTypeMap().values().stream()
                .map(ZenType::getName)
                .filter(name -> !name.endsWith("?"))
                .sorted()
                .collect(Collectors.toList());*/

        // ZenPackages
        //rcFile.ZenPackages = getZenTypes(GlobalRegistry.getRoot());

        // CraftTweaker Globals
        /*GlobalRegistry.getGlobals().forEach((key, value) -> {
            if (value instanceof SymbolJavaStaticMethod) {
                IJavaMethod r = (IJavaMethod) getField(SymbolJavaStaticMethod.class, value, "method");
                rcFile.GlobalMethods.put(key, r);
            } else if (value instanceof SymbolJavaStaticField) {
                Field f = (Field) getField(SymbolJavaStaticField.class, value, "field");
                rcFile.GlobalFields.put(key, f.getType().getName());
            } else if (value instanceof SymbolJavaStaticGetter) {
                IJavaMethod r = (IJavaMethod) getField(SymbolJavaStaticGetter.class, value, "method");
                rcFile.GlobalGetters.put(key, r);
            }
        });*/

        // Write to .zsrc
        try {
            BufferedWriter rcBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("./scripts/.zsrc"), StandardCharsets.UTF_8
            ));
            if (ProbeConfig.setPrettyPrinting)
                gson.setPrettyPrinting();
            gson.create().toJson(rcFile, rcBufferedWriter);
            rcBufferedWriter.close();
            logger.info("Probe loaded successfully!");
        } catch (Exception e) {
            logger.error("Probe met an error while loading! Please report to author about the problem!");
            logger.error(e, e);
        }
    }


    private static Map<String, ZenType> getZenTypes(SymbolPackage primer) {
        Map<String, ZenType> result = new HashMap<>();

        primer.getPackages().forEach((str, symbol) -> {
            if (symbol instanceof SymbolPackage) {
                result.putAll(getZenTypes((SymbolPackage) symbol));
            } else if (symbol instanceof SymbolType || symbol instanceof ZenTypeNative) {
                ZenTypeNative typeNative = null;
                if (symbol instanceof SymbolType) {
                    ZenType type = ((SymbolType) symbol).getType();
                    if (type instanceof ZenTypeNative) {
                        typeNative = (ZenTypeNative) type;
                    }
                } else {
                    typeNative = (ZenTypeNative) symbol;
                }

                if (typeNative != null) {
                    result.put(typeNative.getName(), typeNative);
                }
            }
        });

        return result;
    }

    private static <T> Object getField(Class<T> cls, Object object, String key) {
        try {
            Field f = cls.getDeclaredField(key);
            f.setAccessible(true);
            return f.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}