package org.raiden.commands.utils;

import net.dv8tion.jda.api.interactions.components.Button;
import org.raiden.lavaplayer.GuildMusicManager;
import org.raiden.lavaplayer.TrackScheduler;

import java.util.ArrayList;
import java.util.List;

public class ButtonCreator {
    public static List<Button> createNowPlayingButtons(TrackScheduler scheduler){
        List<Button> buttonList = new ArrayList<>();

        buttonList.add(Button.secondary("queue", "Queue"));
        buttonList.add(Button.secondary("skip", "Skip"));
        buttonList.add(Button.secondary("resume", "Play"));
        if(!scheduler.player.isPaused())
            buttonList.add(Button.secondary("pause", "Pause"));
        else
            buttonList.add(Button.danger("pause", "Pause"));

        if(!scheduler.repeating)
            buttonList.add(Button.secondary("loop", "Loop"));
        else
            buttonList.add(Button.danger("loop", "Loop"));

        return buttonList;
    }
}
