package commands.ServerCommands;


import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;


public class CreateCategory extends ServerCommands {

    private final EventWaiter waiter;

    public CreateCategory(EventWaiter eventWaiter) {
        super.name = "createct";
        super.help = "Данная команда создает категорию на твоем 'сервере'";
        this.waiter = eventWaiter;

    }

    @Override
    protected void execute(CommandEvent event) {

        event.reply(":exclamation: Введите название категории, которую хотите создать!");

        waiter.waitForEvent(GuildMessageReceivedEvent.class,
                //проверка второго сообщения на: ввел тот же пользовотель, ввел с того же канала
                e -> e.getAuthor().equals(event.getAuthor())
                        && e.getChannel().equals(event.getChannel())
                        && !e.getMessage().equals(event.getMessage()),
                // если условия соблюдены код работает
                e -> {


                    event.getGuild().createCategory(e.getMessage().getContentRaw()).queue();
                    event.reply(":white_check_mark: Категория создана");

                },
                //временные ограничения (30 сек)
                30, TimeUnit.SECONDS, () -> event.reply(":alarm_clock: Время на ввод названия **вашей категории** истек!\nДля повторного вызова команды введите:\n**/createct**")
        );

    }
}
