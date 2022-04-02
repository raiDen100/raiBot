package org.raiden.commands.music;

import net.dv8tion.jda.api.managers.AudioManager;
import org.raiden.commands.utils.CommandContext;
import org.raiden.commands.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;

import java.util.List;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final AudioManager audioManager = ctx.getGuild().getAudioManager();

        if(!audioManager.isConnected()){
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();
        audioManager.closeAudioConnection();

        ctx.addEventReaction("👋");
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public List<String> getAliases() {
        return List.of();
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
