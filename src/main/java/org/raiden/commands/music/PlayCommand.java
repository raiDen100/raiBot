package org.raiden.commands.music;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import org.raiden.commands.utils.CommandContext;
import org.raiden.commands.utils.EmbedCreator;
import org.raiden.commands.utils.ICommand;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.PlayerManager;
import org.raiden.utils.SpotifyService;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayCommand implements ICommand {

    private final SpotifyService spotifyService = new SpotifyService();

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if(ctx.getArgs().isEmpty()){
            channel.sendMessage("You need to provide URL");
            return;
        }

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        if(!audioManager.isConnected()){
            if(!memberVoiceState.inVoiceChannel()){
                channel.sendMessage("You must be in a voice channel!").queue();
                return;
            }

            final VoiceChannel memberVoiceChannel = memberVoiceState.getChannel();

            audioManager.openAudioConnection(memberVoiceChannel);
        }

        String link = String.join(" ", ctx.getArgs());

        if (isSpotifyUrl(link)){
            List<String> splitted = Arrays.asList(link.split("/"));
            Collections.reverse(splitted);
            String id = splitted.get(0).split("\\?")[0];
            String type = splitted.get(1);
            if (type.equals("track")){
                Track track = spotifyService.getTrackDetailsById(id);
                link = track.getName() + " " + getTrackArtists(track);
            }
            else if (type.equals("playlist")){
                List<Track> tracks = spotifyService.getPlaylistTracks(id);

                MessageEmbed messageEmbed = EmbedCreator.queuedPlaylistEmbed(tracks.size());
                ctx.sendEventReply(messageEmbed);

                final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
                musicManager.scheduler.setTextChannel(ctx.getChannel());

                int i = 0;
                for(Track track : tracks){
                    String search = "ytsearch:" + track.getName() + " " + getTrackArtists(track);
                    int finalI = i;
                    new Thread(){
                        @Override
                        public void run() {
                            PlayerManager.getInstance().loadAndPlayFromSpotify(ctx, search, ctx.getAuthor(), finalI);
                        }
                    }.start();
                    //PlayerManager.getInstance().loadAndPlayFromSpotify(ctx, search, ctx.getAuthor());
                    i++;
                };
                return;
            }
            else if (type.equals("album")) {
                List<TrackSimplified> tracks = spotifyService.getAlbumTracks(id);

                MessageEmbed messageEmbed = EmbedCreator.queuedPlaylistEmbed(tracks.size());
                ctx.sendEventReply(messageEmbed);

                final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
                musicManager.scheduler.setTextChannel(ctx.getChannel());

                int i = 0;
                for (TrackSimplified track : tracks) {
                    String search = "ytsearch:" + track.getName() + " " + getTrackArtists(track);
                    int finalI = i;
                    new Thread() {
                        @Override
                        public void run() {
                            PlayerManager.getInstance().loadAndPlayFromSpotify(ctx, search, ctx.getAuthor(), finalI);
                        }
                    }.start();
                    //PlayerManager.getInstance().loadAndPlayFromSpotify(ctx, search, ctx.getAuthor());
                    i++;
                }
                return;
            }

            System.out.println(id);
        }
        if(!isValid(link)){
            link = "ytsearch:" + link;
        }
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        musicManager.scheduler.setTextChannel(ctx.getChannel());

        PlayerManager.getInstance().loadAndPlay(ctx, link, ctx.getAuthor());
    }

    public static boolean isSpotifyUrl(String url){
        return url.contains("https://open.spotify.com/");
    }

    public String getTrackArtists(Track track){
        ArtistSimplified[] artists = track.getArtists();
        String result = "";
        for (int i = 0; i < artists.length && i < 3; i++) {
            result += artists[i].getName() + " ";
        }
        return result.trim();
    }

    public String getTrackArtists(TrackSimplified track){
        ArtistSimplified[] artists = track.getArtists();
        String result = "";
        for (int i = 0; i < artists.length && i < 3; i++) {
            result += artists[i].getName() + " ";
        }
        return result.trim();
    }

    public static boolean isValid(String url)
    {
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public List<String> getAliases() {
        return List.of("p");
    }

    @Override
    public String getHelp() {
        return "Plays a song";
    }

}
