package org.spigotmc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.text.DecimalFormat;

public class TicksPerSecondCommand extends Command {
    private static final DecimalFormat DF = new DecimalFormat("########0.0");

    public TicksPerSecondCommand(String name) {
        super(name);
        this.description = "Gets the current ticks per second for the server";
        this.usageMessage = "/tps";
        this.setPermission("bukkit.command.tps");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        // Paper start - Further improve tick handling
        double[] tps = org.bukkit.Bukkit.getTPS();
        double[] tickTimes = org.bukkit.Bukkit.getTickTime();
        String[] tpsAvg = new String[tps.length];
        String[] tickTimesAvg = new String[tickTimes.length];

        for (int i = 0; i < tps.length; i++) {
            tpsAvg[i] = format(tps[i]);
        }

        for (int i = 0; i < tickTimes.length; i++) {
            tickTimesAvg[i] = color(tickTimes[i]);
        }
        sender.sendMessage(ChatColor.GOLD + "Server tick time from last 5s, 10s, 1m: " + org.apache.commons.lang.StringUtils.join(tickTimesAvg, ", "));
        sender.sendMessage(ChatColor.GOLD + "TPS from last 5s, 1m, 5m, 15m: " + org.apache.commons.lang.StringUtils.join(tpsAvg, ", "));
        sender.sendMessage(ChatColor.GOLD + "Current Memory Usage: " + ChatColor.GREEN + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "/" + (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " mb (Max: " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " mb)");
        // Paper end
        return true;
    }

    private String color(double avg) {
        avg = Double.parseDouble(DF.format(avg));
        return (avg < 40.0 ? ChatColor.GREEN : (avg < 50 ? ChatColor.YELLOW : ChatColor.RED)).toString() + avg;
    }

    private static String format(double tps) {
        return ((tps > 18.0) ? ChatColor.GREEN : (tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED).toString()
                + ((tps > 21.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0); // Paper - only print * at 21, we commonly peak to 20.02 as the tick sleep is not accurate enough, stop the noise
    }
}
