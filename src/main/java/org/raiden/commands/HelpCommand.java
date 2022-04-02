package org.raiden.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import org.raiden.commands.utils.CommandContext;
import org.raiden.commands.utils.ICommand;

import java.util.List;

public class HelpCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        EmbedBuilder helpEmbed = new EmbedBuilder();
        List<ICommand> commands = ctx.getCommands();
        for (ICommand command : commands) {
            helpEmbed.addField(command.getName() + command.getAliases().toString(), command.getHelp(), false);
        }

        ctx.sendEventReply(helpEmbed.build());
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public List<String> getAliases() {
        return ICommand.super.getAliases();
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
