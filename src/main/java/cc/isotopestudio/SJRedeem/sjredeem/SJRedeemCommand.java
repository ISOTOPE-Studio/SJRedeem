package cc.isotopestudio.SJRedeem.sjredeem;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SJRedeemCommand implements CommandExecutor {
	private final SJRedeem plugin;

	public SJRedeemCommand(SJRedeem plugin) {
		this.plugin = plugin;
	}

	public int generateRedeem() {
		int temp = (int) (Math.random() * 90000 + 10000);
		return temp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sd")) {
			if (args.length > 0 && !args[0].equals("help") && sender instanceof Player) {
				Player player = (Player) sender;

				if (args[0].equals("about")) {
					about(sender);
					return true;
				}

				// Wrong args0
				else {
					sender.sendMessage(
							(new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("未知命令").toString());
					return true;
				}

			} else { // Help Menu
				sender.sendMessage(
						(new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("== 帮助菜单 ==").toString());
				sender.sendMessage(
						(new StringBuilder()).append(ChatColor.GOLD).append("/sd <兑换码>").append(ChatColor.GRAY)
								.append(" - ").append(ChatColor.LIGHT_PURPLE).append("查看你的兑换码信息").toString());
				sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("/sd <兑换码> <物品ID>")
						.append(ChatColor.GRAY).append(" - ").append(ChatColor.LIGHT_PURPLE).append("兑换!").toString());
				sender.sendMessage(new StringBuilder().append(ChatColor.GOLD).append("/sd about").append(ChatColor.GRAY)
						.append(" - ").append(ChatColor.LIGHT_PURPLE).append("查看插件信息").toString());

				if (!(sender instanceof Player)) {
					sender.sendMessage(
							(new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("只有玩家能执行这些命令！").toString());
				}
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("sda")) {
			if (args.length > 0 && !args[0].equals("help") && sender instanceof Player) {
				if (args[0].equals("create")) {

					return true;
				}

				if (args[0].equals("about")) {
					about(sender);
					return true;
				}

				if (args[0].equals("list")) {
					if (args.length != 2) {
					}
				}
			} else { // Help Menu
				sender.sendMessage(
						(new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("== 管理菜单 ==").toString());
				sender.sendMessage(
						(new StringBuilder()).append(ChatColor.GOLD).append("/sda create <玩家名字> <兑换次数>").append(ChatColor.GRAY)
								.append(" - ").append(ChatColor.LIGHT_PURPLE).append("生成兑换码").toString());
				sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("/sda list")
						.append(ChatColor.GRAY).append(" - ").append(ChatColor.LIGHT_PURPLE).append("查看兑换码列表及其信息").toString());
				sender.sendMessage(new StringBuilder().append(ChatColor.GOLD).append("/sda about").append(ChatColor.GRAY)
						.append(" - ").append(ChatColor.LIGHT_PURPLE).append("查看插件信息").toString());

				if (!(sender instanceof Player)) {
					sender.sendMessage(
							(new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("只有玩家能执行这些命令！").toString());
				}
				return true;
			}
		}
		return false;
	}

	public void about(CommandSender sender) {
		sender.sendMessage((new StringBuilder()).append(ChatColor.GRAY).append("---- " + plugin.prefix)
				.append(ChatColor.RESET).append(ChatColor.DARK_GRAY).append(" " + plugin.version).append(ChatColor.GRAY)
				.append(" ----").toString());
		sender.sendMessage(
				(new StringBuilder()).append(ChatColor.BLUE).append(ChatColor.ITALIC).append("为服务器制作的兑换插件").toString());
		sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("制作： ")
				.append(ChatColor.RESET).append(ChatColor.AQUA).append("Mars (ISOTOPE Studio)").toString());
		sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("网址： ")
				.append(ChatColor.RESET).append(ChatColor.AQUA).append("http://isotopestudio.cc").toString());
	}
}
