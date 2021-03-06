package cc.isotopestudio.SJRedeem.sjredeem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SJRedeemCommand implements CommandExecutor {
	private final SJRedeem plugin;

	public SJRedeemCommand(SJRedeem plugin) {
		this.plugin = plugin;
	}

	public int generateRedeem() {
		int temp = (int) (Math.random() * 900000 + 100000);
		return temp;
	}

	public int getNumber(String args, int size) {
		// Get Number
		int redeem = 0;
		ArrayList<Integer> number = new ArrayList<Integer>();
		for (int i = 0; i < args.length(); i++) {
			char temp = args.charAt(i);
			if (temp < '0' || temp > '9') {
				return -1;
			}
			number.add(temp - '0');
		}
		if (size != -1)
			if (number.size() != size) {
				return -1;
			}
		for (int i = number.size() - 1; i >= 0; i--) {
			int digit = number.size() - i - 1;
			redeem += Math.pow(10, digit) * number.get(i);
		}
		return redeem;
	}

	public ItemStack getItemStack(String arg, String num) {
		int itemNumber = getNumber(arg, -1);
		ItemStack item = null;
		if (itemNumber != -1) {
			item = new ItemStack(itemNumber, Integer.parseInt(num));
		} else {
			try {
				item = new ItemStack(Material.getMaterial(arg), Integer.parseInt(num));
			} catch (Exception e) {
				item = null;
			}
		}
		return item;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sd")) {
			if (args.length > 0 && !args[0].equals("help") && sender instanceof Player) {
				Player player = (Player) sender;
				if (!player.hasPermission("SJRedeem.sd")) {
					sender.sendMessage(
							(new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("你没有权限").toString());
					return true;
				}
				if (args[0].equals("list")) {
					List<String> list = plugin.getPlayersData().getStringList("Players." + player.getName());
					if (list.size() == 0)
						sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("玩家")
								.append("你没有兑换码").toString());
					else {
						sender.sendMessage((new StringBuilder().append(ChatColor.AQUA).append("你的兑换码").toString()));
						for (String redeemString : list) {
							String redeem = redeemString.substring(0, 6);
							String times = redeemString.substring(6);
							sender.sendMessage((new StringBuilder().append(ChatColor.GRAY).append(ChatColor.BOLD)
									.append("    " + redeem).append(ChatColor.RESET).append(ChatColor.BLUE)
									.append(" 可用 " + times + " 次").toString()));
						}
					}
					return true;
				}

				if (args[0].equals("about")) {
					about(sender);
					return true;
				}

				if (args.length <= 2) {

					// Get Number
					int redeem = getNumber(args[0], 6);
					if (redeem == -1) {
						sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
								.append("这不是一个有效的兑换码").toString());
						return true;
					}

					List<String> list = plugin.getPlayersData().getStringList("Players." + player.getName());
					int index = 0;
					for (String redeemString : list) {
						String tempRedeem = redeemString.substring(0, 6);
						if (tempRedeem.equals("" + redeem)) {
							String timesString = redeemString.substring(7);

							int times = getNumber(timesString, -1);
							List<String> itemList = plugin.getConfig().getStringList("Items");
							ItemStack[] itemStacks = new ItemStack[itemList.size()];
							int j = 0;
							for (String itemLine : itemList) {
								String split[] = itemLine.split(" ");
								itemStacks[j] = getItemStack(split[0], split[1]);
								if (itemStacks[j] == null) {
									itemStacks[j] = new ItemStack(Material.AIR, 1);
									sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
											.append("配置文件出错，请联系管理员").toString());
								}
								j++;
							}

							if (args.length == 2) {

								ItemStack item = getItemStack(args[1], "1");
								if (item == null) {
									sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
											.append("物品不存在").toString());
									return true;
								}
								for (ItemStack temp : itemStacks) {
									if (temp.getType().equals(item.getType())) {
										if (player.getInventory().firstEmpty() == -1) {
											sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
													.append("背包满了？").toString());
											return true;
										}
										player.getInventory().addItem(temp);
										sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.AQUA)
												.append("成功领取" + temp.getType() + " " + temp.getAmount() + "个")
												.toString());
										if (times <= 1) {
											sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
													.append("兑换码使用次数已到").toString());
											list.remove(index);
											plugin.getPlayersData().set("Players." + player.getName(), list);
											plugin.savePlayersData();
											return true;
										}
										list.remove(index);
										list.add(redeem + " " + (times - 1));
										plugin.getPlayersData().set("Players." + player.getName(), list);
										plugin.savePlayersData();

										return true;
									}
								}
								sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
										.append("物品不存在").toString());
								return true;
							} else {
								sender.sendMessage(
										(new StringBuilder(plugin.prefix).append(ChatColor.GRAY).append(ChatColor.BOLD)
												.append("" + redeem).append(ChatColor.RESET).append(ChatColor.BLUE)
												.append(" 可用 " + times + " 次，每次下面列表中的其中一个：").toString()));
								for (ItemStack temp : itemStacks) {
									sender.sendMessage((new StringBuilder("    ").append(ChatColor.AQUA)
											.append(ChatColor.BOLD).append(temp.getType()).append(ChatColor.GRAY)
											.append("(" + temp.getTypeId() + ")").append(ChatColor.AQUA)
											.append(ChatColor.BLUE).append(" " + temp.getAmount() + " 个").toString()));

								}
								return true;
							}
						}
						index++;
					}

					sender.sendMessage(
							(new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("这不是一个有效的兑换码").toString());

					return true;
				} else {
					sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
							.append("未知命令，输入 /sd 查看帮助").toString());
					return true;
				}

			} else { // Help Menu
				sender.sendMessage(
						(new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("== 帮助菜单 ==").toString());
				sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("/sd <兑换码> <物品ID>")
						.append(ChatColor.GRAY).append(" - ").append(ChatColor.LIGHT_PURPLE).append("兑换!").toString());
				sender.sendMessage(new StringBuilder().append(ChatColor.GOLD).append("/sd list").append(ChatColor.GRAY)
						.append(" - ").append(ChatColor.LIGHT_PURPLE).append("查看你的兑换码").toString());
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
			if (!sender.hasPermission("SJRedeem.sda")) {
				sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("你没有权限").toString());
				return true;
			}
			if (args.length > 0 && !args[0].equals("help")) {
				if (args[0].equals("create")) {
					if (args.length == 3) {
						Player player = Bukkit.getServer().getPlayer(args[1]);
						String playerName;

						// Get Number
						int times = 0;
						ArrayList<Integer> number = new ArrayList<Integer>();
						for (int i = 0; i < args[2].length(); i++) {
							char temp = args[2].charAt(i);
							if (temp < '0' || temp > '9') {
								sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
										.append(args[2] + "不是一个有效数字").toString());
								return true;
							}
							number.add(temp - '0');
						}
						for (int i = number.size() - 1; i >= 0; i--) {
							int digit = number.size() - i - 1;
							times += Math.pow(10, digit) * number.get(i);
						}
						if (times <= 0) {
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
									.append(args[2] + "不是一个有效数字").toString());
							return true;
						}

						// Check player online
						if (player == null) {
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("玩家")
									.append(args[1]).append("不在线，请确认名字无误").toString());
							playerName = args[1];
						} else
							playerName = player.getName();

						int redeem = generateRedeem();
						List<String> redeemList = plugin.getPlayersData().getStringList("Players." + playerName);
						redeemList.add(redeem + " " + times);
						plugin.getPlayersData().set("Players." + playerName, redeemList);
						plugin.savePlayersData();
						sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("成功给玩家")
								.append(playerName).append("一个可使用 ").append(times).append(" 次的兑换码: ")
								.append(ChatColor.GRAY).append(ChatColor.BOLD).append(redeem).toString());
						return true;
					} else {
						sender.sendMessage((new StringBuilder()).append(ChatColor.RED)
								.append("/sda create <玩家名字> <兑换次数>").append(ChatColor.GRAY).append(" - ")
								.append(ChatColor.LIGHT_PURPLE).append("生成兑换码").toString());

						return true;
					}
				}

				if (args[0].equals("delete")) {
					if (args.length == 2) {
						Player player = Bukkit.getServer().getPlayer(args[1]);
						String playerName;
						if (player == null) {
							playerName = args[1];
						} else
							playerName = player.getName();
						if (plugin.getPlayersData().getStringList("Players." + playerName).size() == 0)
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("玩家")
									.append(playerName).append("没有兑换码").toString());
						else {
							plugin.getPlayersData().set("Players." + playerName, null);
							plugin.savePlayersData();
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.AQUA)
									.append("成功删除玩家").append(playerName).append("所有的兑换码").toString());
						}
						return true;
					} else {
						return true;
					}
				}

				if (args[0].equals("reload")) {
					plugin.onReload();
					sender.sendMessage(
							(new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("成功重载配置文件").toString());
					return true;
				}

				if (args[0].equals("about")) {
					about(sender);
					return true;
				}

				if (args[0].equals("list")) {
					if (args.length <= 2) {
						Set<String> list = plugin.getPlayersData().getKeys(true);
						if (list.size() <= 1) {
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
									.append("没有一个玩家拥有有效的兑换码").toString());
							return true;
						}

						sender.sendMessage(
								(new StringBuilder(plugin.prefix)).append(ChatColor.GOLD).append("兑换码列表").toString());
						for (String name : list) {
							if (!name.equals("Players")) {
								name = name.substring(8);
								sender.sendMessage((new StringBuilder().append(ChatColor.AQUA)
										.append("玩家 " + name + " 的兑换码").toString()));
								List<String> redeemList = plugin.getPlayersData().getStringList("Players." + name);
								for (String redeemString : redeemList) {
									String redeem = redeemString.substring(0, 6);
									String times = redeemString.substring(7);
									sender.sendMessage((new StringBuilder().append(ChatColor.GRAY)
											.append(ChatColor.BOLD).append("    " + redeem).append(ChatColor.RESET)
											.append(ChatColor.BLUE).append(" 可用 " + times + " 次").toString()));
								}
							}
						}
						return true;
					} else {
						sender.sendMessage(
								(new StringBuilder()).append(ChatColor.RED).append("/sda list").append(ChatColor.GRAY)
										.append(" - ").append(ChatColor.LIGHT_PURPLE).append("查看兑换码列表及其信息").toString());

						return true;

					}
				} else {
					sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
							.append("未知命令，输入 /sda 查看帮助").toString());
					return true;
				}
			} else { // Help Menu
				sender.sendMessage(
						(new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("== 管理菜单 ==").toString());
				sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("/sda create <玩家名字> <兑换次数>")
						.append(ChatColor.GRAY).append(" - ").append(ChatColor.LIGHT_PURPLE).append("生成兑换码")
						.toString());
				sender.sendMessage(
						(new StringBuilder()).append(ChatColor.GOLD).append("/sda delete <玩家名字>").append(ChatColor.GRAY)
								.append(" - ").append(ChatColor.LIGHT_PURPLE).append("删除玩家的 所有 兑换码！").toString());
				sender.sendMessage(
						(new StringBuilder()).append(ChatColor.GOLD).append("/sda list").append(ChatColor.GRAY)
								.append(" - ").append(ChatColor.LIGHT_PURPLE).append("查看兑换码列表及其信息").toString());
				sender.sendMessage(
						(new StringBuilder()).append(ChatColor.GOLD).append("/sda reload").append(ChatColor.GRAY)
								.append(" - ").append(ChatColor.LIGHT_PURPLE).append("重新载入配置文件").toString());
				sender.sendMessage(
						new StringBuilder().append(ChatColor.GOLD).append("/sda about").append(ChatColor.GRAY)
								.append(" - ").append(ChatColor.LIGHT_PURPLE).append("查看插件信息").toString());
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
				.append(ChatColor.RESET).append(ChatColor.AQUA).append("http://isotopestudio.cc/minecraft.html")
				.toString());
	}
}
