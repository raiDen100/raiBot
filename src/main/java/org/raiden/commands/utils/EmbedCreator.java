package org.raiden.commands.utils;

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

    public static MessageEmbed nowPlayingEmbed(String description){
        return new EmbedBuilder()
                .setTitle("Now playsing!")
                .setDescription(description)
                .setColor(new Color(194, 116, 0))
                .build();
    }

    public static MessageEmbed queuedTrackEmbed(String description){
        return new EmbedBuilder()
                .setTitle("Queued!")
                .setDescription(description)
                .setColor(new Color(64, 0, 148))
                .build();
    }

    public static MessageEmbed queuedPlaylistEmbed(String description){
        return new EmbedBuilder()
                .setDescription(description)
                .setColor(new Color(64, 0, 148))
                .build();
    }

    public static MessageEmbed actionFailedEmbed(String description){
        return new EmbedBuilder()
                .setTitle("Error")
                .setDescription(description)
                .setColor(new Color(255, 0, 0))
                .build();
    }

}
