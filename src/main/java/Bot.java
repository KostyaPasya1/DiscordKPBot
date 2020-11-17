
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import commands.Games.*;
import commands.Music.*;
import commands.ServerCommands.CreateCategory;
import commands.ServerCommands.InviteCommand;
import commands.ServerCommands.ServerInfo;
import commands.ServerCommands.UserInfo;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

import java.util.concurrent.TimeUnit;


public class Bot extends ListenerAdapter {


    private Bot() throws LoginException {


        final JDA jda = new JDABuilder(AccountType.BOT)
                .setToken("NzcyNzUwMDI4MzY3OTg2Njg5.X5_Niw.OPSjD-Ch_st4I-c_CpNWz3tIkPQ").build();


        EventWaiter waiter = new EventWaiter();

        CommandClientBuilder commandClientBuilder = new CommandClientBuilder()
                .setPrefix("/")
                .setOwnerId("772750028367986689")
                .setHelpWord("help")
                .addCommand(new ServerInfo())
                .addCommand(new UserInfo())
                .addCommand(new InviteCommand())
                .addCommand(new CreateCategory(waiter))
                .addCommand(new LolCommand())
                .addCommand(new MemeCommand())
                .addCommand(new ImageConverter(waiter))
                .addCommand(new CurrencyCommand())
                .addCommand(new RandomizerCommand())
                .addCommand(new PlayMusicCommand())
                .addCommand(new LeaveCommand())
                .addCommand(new NextCommand())
                .addCommand(new QueueCommand())
                .addCommand(new StopCommand())
                .addCommand(new ResumeCommand());


        CommandClient client = commandClientBuilder.build();

        jda.addEventListener(client);
        jda.addEventListener(waiter);


    }



    public static void main(String[] args) throws LoginException {

        new Bot();

        long enable = System.currentTimeMillis();
        System.out.println("Bot enable in " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - enable) + "s!");



    }



}



