package org.raiden.commands.music.filters;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.raiden.commands.music.filters.utils.CommandContext;
import org.raiden.commands.music.filters.utils.ICommand;
import org.raiden.lavaplayer.PlayerManager;

import java.awt.*;
import java.util.List;

public class VolumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if(ctx.getArgs().isEmpty()){
            channel.sendMessage("You need to provide a number");
            return;
        }
        String args = String.join("", ctx.getArgs());
        if(args.contains("reset")){
            PlayerManager.getInstance().getMusicManager(ctx.getGuild()).resetVolume();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setDescription("Volume " + " removed!")
                    .setColor(new Color(0, 255, 0));

            ctx.sendEventReply(eb.build());
            return;
        }

        float volume = Float.parseFloat(String.join(" ", ctx.getArgs()));
        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).setVolume(volume/100);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription("Volume " + volume + "% set!")
                .setColor(new Color(0, 255, 0));

        ctx.sendEventReply(eb.build());
    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public List<String> getAliases() {
        return List.of("er", "earrape");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
