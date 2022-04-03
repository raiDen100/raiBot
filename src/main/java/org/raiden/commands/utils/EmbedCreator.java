package org.raiden.commands.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.List;

public class EmbedCreator {

    public static MessageEmbed actionSuccessfulEmbed(String description, Color color){
        return new EmbedBuilder()
                .setDescription(description)
                .setColor(color)
                .build();
    }

    public static MessageEmbed actionSuccessfulEmbed(String description){
        return new EmbedBuilder()
                .setDescription(description)
                .build();
    }

    public static MessageEmbed nowPlayingEmbed(AudioTrack track, Long authorId){
        String description = "[" + track.getInfo().title + "](" + track.getInfo().uri + ") [<@" + authorId + ">]";
        return new EmbedBuilder()
                .setTitle("Now playing!")
                .setDescription(description)
                .setColor(Color.magenta)
                .build();
    }

    public static MessageEmbed queuedTrackEmbed(AudioTrack track, Long authorId){

        String description = "[" + track.getInfo().title + "](" + track.getInfo().uri + ") [<@" + authorId + ">]";
        return new EmbedBuilder()
                .setTitle("Queued!")
                .setDescription(description)
                .setColor(new Color(57, 28, 107))
                .build();
    }

    public static MessageEmbed queuedPlaylistEmbed(int playlistSize){

        String description = "Queued **" + playlistSize + "** tracks";
        return new EmbedBuilder()
                .setDescription(description)
                .setColor(new Color(57, 28, 107))
                .build();
    }

    public static MessageEmbed actionFailedEmbed(String description){
        return new EmbedBuilder()
                .setTitle("Error")
                .setDescription(description)
                .setColor(new Color(255, 0, 0))
                .build();
    }

    public static MessageEmbed playbackFailedEmbed(AudioTrack track) {
        String description = "Track " + track.getInfo().title + " failed after 3 attempts";
        return new EmbedBuilder()
                .setTitle("Error")
                .setDescription(description)
                .setColor(new Color(255, 0, 0))
                .build();
    }
}
