package commands.ServerCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class ServerInfo extends ServerCommands {

    public ServerInfo (){
        super.name = "serverinfo";
        super.help = "Дает информацию о сервере!";
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();

    @Override
    protected void execute(CommandEvent event) {
        String [] members = new String [event.getGuild().getMembers().size()];
        for (int i = 0; i < event.getGuild().getMembers().size(); i++) {
            members[i] = event.getGuild().getMembers().get(i).getAsMention();

        }


        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.cyan);
        embedBuilder.setAuthor(event.getGuild().getName());
        embedBuilder.setThumbnail(event.getGuild().getIconUrl());
        embedBuilder.addField("Владелец сервера:", ":crown: " + Objects.requireNonNull(event.getGuild().getOwner()).getAsMention(), true);
        embedBuilder.addField("Кол-во учасников:",":busts_in_silhouette: " + members.length, true);
        embedBuilder.addField("Канал создан: ", ":calendar: " + event.getGuild().getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),true);
        embedBuilder.addField("Участники канала", Arrays.toString(members),false);
        embedBuilder.setFooter("Запрос был составлен на: " + simpleDateFormat.format(date),event.getGuild().getIconUrl());


        event.reply(embedBuilder.build());




    }
}
