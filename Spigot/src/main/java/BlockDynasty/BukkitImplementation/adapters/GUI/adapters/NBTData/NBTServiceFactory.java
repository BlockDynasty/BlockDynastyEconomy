package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.NBTData;

import BlockDynasty.BukkitImplementation.utils.Version;

public class NBTServiceFactory {


    public static NBTService get() {
        if (Version.isLegacy()){
            return new NBTApi();
        }else {
            return new NBTModern();
        }
    }
}
