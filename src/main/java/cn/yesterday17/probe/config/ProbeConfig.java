package cn.yesterday17.probe.config;

import cn.yesterday17.probe.Probe;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = Probe.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ProbeConfig {

    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static boolean getMods;
    public static boolean getItems;
    public static boolean getEnchantments;
    public static boolean getEntities;
    public static boolean getFluids;
    public static boolean prettyPrint;

    public static void bakeConfig() {
        getMods = CLIENT.getMods.get();
        getItems = CLIENT.getItems.get();
        getEnchantments = CLIENT.getEnchantments.get();
        getEntities = CLIENT.getEntities.get();
        getFluids = CLIENT.getFluids.get();
        prettyPrint = CLIENT.prettyPrint.get();
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == CLIENT_SPEC) {
            bakeConfig();
        }
    }
}
