package commands.Games;

import com.jagrosh.jdautilities.command.CommandEvent;



public class RandomizerCommand extends GameCommand {


    public RandomizerCommand() {
        super.name = "random";
        super.help = "Генирирует рандомное число";
        super.arguments = "[от] [до] (можно вводить отрицательные числа)";
    }

    @Override
    protected void execute(CommandEvent event) {

        String[] args = event.getArgs().split(" ");
        int a = 1; // Начальное значение диапазона - "от"
        int b = 2; // Конечное значение диапазона - "до"

        if (args.length == 0) {
            event.reply(":x:" + event.getAuthor().getAsMention() + ", введите аргументы правильно, например: `/random 1 100`");
        } else {
            a = Integer.parseInt(args[0]);
            b = Integer.parseInt(args[1]);

            if (a > b) {
                event.reply(":x: `Конечное значение диапазона должно быть больше Начального значения диапазона`");
            } else {
                int randomNumber = a + (int)(Math.random() * (b - a + 1));
                event.reply(randomNumber + ", " + event.getAuthor().getAsMention());
            }

        }

        }





}
