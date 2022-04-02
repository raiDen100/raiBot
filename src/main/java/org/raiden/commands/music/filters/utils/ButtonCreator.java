package org.raiden.commands.music.filters.utils;

import net.dv8tion.jda.api.interactions.components.Button;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.TrackScheduler;

import java.util.ArrayList;
import java.util.List;

public class ButtonCreator {
    public static List<Button> createNowPlayingButtons(TrackScheduler scheduler){
        List<Button> buttonList = new ArrayList<>();

        buttonList.add(Button.secondary("r-queue", "Queue"));
        buttonList.add(Button.secondary("r-skip", "Skip"));
        buttonList.add(Button.secondary("r-resume", "Play"));
        if(!scheduler.player.isPaused())
            buttonList.add(Button.secondary("r-pause", "Pause"));
        else
            buttonList.add(Button.danger("r-pause", "Pause"));

        if(!scheduler.repeating)
            buttonList.add(Button.secondary("r-loop", "Loop"));
        else
            buttonList.add(Button.danger("r-loop", "Loop"));

        return buttonList;
    }
}
