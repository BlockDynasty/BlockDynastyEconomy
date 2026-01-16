/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package platform.files.logs;

import BlockDynasty.Economy.domain.services.log.Log;
import platform.files.Configuration;
import abstractions.platform.scheduler.ContextualTask;
import platform.files.IConfigurationEngine;
import services.Console;
import abstractions.platform.scheduler.IScheduler;
import services.configuration.IConfiguration;
import utils.UtilTime;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public abstract class AbstractLogger implements Log {
    //private final BlockDynastyEconomy plugin;
    private static boolean firstInstanceCreated = false;
    private final File folder;
    private final File latest;
    private final Set<String> toAdd;
    private volatile boolean zipping;
    private final IScheduler scheduler;


    public AbstractLogger(IConfigurationEngine config, IScheduler scheduler) {
        //this.plugin = plugin;
        this.scheduler = scheduler;
        this.folder = new File(config.getLogsPath());
        this.latest = new File(folder, "LATEST.log");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (!latest.exists()) {
            try {
                latest.createNewFile();
            } catch (IOException ex) {
                Console.logError(ex.getMessage());
            }
        }
        this.toAdd = new HashSet<>();
        this.zipping = false;

        if (!firstInstanceCreated) {
            this.zipAndReplace();
            firstInstanceCreated = true;
        }
    }

    public void log(String message) {
        try {
            StringBuilder builder = new StringBuilder();
            appendDate(builder);
            builder.append(getName());
            builder.append(message);
            writeToFile(builder.toString());
        } catch (IOException ex) {
            Console.logError(ex.getMessage());
        }
    }

    public abstract String getName();

    public void save() {
        zipAndReplace();
    }
    private void zipAndReplace() {
        zipping = true;

        scheduler.runAsync( ContextualTask.build(() -> {
            try {
                String date = UtilTime.date();
                date = date.replace("/", "-");
                File zFile = new File(folder, date + ".zip");
                int link = 1;
                while (zFile.exists()) {
                    zFile = new File(folder, (date + '[') + link + (']' + ".zip"));
                    link++;
                }
                FileOutputStream fos = new FileOutputStream(zFile);
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                FileInputStream fis = new FileInputStream(latest);
                ZipEntry zipEntry = new ZipEntry(date + ".log");
                zipOut.putNextEntry(zipEntry);
                final byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                zipOut.close();
                fis.close();
                fos.close();
                latest.delete();
                latest.createNewFile();
                PrintWriter writer = new PrintWriter(new FileWriter(latest, true));
                toAdd.forEach(writer::println);
                toAdd.clear();
                writer.close();

                zipping = false;
            } catch (Exception e) {
                Console.logError(e.getMessage());
            }
        }));
    }

    private void appendDate(StringBuilder builder) {
        builder.append('[').append(getDateAndTime()).append(']').append(' ');
    }

    private void writeToFile(String string) throws IOException {
        if (zipping) {
            toAdd.add(string);
            return;
        }
        PrintWriter writer = new PrintWriter(new FileWriter(latest, true));
        writer.println(string);
        writer.close();
    }

    private String getDateAndTime() {
        return UtilTime.now();
    }
}