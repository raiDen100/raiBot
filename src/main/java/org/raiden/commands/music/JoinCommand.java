package org.raiden.commands.music;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.raiden.commands.utils.CommandContext;
import org.raiden.commands.utils.EmbedCreator;
import org.raiden.commands.utils.ICommand;

import java.awt.*;
import java.util.List;

public class JoinCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();



        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()){
            String description = "You must be in a voice channel!";
            ctx.sendEventReply(description);
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberVoiceChannel = memberVoiceState.getChannel();

        if(audioManager.isConnected()){
            return;
        }

        audioManager.openAudioConnection(memberVoiceChannel);

        String description = "Joined the voice channel!";

        ctx.sendEventReply(EmbedCreator.actionSuccessfulEmbed(description));
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public List<String> getAliases() {
        return ICommand.super.getAliases();
    }

    @Override
    public String getHelp(){
        return "Join voice channel";
    }
}
