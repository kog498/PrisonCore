package me.justfu498.prisoncore.util;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Utils {

    public String getPluginVersion() {
        return "1.0";
    }

    public String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public List<String> color(List<String> strings) {
        return strings.stream().map(this::color).collect(Collectors.toList());
    }

    private String toLong(double amt) {
        return String.valueOf((long) amt);
    }

    private String format(double d){
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        return format.format(d);
    }

    public String formatShort(double d){
        if (d < 1000L) {
            return format(d);
        }
        if (d < 1000000L) {
            return format(d / 1000L) + "K";
        }
        if (d < 1000000000L) {
            return format(d / 1000000L) + "M";
        }
        if (d < 1000000000000L) {
            return format(d / 1000000000L) + "B";
        }
        if (d < 1000000000000000L) {
            return format(d / 1000000000000L) + "T";
        }
        if (d < 1000000000000000000L) {
            return format(d / 1000000000000000L) + "Q";
        }

        return toLong(d);
    }

    public String formatTimeByDate(int sec) {
        if (sec < 60) {
            return sec + "s";
        }
        if (sec < 3600) {
            return sec / 60 + "m" + sec % 60 + "s";
        }
        if (sec < 86400) {
            return sec / 3600 + "h" + sec % 3600 / 60 + "m" + sec % 60 + "s";
        }
        return sec / 86400 + "d" + sec % 86400 / 3600 + "h" + sec % 86400 % 3600 / 60  + "m" + sec % 60 + "s";
    }

    private String toTwoNum(int n) {
        if (n / 10 != 0) {
            return n + "";
        }
        return "0" + n;
    }

    public String formatTimeByColon(int sec) {
        if (sec < 60) {
            return "00:00:00:" + toTwoNum(sec);
        }
        if (sec < 3600) {
            return "00:00:" + toTwoNum(sec / 60) + ":" + toTwoNum(sec % 60);
        }
        if (sec < 86400) {
            return "00:" + toTwoNum(sec / 3600) + ":" + toTwoNum(sec % 3600 / 60) + ":" + toTwoNum(sec % 60);
        }
        return toTwoNum(sec / 86400) + ":" + toTwoNum(sec % 86400 / 3600) + ":" + toTwoNum(sec % 86400 % 3600 / 60)  + ":" + toTwoNum(sec % 60);
    }

    public int toSecond(String formatTime) {
        String[] split = formatTime.split(":");
        int day = 0, hour = 0, minute = 0, second = 0;
        if (split.length > 3) {
            second = Integer.parseInt(split[3]);
        }
        if (split.length > 2) {
            minute = Integer.parseInt(split[2]);
        }
        if (split.length > 1) {
            hour = Integer.parseInt(split[1]);
        }
        day = Integer.parseInt(split[0]);
        return day * 86400 + hour * 3600 + minute * 60 + second;
    }

    public boolean isSimilar(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null) {
            return false;
        } else if (stack1 == stack2) {
            return true;
        } else {
            return stack2.getTypeId() == stack1.getTypeId() && stack2.getDurability() == stack1.getDurability() && stack2.hasItemMeta() == stack1.hasItemMeta() && (!stack2.hasItemMeta() || Bukkit.getItemFactory().equals(stack2.getItemMeta(), stack1.getItemMeta()));
        }
    }

    public String capitalizeFirstLetter(String text) {
        String[] words = text.toLowerCase().split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : words) {
            stringBuilder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return stringBuilder.toString().trim();
    }

    public void replaceItemInList(ItemStack oldItemStack, ItemStack newItemStack, List<ItemStack> itemStackList) {
        int index = itemStackList.indexOf(oldItemStack);
        if (index != -1) {
            itemStackList.remove(index);
            itemStackList.add(index, newItemStack);
        }
    }

    public boolean hasOffhand() {
        if (Bukkit.getVersion().contains("1.9") ||
                Bukkit.getVersion().contains("1.10") ||
                Bukkit.getVersion().contains("1.11") ||
                Bukkit.getVersion().contains("1.12") ||
                Bukkit.getVersion().contains("1.13") ||
                Bukkit.getVersion().contains("1.14") ||
                Bukkit.getVersion().contains("1.15") ||
                Bukkit.getVersion().contains("1.16") ||
                Bukkit.getVersion().contains("1.17")){
            return true;
        } else {
            return false;
        }
    }

    public double getProgress(double gainedValue, double neededValue) {
        double value = gainedValue / neededValue * 100;
        return Math.round(value * 100.0) / 100.0;
    }

    public boolean isOmnitoolInHand(ItemStack hand) {
        if (hand.getType() == Material.AIR) {
            return false;
        }
        NBTItem nbtItem = new NBTItem(hand);
        return nbtItem.getString("info").equals("omnitool");
    }
}
