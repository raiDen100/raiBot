package org.raiden.commands.music.filters;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.raiden.commands.music.filters.utils.CommandContext;
import org.raiden.commands.music.filters.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;

import java.awt.*;
import java.util.List;

public class VaporwaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if(ctx.getArgs().isEmpty()){
            channel.sendMessage("You need to provide a number");
            return;
        }

        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        String args = String.join("", ctx.getArgs());

        if(args.contains("reset")){
            guildMusicManager.resetVaporwave();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setDescription("Vaporwave " + " removed!")
                    .setColor(new Color(0, 255, 0));

            ctx.sendEventReply(eb.build());
            return;
        }

        float parsedArg = Float.parseFloat(args);

        if(parsedArg >= 100){
            parsedArg = 100;
        }
        else if(parsedArg < 10){
            parsedArg = 10;
        }

        float bassboostValue = parsedArg / 100;
        guildMusicManager.setVaporwave(bassboostValue);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription("Vaporwave " + " set!")
                .setColor(new Color(0, 255, 0));

        ctx.sendEventReply(eb.build());
    }

    @Override
    public String getName() {
        return "vaporwave";
    }

    @Override
    public List<String> getAliases() {
        return List.of("vw");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
