package xnaxger.prdxrename;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.ArrayList;

public final class Prdxrename extends JavaPlugin {
    private static final int MAX_LORE_LINES = 7;

    @Override
    public void onEnable() {
        getCommand("setlore").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setlore")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Только игроки с fusion могут использовать эту команду.");
                return true;
            }

            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item == null || item.getType().isAir()) {
                player.sendMessage(ChatColor.RED + "Вы должны держать предмет в руке.");
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Используйте: /setlore Номер строки Текст");
                return true;
            }

            int lineNumber;
            try {
                lineNumber = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Номер строки должен быть целым числом.");
                return true;
            }

            if (lineNumber < 0) {
                player.sendMessage(ChatColor.RED + "Номер строки должен быть больше 0.");
                return true;
            }

            if (lineNumber >= MAX_LORE_LINES) {
                player.sendMessage(ChatColor.RED + "Максимальное количество строк в описании: " + MAX_LORE_LINES);
                return true;
            }

            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            while (lore.size() <= lineNumber) {
                lore.add("");
            }

            StringBuilder text = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                text.append(args[i]).append(" ");
            }
            String loreText = ChatColor.translateAlternateColorCodes('&', text.toString().trim());

            lore.set(lineNumber, loreText);

            if (lore.size() > MAX_LORE_LINES) {
                lore = lore.subList(0, MAX_LORE_LINES);
            }


            meta.setLore(lore);
            item.setItemMeta(meta);

            player.sendMessage(ChatColor.GREEN + "Описание предмета установлено.");
            return true;

        } else if (command.getName().equalsIgnoreCase("setname")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Только игроки с fusion могут использовать эту команду.");
                return true;
            }

            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item == null || item.getType().isAir()) {
                player.sendMessage(ChatColor.RED + "Вы должны держать предмет в руке.");
                return true;
            }

            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Используйте: /setname Название");
                return true;
            }

            StringBuilder nameBuilder = new StringBuilder();
            for (String arg : args) {
                nameBuilder.append(arg).append(" ");
            }
            String newName = ChatColor.translateAlternateColorCodes('&', nameBuilder.toString().trim());

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(newName);
            item.setItemMeta(meta);

            player.sendMessage(ChatColor.GREEN + "Название предмета изменено на " + newName);
            return true;
        }
        return false;
    }
}
