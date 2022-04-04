package org.raiden.commands.utils;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.raiden.commands.HelpCommand;
import org.raiden.commands.StatusCommand;
import org.raiden.commands.music.*;
import org.raiden.commands.music.filters.*;
import org.raiden.utils.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

    public CommandManager() {
        addCommand(new JoinCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new SkipCommand());
        addCommand(new VolumeCommand());
        addCommand(new BassboostCommand());
        addCommand(new ResetCommand());
        addCommand(new LoopCommand());
        addCommand(new LoopQCommand());
        addCommand(new ShuffleCommand());
        addCommand(new QueueCommand());
        addCommand(new HelpCommand());
        addCommand(new LeaveCommand());
        addCommand(new PauseCommand());
        addCommand(new ResumeCommand());
        addCommand(new SeekCommand());
        addCommand(new CounterCommand());
        addCommand(new SetSpeedCommand());
        addCommand(new NightcoreCommand());
        addCommand(new VaporwaveCommand());
        addCommand(new StatusCommand());
    }
    private void addCommand(ICommand cmd){
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if(nameFound){
            throw new IllegalArgumentException("Command with this name already exists");
        }
        commands.add(cmd);
    }

    @Nullable
    private ICommand getCommand(String search){
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if(cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)){
                return cmd;
            }
        }
        return null;
    }

    public void handle(GuildMessageReceivedEvent event){
        PropertiesReader propertiesReader = new PropertiesReader();
        String prefix = propertiesReader.getPropertyValue("bot.prefix");

        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if(cmd != null){
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args, this.commands);

            cmd.handle(ctx);
        }


    }

    public void handle(SlashCommandEvent event){

        String name = event.getName();


        ICommand cmd = this.getCommand(name);

        if(cmd != null){
            List<String> args = Arrays.asList();
            if(!event.getOptions().isEmpty()){
                args = Arrays.asList(event.getOptions().get(0).getAsString());
            }


            CommandContext ctx = new CommandContext(event, args, this.commands);
            cmd.handle(ctx);
        }


    }


    public void handle(ButtonClickEvent event){
        PropertiesReader propertiesReader = new PropertiesReader();
        String prefix = propertiesReader.getPropertyValue("bot.prefix");
        String[] split = event.getButton().getId()
                .split(" ");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if(cmd != null){
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args, this.commands);
            cmd.handle(ctx);
        }
    }


}
