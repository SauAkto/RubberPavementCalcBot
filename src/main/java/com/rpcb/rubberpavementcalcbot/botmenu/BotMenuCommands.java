package com.rpcb.rubberpavementcalcbot.botmenu;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

public class BotMenuCommands {

        public List<BotCommand>  botMenuCommands() {
                List<BotCommand> listofCommands = new ArrayList<>();//пишем списко команд для меню
                listofCommands.add(new BotCommand("/price", "Прайс листы"));
                listofCommands.add(new BotCommand("/calcPavment", "Расчет покрытий"));
                listofCommands.add(new BotCommand("/calendar", "Список дел"));
                listofCommands.add(new BotCommand("/price update", "Редктирование прайс листов"));
                listofCommands.add(new BotCommand("/consumption update", "Редактирование расхода материала"));
                listofCommands.add(new BotCommand("/help", "Описание функционала"));
                listofCommands.add(new BotCommand("/calcValut", "Калькулятор валют"));
                return listofCommands;
        }
}
