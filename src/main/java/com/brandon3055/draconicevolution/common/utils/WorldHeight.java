package com.brandon3055.draconicevolution.common.utils;

import net.minecraft.world.World;

import com.cardinalstar.cubicchunks.world.ICubicWorld;
import com.cardinalstar.cubicchunks.world.cube.ICubeProvider;

import cpw.mods.fml.common.Loader;

public final class WorldHeight {

    private WorldHeight() {}

    public static int getMinHeight(World world, int legacyHeight) {
        return Loader.isModLoaded("cubicchunks") ? CubicBounds.getMinHeight(world, legacyHeight) : legacyHeight;
    }

    public static int getMaxHeight(World world, int legacyHeight) {
        return Loader.isModLoaded("cubicchunks") ? CubicBounds.getMaxHeight(world, legacyHeight) : legacyHeight;
    }

    private static final class CubicBounds {

        private static int getMinHeight(World world, int legacyHeight) {
            return world.getChunkProvider() instanceof ICubeProvider ? ((ICubicWorld) world).getMinHeight()
                    : legacyHeight;
        }

        private static int getMaxHeight(World world, int legacyHeight) {
            return world.getChunkProvider() instanceof ICubeProvider ? ((ICubicWorld) world).getMaxHeight()
                    : legacyHeight;
        }
    }
}
