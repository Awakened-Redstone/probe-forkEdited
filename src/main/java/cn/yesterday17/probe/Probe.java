package cn.yesterday17.probe;

import cn.yesterday17.probe.config.ProbeConfig;
import cn.yesterday17.probe.serializer.*;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.MavenVersionStringHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mod("probe")
public class Probe {
    public static final String MOD_ID = "probe";
    static final String NAME = "Probe";
    static final String VERSION = ProbeVersion.VERSION;

    public Probe() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ProbeConfig.CLIENT_SPEC);
    }

    public static final Logger logger = LogManager.getLogger();

    private static GsonBuilder gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(IModInfo.class, new ArtifactVersionSerializer())
            .registerTypeHierarchyAdapter(ResourceLocation.class, new ResourceLocationSerializer())
            .registerTypeHierarchyAdapter(ItemGroup.class, new CreativeTabSerializer())
            .registerTypeHierarchyAdapter(ModInfo.class, new ModSerializer())
            .registerTypeHierarchyAdapter(Item.class, new ItemSerializer())
            .registerTypeHierarchyAdapter(Enchantment.class, new EnchantmentSerializer())
            .registerTypeHierarchyAdapter(EntityType.class, new EntitySerializer())
            .registerTypeHierarchyAdapter(Fluid.class, new FluidSerializer())

            .serializeNulls();

    private static ZSRCFile rcFile = new ZSRCFile();

    public static String getModVersion(ArtifactVersion version) {
        return MavenVersionStringHelper.artifactVersionToString(version);
    }

    public static List<String> getItemTooltips(Item item) {
        List<String> output = new ArrayList<>();
        List<ITextComponent> tooltip = item.getDefaultInstance().getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL);

        tooltip.remove(0);

        tooltip.forEach(t -> output.add(t.getString()));

        return output;
    }

    public static List<String> getCreativeTabs(Collection<ItemGroup> creativeTabs) {
        List<String> output = new ArrayList<>();

        creativeTabs.forEach(t -> output.add(t.getTabLabel()));

        return creativeTabs.isEmpty() ? new ArrayList<>() : output;
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        //Code
        rcFile.mcVersion = Minecraft.getInstance().getVersion();
        rcFile.forgeVersion = ForgeVersion.getVersion();
        rcFile.mcVersion = VERSION;

        // Mods
        rcFile.mods = ModList.get().getMods().stream()
                .sorted(Comparator.comparing(ModInfo::getModId))
                .collect(Collectors.toList());

        // Items
        rcFile.items = ForgeRegistries.ITEMS.getValues().stream()
                .sorted(Comparator.comparing(e -> e.getRegistryName().toString()))
                .collect(Collectors.toList());

        // Enchantments
        rcFile.enchantments = ForgeRegistries.ENCHANTMENTS.getValues().stream()
                .sorted(Comparator.comparing(e -> e.getRegistryName().toString()))
                .collect(Collectors.toList());

        // Entities
        rcFile.entities = ForgeRegistries.ENTITIES.getValues().stream()
                .sorted(Comparator.comparing(e -> e.getRegistryName().toString()))
                .collect(Collectors.toList());

        // Fluids
        rcFile.fluids = ForgeRegistries.FLUIDS.getValues().stream()
                .sorted(Comparator.comparing(f -> f.getRegistryName().toString()))
                .collect(Collectors.toList());

        //Generate .zsrc file
        try {
            BufferedWriter rcBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("./scripts/.zsrc"), StandardCharsets.UTF_8));

            if (ProbeConfig.prettyPrint) gson.setPrettyPrinting();
            gson.create().toJson(rcFile, rcBufferedWriter);
            rcBufferedWriter.close();
            logger.info("Probe loaded successfully!");
        } catch (Exception e) {
            logger.error("Probe met an error while loading! Please report to author about the problem!");
            logger.error(e, e);
        }
    }
}
