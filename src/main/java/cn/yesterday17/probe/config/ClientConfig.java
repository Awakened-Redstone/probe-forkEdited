package cn.yesterday17.probe.config;

import cn.yesterday17.probe.Probe;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public final ForgeConfigSpec.BooleanValue getMods;
    public final ForgeConfigSpec.BooleanValue getItems;
    public final ForgeConfigSpec.BooleanValue getEnchantments;
    public final ForgeConfigSpec.BooleanValue getEntities;
    public final ForgeConfigSpec.BooleanValue getFluids;
    public final ForgeConfigSpec.BooleanValue prettyPrint;

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        getMods = builder
                .comment("Gets the loaded mods if true")
                .translation(Probe.MOD_ID + ".config." + "getMods")
                .define("getMods", true);

        getItems = builder
                .comment("Gets the loaded items if true")
                .translation(Probe.MOD_ID + ".config." + "getItems")
                .define("getItems", true);

        getEnchantments = builder
                .comment("Gets the loaded enchantments if true")
                .translation(Probe.MOD_ID + ".config." + "getEnchantments")
                .define("getEnchantments", true);

        getEntities = builder
                .comment("Gets the loaded entities if true")
                .translation(Probe.MOD_ID + ".config." + "getEntities")
                .define("getEntities", true);

        getFluids = builder
                .comment("Gets the loaded fluids if true")
                .translation(Probe.MOD_ID + ".config." + "getFluids")
                .define("getFluids", true);

        prettyPrint = builder
                .comment("Generates the file with prettyPrint")
                .translation(Probe.MOD_ID + ".config." + "prettyPrint")
                .define("prettyPrint", false);
    }

}
