
package BlockDynasty.BukkitImplementation.utils;

import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import abstractions.platform.scheduler.ContextualTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Updater {
    private int project = -1;
    private URL checkURL;
    private String newVersion;
    private String currentVersion;
    private JavaPlugin plugin;

    private Updater(JavaPlugin plugin, int project) {
        this.project = project;
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + project);
        } catch (MalformedURLException ex) {
            Console.logError("Invalid URL for updater.");
        }
    }

    private String getResourceSpigot() {
        return "https://www.spigotmc.org/resources/" + project;
    }
    private String getResourceModrinth() {
        return "https://modrinth.com/plugin/dynastyeconomy";
    }

    private boolean checkForUpdates() {
        if (checkURL == null) return false;
        try {
            URLConnection con = checkURL.openConnection();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                this.newVersion = reader.readLine();
            }
            return newVersion != null && isVersionGreater(newVersion, currentVersion);
        } catch (IOException e) {
            Console.log("Can't check update: " + e.getMessage());
            return false;
        }
    }

    private boolean isVersionGreater(String version1, String version2) {
        String[] v1Parts = version1.split("\\.");
        String[] v2Parts = version2.split("\\.");

        int maxLength = Math.max(v1Parts.length, v2Parts.length);

        for (int i = 0; i < maxLength; i++) {
            int v1 = (i < v1Parts.length) ? Integer.parseInt(v1Parts[i]) : 0;
            int v2 = (i < v2Parts.length) ? Integer.parseInt(v2Parts[i]) : 0;

            if (v1 > v2) {
                return true;
            } else if (v1 < v2) {
                return false;
            }
        }
        return false;
    }

    private void checkAsync() {
        Scheduler.run(ContextualTask.build( ()->{
            if (checkForUpdates()) {
                Console.log("¡New version Available : " + newVersion + "!");
                Console.log("Download in Spigot: " + getResourceSpigot());
                Console.log("Download in Modrinth: " + getResourceModrinth());
            }else{Console.log("The plugin is Updated");}
        } ));
    }

    public static Updater check(JavaPlugin plugin, int project) {
        if(project == -1){
            return null;
        }
        Updater updater = new Updater(plugin,project);
        updater.checkAsync();
        return  updater;
    }

}
