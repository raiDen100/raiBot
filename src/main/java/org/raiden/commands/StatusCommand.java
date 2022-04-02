package org.raiden.commands;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import org.raiden.commands.music.filters.utils.CommandContext;
import org.raiden.commands.music.filters.utils.ICommand;

import java.util.List;

public class StatusCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        if(ctx.getArgs().isEmpty()){
            channel.sendMessage("You need to provide type and description");
            return;
        }

        List<String> args = ctx.getArgs();

        if(args.get(0).equals("playing")){
            List<String> x = args.subList(1, args.size());
            String desc = String.join(" ", x);
            ctx.getJDA().getPresence().setActivity(Activity.playing(desc));
            return;
        }
        if(args.get(0).equals("watching")){
            List<String> x = args.subList(1, args.size());
            String desc = String.join(" ", x);
            ctx.getJDA().getPresence().setActivity(Activity.watching(desc));
            return;
        }
        if(args.get(0).equals("listening")){
            List<String> x = args.subList(1, args.size());
            String desc = String.join(" ", x);
            ctx.getJDA().getPresence().setActivity(Activity.listening(desc));
            return;
        }
        if(args.get(0).equals("competing")){
            List<String> x = args.subList(1, args.size());
            String desc = String.join(" ", x);
            ctx.getJDA().getPresence().setActivity(Activity.competing(desc));
            return;
        }
        if(args.get(0).equals("streaming")){
            List<String> x = args.subList(2, args.size());
            String desc = String.join(" ", x);
            ctx.getJDA().getPresence().setActivity(Activity.streaming(desc, args.get(1)));
            return;
        }
    }

    @Override
    public String getName() {
        return "status";
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
