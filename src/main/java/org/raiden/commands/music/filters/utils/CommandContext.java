package org.raiden.commands.music.filters.utils;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.List;

public class CommandContext implements ICommandContext{

    private GuildMessageReceivedEvent event;
    private ButtonClickEvent buttonEvent;
    private SlashCommandEvent slashCommandEvent;
    private final List<String> args;
    private final List<ICommand> commands;

    public CommandContext(@Nullable GuildMessageReceivedEvent event, @Nullable List<String> args, @Nullable List<ICommand> commands) {
        this.event = event;
        this.args = args;
        this.commands = commands;
    }

    public CommandContext(@Nullable ButtonClickEvent buttonClickEvent, @Nullable List<String> args, @Nullable List<ICommand> commands) {
        this.buttonEvent = buttonClickEvent;
        this.args = args;
        this.commands = commands;
    }
    public CommandContext(@Nullable SlashCommandEvent slashCommandEvent, @Nullable List<String> args, @Nullable List<ICommand> commands) {
        this.slashCommandEvent = slashCommandEvent;
        this.args = args;
        this.commands = commands;
    }

    @Override
    public GuildMessageReceivedEvent getEvent(){
        return this.event;
    }
    public ButtonClickEvent getButtonEvent(){
        return this.buttonEvent;
    }
    public SlashCommandEvent getSlashEvent(){
        return this.slashCommandEvent;
    }

    public List<ICommand> getCommands(){
        return this.commands;
    }

    @Override
    public Member getMember() {

        if(this.getButtonEvent() != null){
            return this.getButtonEvent().getMember();
        }
        if(this.getSlashEvent() != null){
            return this.slashCommandEvent.getMember();
        }
        return ICommandContext.super.getMember();
    }

    @Override
    public Guild getGuild() {
        if(this.getButtonEvent() != null){
            return this.getButtonEvent().getGuild();
        }
        if(this.getSlashEvent() != null){
            return this.slashCommandEvent.getGuild();
        }
        return this.getEvent().getGuild();
    }

    @Override
    public TextChannel getChannel() {
        if(this.getButtonEvent() != null){
            return this.getButtonEvent().getTextChannel();
        }
        if(this.getSlashEvent() != null){
            return this.slashCommandEvent.getTextChannel();
        }
        return ICommandContext.super.getChannel();
    }

    @Override
    public Member getSelfMember() {
        if(this.getButtonEvent() != null){
            return this.getButtonEvent().getGuild().getSelfMember();
        }
        if(this.getSlashEvent() != null){
            return this.slashCommandEvent.getGuild().getSelfMember();
        }
        return ICommandContext.super.getSelfMember();
    }

    @Override
    public User getAuthor() {
        if(this.getButtonEvent() != null){
            return this.getButtonEvent().getUser();
        }
        if(this.getSlashEvent() != null){
            return this.slashCommandEvent.getUser();
        }
        return ICommandContext.super.getAuthor();
    }

    public void sendEventReply(Message message){
        if(this.getButtonEvent() != null){
            this.buttonEvent.editMessage(message).queue();
            return;
        }
        if(this.getSlashEvent() != null){
            this.slashCommandEvent.reply(message).queue();
            return;
        }
        this.event.getChannel().sendMessage(message).queue();
        return;
    }

    public void sendEventReply(String message){
        if(this.getButtonEvent() != null){
            this.buttonEvent.editMessage(message).queue();
            return;
        }
        if(this.getSlashEvent() != null){
            this.slashCommandEvent.reply(message).queue();
            return;
        }
        this.event.getChannel().sendMessage(message).queue();
        return;
    }

    public void sendEventReply(MessageEmbed embed){
        if(this.getButtonEvent() != null){
            this.buttonEvent.editMessageEmbeds(embed).queue();
            return;
        }
        if(this.getSlashEvent() != null){
            this.slashCommandEvent.replyEmbeds(embed).queue();
            return;
        }
        this.event.getChannel().sendMessageEmbeds(embed).queue();
        return;
    }

    public void addEventReaction(String reaction){
        if(this.getButtonEvent() != null){
            this.buttonEvent.getMessage().addReaction(reaction).queue();
            return;
        }
        if(this.getSlashEvent() != null){
            this.slashCommandEvent.reply(reaction).queue();
            return;
        }
        this.event.getMessage().addReaction(reaction).queue();
        return;
    }

    public List<String> getArgs(){
        return this.args;
    }
}
