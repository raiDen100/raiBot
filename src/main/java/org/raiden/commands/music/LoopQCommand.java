package org.raiden.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.raiden.commands.music.filters.utils.CommandContext;
import org.raiden.commands.music.filters.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;

import java.util.List;

public class LoopQCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()){
            return;
        }

        if(selfVoiceState.getChannel() != memberVoiceState.getChannel()){
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        final boolean newRepeating = !musicManager.scheduler.repeatingQueue;

        musicManager.scheduler.repeating = false;
        musicManager.scheduler.repeatingQueue = newRepeating;

        EmbedBuilder eb = new EmbedBuilder();
        if(newRepeating){
            eb.setTitle("Looping queue!", null);
        }
        else {
            eb.setTitle("Loop disabled!", null);
        }


        ctx.sendEventReply(eb.build());
    }

    @Override
    public String getName() {
        return "loopq";
    }

    @Override
    public List<String> getAliases() {
        return List.of("rq", "lq");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
