package net.blockdynasty.economy.hytale.integrations.vaultUnlocked;

import net.blockdynasty.economy.api.DynastyEconomy;
import net.blockdynasty.economy.hytale.utils.JavaUtil;
import net.blockdynasty.economy.engine.Economy;
import net.blockdynasty.economy.libs.services.Console;

import java.util.Optional;

public class Vault {
    public static void register(){
        Optional<DynastyEconomy> api = Economy.getApi();
        if (api.isEmpty()) {
            Console.log("No economy API found. Vault Unlocked integration will not be enabled.");
            return;
        }
        if(JavaUtil.classExists("net.cfh.vault.VaultUnlockedServicesManager")){
            vaultHook.hook(api.get());
            Console.log("Vault Unlocked Hook successful");
        }else {Console.log("Vault Unlocked not detected");}
    }
    public static void unregister(){
        //VaultUnlockedServicesManager.get().economy();
    }
}