package com.example.creepercat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class CatPlayerState {
    public static final String KEY_CAT = "creepercat_is_cat";
    public static final String KEY_EVO = "creepercat_evo";
    public static final String KEY_ANIM_TICKS = "creepercat_anim";

    public static boolean isCat(PlayerEntity p) {
        return p.getPersistentData().getBoolean(KEY_CAT);
    }

    public static int getEvo(PlayerEntity p) {
        return p.getPersistentData().getInt(KEY_EVO);
    }

    public static void setCat(PlayerEntity p, boolean v) {
        NbtCompound tag = p.getPersistentData();
        tag.putBoolean(KEY_CAT, v);
    }

    public static void addEvo(PlayerEntity p, int delta) {
        NbtCompound tag = p.getPersistentData();
        tag.putInt(KEY_EVO, tag.getInt(KEY_EVO) + delta);
    }

    public static int getStage(int evo) {
        if (evo >= 15) return 3;
        if (evo >= 5) return 2;
        return 1;
    }

    public static float stageScale(int stage) {
        return switch (stage) {
            case 3 -> 2.2f;
            case 2 -> 1.5f;
            default -> 1.0f;
        };
    }

    public static void triggerRoarAnim(PlayerEntity p) {
        p.getPersistentData().putInt(KEY_ANIM_TICKS, 40);
    }

    public static int consumeAnim(PlayerEntity p) {
        NbtCompound t = p.getPersistentData();
        int ticks = t.getInt(KEY_ANIM_TICKS);
        if (ticks > 0) {
            t.putInt(KEY_ANIM_TICKS, ticks - 1);
            return ticks;
        }
        return 0;
    }
}
