package com.BlockDynasty.hytale.integrations.vaultUnlocked;

import com.BlockDynasty.hytale.utils.JavaUtil;
import net.cfh.vault.VaultUnlockedServicesManager;
import services.Console;

public class Vault {
    public static void register(){
        if(JavaUtil.classExists("net.cfh.vault.VaultUnlockedServicesManager")){
            VaultUnlockedServicesManager.get().economy(new vaultHook());
            Console.log("Vault Unlocked Hook successful");
        }else {Console.log("Vault Unlocked not detected");}
    }
    public static void unregister(){
        //VaultUnlockedServicesManager.get().economy();
    }
}