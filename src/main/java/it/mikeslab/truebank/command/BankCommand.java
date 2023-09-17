package it.mikeslab.truebank.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.util.color.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("bank")
public class BankCommand extends BaseCommand {

    private final TrueBank instance;

    public BankCommand(TrueBank instance) {
        this.instance = instance;
    }

    @Default
    public void onDefault(CommandSender sender) {

        String pluginRunningVersion = instance.getDescription().getVersion();
        sender.sendMessage(ChatColor.color("&#FF9600TrueBank v" + pluginRunningVersion + " made by &#FFB400&lMikesLab&#FF9600."));

    }


    @HelpCommand
    @Subcommand("help")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }


}
