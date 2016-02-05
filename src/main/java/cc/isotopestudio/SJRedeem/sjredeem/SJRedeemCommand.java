package cc.isotopestudio.SJRedeem.sjredeem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
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
		int temp = (int) (Math.random() * 900000 + 100000);
		return temp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sd")) {
			if (args.length > 0 && !args[0].equals("help") && sender instanceof Player) {
				Player player = (Player) sender;

				if (args[0].equals("list")) {
					List<String> list = plugin.getPlayersData().getStringList("Players." + player.getName());
					if (list.size() == 0)
						sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("���")
								.append("��û�жһ���").toString());
					else {
						sender.sendMessage((new StringBuilder().append(ChatColor.AQUA).append("��Ķһ���").toString()));
						for (String redeemString : list) {
							String redeem = redeemString.substring(0, 6);
							String times = redeemString.substring(6);
							sender.sendMessage((new StringBuilder().append(ChatColor.GRAY).append(ChatColor.BOLD)
									.append("    " + redeem).append(ChatColor.RESET).append(ChatColor.BLUE)
									.append(" ���� " + times + " ��").toString()));
						}
					}
					return true;
				}

				if (args[0].equals("about")) {
					about(sender);
					return true;
				}

				if (args.length == 2) {

					// Get Number
					int redeem = 0;
					ArrayList<Integer> number = new ArrayList<Integer>();
					for (int i = 0; i < args[0].length(); i++) {
						char temp = args[0].charAt(i);
						if (temp < '0' || temp > '9') {
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
									.append("�ⲻ��һ����Ч�Ķһ���").toString());
							return true;
						}
						number.add(temp - '0');
					}
					if (number.size() != 6) {
						sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
								.append("�ⲻ��һ����Ч�Ķһ���").toString());
						return true;
					}
					for (int i = number.size() - 1; i >= 0; i--) {
						int digit = number.size() - i - 1;
						redeem += Math.pow(10, digit) * number.get(i);
					}

					List<String> list = plugin.getPlayersData().getStringList("Players." + player.getName());
					int index = 0;
					for (String redeemString : list) {
						String tempRedeem = redeemString.substring(0, 6);
						if (tempRedeem.equals("" + redeem)) {
							String timesString = redeemString.substring(7);
							// Get Number
							int times = 0;
							number = new ArrayList<Integer>();
							for (int i = 0; i < timesString.length(); i++) {
								char temp = timesString.charAt(i);
								number.add(temp - '0');
							}
							for (int i = number.size() - 1; i >= 0; i--) {
								int digit = number.size() - i - 1;
								times += Math.pow(10, digit) * number.get(i);
							}
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("�ɹ���ȡ")
									.toString());

							if (times <= 1) {
								sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
										.append("�һ���ʹ�ô����ѵ�").toString());
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
						index++;
					}

					sender.sendMessage(
							(new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("�ⲻ��һ����Ч�Ķһ���").toString());

					return true;
				} else {
					sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
							.append("δ֪������� /sd �鿴����").toString());
					return true;
				}

			} else { // Help Menu
				sender.sendMessage(
						(new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("== �����˵� ==").toString());
				sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("/sd <�һ���> <��ƷID>")
						.append(ChatColor.GRAY).append(" - ").append(ChatColor.LIGHT_PURPLE).append("�һ�!").toString());
				sender.sendMessage(new StringBuilder().append(ChatColor.GOLD).append("/sd list").append(ChatColor.GRAY)
						.append(" - ").append(ChatColor.LIGHT_PURPLE).append("�鿴��Ķһ���").toString());
				sender.sendMessage(new StringBuilder().append(ChatColor.GOLD).append("/sd about").append(ChatColor.GRAY)
						.append(" - ").append(ChatColor.LIGHT_PURPLE).append("�鿴�����Ϣ").toString());

				if (!(sender instanceof Player)) {
					sender.sendMessage(
							(new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("ֻ�������ִ����Щ���").toString());
				}
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("sda")) {
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
										.append(args[2] + "����һ����Ч����").toString());
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
									.append(args[2] + "����һ����Ч����").toString());
							return true;
						}

						// Check player online
						if (player == null) {
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("���")
									.append(args[1]).append("�����ߣ���ȷ����������").toString());
							playerName = args[1];
						} else
							playerName = player.getName();

						int redeem = generateRedeem();
						List<String> redeemList = plugin.getPlayersData().getStringList("Players." + playerName);
						redeemList.add(redeem + " " + times);
						plugin.getPlayersData().set("Players." + playerName, redeemList);
						plugin.savePlayersData();
						sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("�ɹ������")
								.append(playerName).append("һ����ʹ�� ").append(times).append(" �εĶһ���: ")
								.append(ChatColor.GRAY).append(ChatColor.BOLD).append(redeem).toString());
						return true;
					} else {
						sender.sendMessage((new StringBuilder()).append(ChatColor.RED)
								.append("/sda create <�������> <�һ�����>").append(ChatColor.GRAY).append(" - ")
								.append(ChatColor.LIGHT_PURPLE).append("���ɶһ���").toString());

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
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("���")
									.append(playerName).append("û�жһ���").toString());
						else {
							plugin.getPlayersData().set("Players." + playerName, null);
							plugin.savePlayersData();
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.AQUA)
									.append("�ɹ�ɾ�����").append(playerName).append("���еĶһ���").toString());
						}
						return true;
					} else {
						return true;
					}
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
									.append("û��һ�����ӵ����Ч�Ķһ���").toString());
							return true;
						}

						sender.sendMessage(
								(new StringBuilder(plugin.prefix)).append(ChatColor.GOLD).append("�һ����б�").toString());
						for (String name : list) {
							if (!name.equals("Players")) {
								name = name.substring(8);
								sender.sendMessage((new StringBuilder().append(ChatColor.AQUA)
										.append("��� " + name + " �Ķһ���").toString()));
								List<String> redeemList = plugin.getPlayersData().getStringList("Players." + name);
								for (String redeemString : redeemList) {
									String redeem = redeemString.substring(0, 6);
									String times = redeemString.substring(7);
									sender.sendMessage((new StringBuilder().append(ChatColor.GRAY)
											.append(ChatColor.BOLD).append("    " + redeem).append(ChatColor.RESET)
											.append(ChatColor.BLUE).append(" ���� " + times + " ��").toString()));
								}
							}
						}
						return true;
					} else {
						sender.sendMessage(
								(new StringBuilder()).append(ChatColor.RED).append("/sda list").append(ChatColor.GRAY)
										.append(" - ").append(ChatColor.LIGHT_PURPLE).append("�鿴�һ����б�����Ϣ").toString());

						return true;

					}
				} else {
					sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
							.append("δ֪������� /sda �鿴����").toString());
					return true;
				}
			} else { // Help Menu
				sender.sendMessage(
						(new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("== ����˵� ==").toString());
				sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("/sda create <�������> <�һ�����>")
						.append(ChatColor.GRAY).append(" - ").append(ChatColor.LIGHT_PURPLE).append("���ɶһ���")
						.toString());
				sender.sendMessage(
						(new StringBuilder()).append(ChatColor.GOLD).append("/sda delete <�������>").append(ChatColor.GRAY)
								.append(" - ").append(ChatColor.LIGHT_PURPLE).append("ɾ����ҵ� ���� �һ��룡").toString());
				sender.sendMessage(
						(new StringBuilder()).append(ChatColor.GOLD).append("/sda list").append(ChatColor.GRAY)
								.append(" - ").append(ChatColor.LIGHT_PURPLE).append("�鿴�һ����б�����Ϣ").toString());
				sender.sendMessage(
						new StringBuilder().append(ChatColor.GOLD).append("/sda about").append(ChatColor.GRAY)
								.append(" - ").append(ChatColor.LIGHT_PURPLE).append("�鿴�����Ϣ").toString());
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
				(new StringBuilder()).append(ChatColor.BLUE).append(ChatColor.ITALIC).append("Ϊ�����������Ķһ����").toString());
		sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("������ ")
				.append(ChatColor.RESET).append(ChatColor.AQUA).append("Mars (ISOTOPE Studio)").toString());
		sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("��ַ�� ")
				.append(ChatColor.RESET).append(ChatColor.AQUA).append("http://isotopestudio.cc").toString());
	}
}
