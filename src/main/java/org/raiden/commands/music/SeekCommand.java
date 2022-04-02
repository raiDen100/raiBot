package org.raiden.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.raiden.commands.utils.CommandContext;
import org.raiden.commands.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;

import java.util.List;

public class SeekCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if(ctx.getArgs().isEmpty()){
            channel.sendMessage("You need to provide a number");
            return;
        }

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(selfVoiceState.getChannel() != memberVoiceState.getChannel()){
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            return;
        }
        audioPlayer.getPlayingTrack().setPosition( Long.parseLong(ctx.getArgs().get(0)) * 1000);

        ctx.addEventReaction("⏩");
    }

    @Override
    public String getName() {
        return "seek";
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
