package commands.ServerCommands;


import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;


import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class UserInfo extends ServerCommands {
    public UserInfo() {
        super.name = "userinfo";
        super.help = "Дает информацию о пользователе канала!";
        super.arguments = "[nickname]";
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");



    @Override
    protected void execute(CommandEvent event) {


        String userName = event.getArgs();


        //копирование всех пользователей канала в новый массив (members)
        String [] members = new String [event.getGuild().getMembers().size()];
        for (int i = 0; i < event.getGuild().getMembers().size(); i++) {
            members[i] = event.getGuild().getMembers().get(i).getAsMention();
        }
        //проверка на наличие введенного пользователя (userName) в массиве (members)
        boolean isMember = false;
        for (String member : members) {
            if (member.equals(userName)) {
                isMember = true;
                break;
            }
        }



        if (userName.equals(" ")) {
            event.reply("Что бы получить информацию о пользователи введите следующую команду:\n :exclamation:**/userinfo [имя]**");

        } else if (isMember) {
            Member name = event.getMessage().getMentionedMembers().get(0);



            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(Color.cyan);
            embedBuilder.setTitle("Информация об пользователе " + userName)
                    .setThumbnail(name.getUser().getAvatarUrl())
                    .addField("Name",name.getAsMention(), true)
                    .addField("Аккаунт создан", name.getTimeCreated().format(dateTimeFormatter), true)
                    .addField("Присоединился на сервер", name.getTimeJoined().format(dateTimeFormatter),true)
                    .addField("Roles", getRolesAsString(name.getRoles(), name.getAsMention()), true)
                    .setTitle(" " + name.getOnlineStatus());


            event.reply(embedBuilder.build());


        } else if (!isMember) {
            event.reply(":x: Пользователь под ником: **" + userName + "** не найден");
        }
    }


    private String getRolesAsString (List<Role> roleList, String userName){
        String roles;

        if(!roleList.isEmpty()) {
            Role tempRole = roleList.get(0);
            roles = tempRole.getName();
            for (int i = 1; i < roleList.size(); i++) {
                tempRole = (Role) roleList.get(i);
                roles = roles + ", " + tempRole.getName();
            }

        }else {
            roles = "У " + userName + " пока что нету ролей на этом сервере";
        }

        return roles;
    }

}
