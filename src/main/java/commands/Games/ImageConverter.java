package commands.Games;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.coobird.thumbnailator.Thumbnails;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;


import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ImageConverter extends GameCommand {

    private final EventWaiter waiter;

    public ImageConverter(EventWaiter eventWaiter) {
        super.name = "imgcon";
        super.help = "Конвертирует размер и угол поворота изображения";
        this.waiter = eventWaiter;
    }

    @Override
    protected void execute(CommandEvent event) {

        event.reply(":exclamation: Для конвертирования изображения введите (без скобок):") ;
        event.reply(" `[длина]`, `[высота]`, `[ссылка на изображение]`, `[угол поворота (если необходимо)]` ");

        waiter.waitForEvent(GuildMessageReceivedEvent.class,
                //проверка второго сообщения на: ввел тот же пользовотель, ввел с того же канала
                e -> e.getAuthor().equals(event.getAuthor())
                        && e.getChannel().equals(event.getChannel())
                        && !e.getMessage().equals(event.getMessage()),
                // если условия соблюдены код работает
                e -> {


                    try {
                        String [] args = e.getMessage().getContentRaw().split(" ");
                        int width = Integer.parseInt(args[0]);
                        int height = Integer.parseInt(args[1]);
                        int rotateAmount = 0;
                        if (args.length == 4){
                            rotateAmount = Integer.parseInt(args[3]);
                        }
                        URL url = new URL(args[2]);

                        //Thumbnails API's
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        Thumbnails.of(url).forceSize(width, height).rotate(rotateAmount).outputFormat("png").toOutputStream(outputStream);
                        byte[] imgInByte = outputStream.toByteArray();

                        event.reply(":white_check_mark: " + event.getAuthor().getAsMention() + ", дело сделано\n Твое изображение получилось " + width +  "x" + height + " пикселей, " + "угол поворота " + rotateAmount + "°");
                        event.getChannel().sendFile(imgInByte, "converted Image.png").queue();

                    } catch (Exception ex) {
                        event.reply(":x: Параметры были введены неверно");
                        event.reply("**Введите команду `/imgcon` еще раз** ");
                    }



                },
                //временные ограничения (30 сек)
                60, TimeUnit.SECONDS, () -> event.reply(":alarm_clock: Время на ввод параметров истек! Для повторного вызова команды введите:\n**/imgcon**")
        );



    }
}
