package spongeV13.utils;

public class Version {
    private static final boolean isNeoForge= JavaUtil.classExists("net.neoforged.fml.loading.FMLLoader");
    private static final boolean isForge = JavaUtil.classExists("net.minecraftforge.fml.loading.FMLLoader");

    public static boolean isNeoForge() {
        return isNeoForge;
    }

    public static boolean isForge() {
        return isForge;
    }

    public static boolean isSpongeVanilla() {
        return !isNeoForge() && !isForge();
    }

}
