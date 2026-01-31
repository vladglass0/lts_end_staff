package com.rtxbb.lts_perks;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue TELEPORT_DISTANCE;
    public static final ForgeConfigSpec.IntValue TELEPORT_COOLDOWN;

    static {
        BUILDER.push("Ender Staff Settings");
        TELEPORT_DISTANCE = BUILDER.comment("Maximum teleport distance in blocks")
                .defineInRange("teleportDistance", 50.0, 1.0, 500.0);
        TELEPORT_COOLDOWN = BUILDER.comment("Teleport cooldown in ticks (20 ticks = 1 second)")
                .defineInRange("teleportCooldown", 40, 0, 72000);
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
