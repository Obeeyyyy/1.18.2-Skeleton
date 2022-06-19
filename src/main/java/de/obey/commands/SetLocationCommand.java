package net.traxfight.backend.commands;
/*

    Author - Obey -> Kingdoms
       29.11.2021 / 18:19

*/

import net.traxfight.backend.Trax;
import net.traxfight.backend.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetLocationCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player))
            return false;

        if(!Util.hasPermission(sender, "*", true))
            return false;

        if(args.length == 1){
            Trax.getInstance().getLocationData().setLocation(args[0], ((Player) sender).getLocation());
            Util.message(sender, true, args[0] + " gesetzt.");
            return false;
        }

        Util.message(sender, true, "/setlocation <name>");

        return false;
    }
}
