package commands.ServerCommands;



import com.jagrosh.jdautilities.command.CommandEvent;





public class InviteCommand extends ServerCommands {


    public InviteCommand() {
        super.name = "invite";
        super.help = "Создает URL ссылку-приглашение на сервер";
    }

    @Override
    protected void execute(CommandEvent event) {

            event.reply(":white_check_mark: Ссылка былла создана!\n *****");


    }


}
