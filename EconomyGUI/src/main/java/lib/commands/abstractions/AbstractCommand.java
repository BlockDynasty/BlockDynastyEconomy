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

package lib.commands.abstractions;

import services.messages.MessageService;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements Command {
    private final List<Command> subCommands = new ArrayList<>();
    private final List<String> args = new ArrayList<>();
    private final String name;
    private String description="";
    private String permission="";

    public AbstractCommand( String name,String permission,List<String> args) {
        this.name = name;
        this.permission = permission;
        this.args.addAll(args);
    }

    public AbstractCommand( String name,String permission) {
        this.name = name;
        this.permission = permission;
    }

    @Override
    public void registerSubCommand(Command executor) {
        subCommands.add(executor);
    }

    @Override
    public List<Command> getSubCommands() {
        return this.subCommands;
    }

    @Override
    public List<String> getArgs() {
        return this.args;
    }

    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public String getPermission() {
        return permission;
    }

    private boolean hasArgs() {
        return !args.isEmpty();
    }
    private boolean hasPermission() {
        return !permission.isEmpty();
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if (hasPermission()) {
            if (!sender.hasPermission(getPermission())){
                sender.sendMessage(MessageService.getMessage("nopermission"));
                return false;
            }
        }

        if (hasArgs()) {
            if (args.length < getArgs().size()) {
                String stringArgs = String.join(" ", getArgs());
                sender.sendMessage(">> "+getName()+ " "+stringArgs);
                return false;
            }
        }
        return true;
    }
}
