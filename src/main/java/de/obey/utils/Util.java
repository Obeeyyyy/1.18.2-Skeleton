package de.obey.utils;
/*

    Author - Obey -> Max Workspace
       02.08.2021 / 18:15

*/

import de.obey.Startup;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static final Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String translateHex(String string){
        Matcher matcher = hexPattern.matcher(string);

        while(matcher.find()){
            final String hexCode = string.substring(matcher.start(), matcher.end());
            string = string.replace(hexCode, ChatColor.of(hexCode) + "");
            matcher = hexPattern.matcher(string);
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static TextComponent getCommandOnKlickMessage(final String command, final String message){
        final TextComponent text = new TextComponent(translateHex(message));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return text;
    }

    public static void message(final CommandSender sender, final String... messages) {
        for (final String message : messages)
            sender.sendMessage(translateHex(message));
    }

    public static void message(final CommandSender sender, final boolean prefix, final String... messages) {
        if(prefix) {
            sender.sendMessage(translateHex(Startup.getInstance().getServerData().getData().getString("prefix") + messages[0]));
        }else{
            sender.sendMessage(translateHex(messages[0]));
        }

        for (int i = 1; i < messages.length; i++)
            sender.sendMessage(translateHex(messages[i]));
    }

    public static void broadcast(final boolean prefix, final String... messages) {
        if(prefix) {
            Bukkit.broadcastMessage(translateHex(Startup.getInstance().getServerData().getData().getString("prefix") + messages[0]));
        }else{
            Bukkit.broadcastMessage(translateHex(messages[0]));
        }

        for (int i = 1; i < messages.length; i++)
            Bukkit.broadcastMessage(translateHex(messages[i]));
    }

    public static void console(final String... messages) {
        for (final String message : messages)
            Bukkit.getConsoleSender().sendMessage(translateHex(message));
    }

    public static String getLongWithDots(final long value) {
        return NumberFormat.getInstance().format(value);
    }

    public static String getStringFromMillis(long millis) {
        if (millis < 0)
            return "PERMANENT";

        int hours = 0, minutes = 0, seconds = 0;

        while (millis >= 1000) {
            seconds++;
            millis -= 1000;
        }

        while (seconds >= 60) {
            minutes++;
            seconds -= 60;
        }

        while (minutes >= 60) {
            hours++;
            minutes -= 60;
        }

        final String hoursString = (hours > 0) ? hours + " H " : "";
        final String minutesString = (minutes > 0) ? minutes + " M " : "";
        final String secondsString = (seconds > 0) ? seconds + " S " : "";

        return hoursString + minutesString + secondsString;
    }

    public static String replaceLongWithSuffix(long amount) {
        String bearbeite = "";

        final DecimalFormat df = new DecimalFormat("#,###.##");

        bearbeite = df.format(amount);

        if (amount <= 999)
            return "" + amount;

        if (bearbeite.length() == 5) {
            bearbeite = bearbeite.substring(0, 3) + "k";
        } else if (bearbeite.length() == 6) {
            bearbeite = bearbeite.substring(0, 4) + "k";
        } else if (bearbeite.length() == 7) {
            bearbeite = bearbeite.substring(0, 5) + "k";
        } else if (bearbeite.length() == 9) {
            bearbeite = bearbeite.substring(0, 3) + "m";
        } else if (bearbeite.length() == 10) {
            bearbeite = bearbeite.substring(0, 4) + "m";
        } else if (bearbeite.length() == 11) {
            bearbeite = bearbeite.substring(0, 5) + "m";
        } else if (bearbeite.length() == 13) {
            bearbeite = bearbeite.substring(0, 3) + "b";
        } else if (bearbeite.length() == 14) {
            bearbeite = bearbeite.substring(0, 4) + "b";
        } else if (bearbeite.length() == 15) {
            bearbeite = bearbeite.substring(0, 5) + "b";
        } else if (bearbeite.length() == 17) {
            bearbeite = bearbeite.substring(0, 3) + "T";
        } else if (bearbeite.length() == 18) {
            bearbeite = bearbeite.substring(0, 4) + "T";
        } else if (bearbeite.length() == 19) {
            bearbeite = bearbeite.substring(0, 5) + "T";
        } else {
            bearbeite = amount + "";
        }

        return bearbeite;
    }

    public static long getMillisFromString(final String string) {
        // 10D 10H 6M

        if (string.equalsIgnoreCase("PERMANENT"))
            return -1;

        final String[] splitted = string.split(" ");

        long millis = 0;

        if (string.length() == 1) {
            for (final String a : splitted) {
                Bukkit.broadcastMessage(a);

                final long value = Integer.parseInt(a.substring(0, a.length() - 1).replace(" ", ""));
                final String type = a.substring(a.length() - 1);

                if (type.equalsIgnoreCase("s"))
                    millis += value * 1000;

                if (type.equalsIgnoreCase("m"))
                    millis += value * 60000;

                if (type.equalsIgnoreCase("h"))
                    millis += value * 3600000;

                if (type.equalsIgnoreCase("d"))
                    millis += value * 86400000;
            }
            return millis;
        }


        final long value = Integer.parseInt(string.substring(0, string.length() - 1).replace(" ", ""));
        final String type = string.substring(string.length() - 1);

        if (type.equalsIgnoreCase("s"))
            millis += value * 1000;

        if (type.equalsIgnoreCase("m"))
            millis += value * 60000;

        if (type.equalsIgnoreCase("h"))
            millis += value * 3600000;

        if (type.equalsIgnoreCase("d"))
            millis += value * 86400000;

        return millis;
    }

    public static void saveJSONObject(final JSONObject jsonObject, final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            final FileWriter fileWriter = new FileWriter(file);
            final String[] splitted = jsonObject.toString().split(",");

            String cleanedText = "";

            for(String line : splitted)
                cleanedText = cleanedText + System.getProperty("line.separator") + "  " + line + ",";

            fileWriter.write(cleanedText.substring(0, cleanedText.length() - 1));

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject loadJSONObject(final File file) throws IOException {
        JSONObject jsonObject = null;
        jsonObject = new JSONObject(new String(Files.readAllBytes(file.toPath())));

        return jsonObject;
    }

    public static void setTabList(final Player player, final String header, final String footer) {
        if (player == null)
            return;

        player.setPlayerListHeaderFooter(translateHex(header), translateHex(footer));
    }

    public static boolean isOnline(final CommandSender sender, final CommandSender target, final String name){
        if(target == Bukkit.getConsoleSender())
            return true;

        if(target == null  || !((Player) target).isOnline()){
            message(sender, true, name + " ist nicht Online.");

            if(sender instanceof Player)
                ((Player)sender).playSound(((Player)sender).getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);

            return false;
        }

        return true;
    }

    public static ArrayList<String> stringToArrayList(String string){
        if(string.equalsIgnoreCase(""))
            return new ArrayList<>();

        final String[] splitted = string.split("-");
        final ArrayList<String> list = new ArrayList<>();

        list.addAll(List.of(splitted));

        return list;
    }

    public static String arrayListToString(final ArrayList<String> list){
        String string = list.toString().replace("[", "").replace("]", "").replace(",", "-");

        if(string.equalsIgnoreCase(" "))
            string = "";

        return string;
    }


    public static Location getLocationFromString(final String string){
        // world#x#y#z#yaw#pitch
        final String[] splitted = string.split("#");
        return new Location(Bukkit.getWorld(splitted[0]), Double.parseDouble(splitted[1]), Double.parseDouble(splitted[2]), Double.parseDouble(splitted[3]));
    }

    public static String getStringFromLocation(final Location location){
        return location.getWorld().getName() + "#" + location.getX() + "#" + location.getY() + "#" + location.getZ() + "#" + location.getYaw() + "#" + location.getPitch();
    }
}