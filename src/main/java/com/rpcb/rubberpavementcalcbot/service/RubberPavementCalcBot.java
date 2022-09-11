package com.rpcb.rubberpavementcalcbot.service;

import com.rpcb.rubberpavementcalcbot.config.BotConfig;
<<<<<<< HEAD
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

=======
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
>>>>>>> 8d4367b (initial commit)
@Component
public class RubberPavementCalcBot extends TelegramLongPollingBot {

    final BotConfig config;

    public RubberPavementCalcBot(BotConfig config) {
        this.config = config;
<<<<<<< HEAD
=======
        List<BotCommand> listofCommands = new ArrayList<>();//пишем списко команд для меню
        listofCommands.add(new BotCommand("/price", "Прайс листы"));
        listofCommands.add(new BotCommand("/calcPavment", "Расчет покрытий"));
        listofCommands.add(new BotCommand("/calendar", "Список дел"));
        listofCommands.add(new BotCommand("/price update", "Редктирование прайс листов"));
        listofCommands.add(new BotCommand("/help", "Описание функционала"));
        listofCommands.add(new BotCommand("/calcValut", "Калькулятор валют"));
        try{
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){
            log.error("Error setting bot`s command list: " + e.getMessage());
        }
>>>>>>> 8d4367b (initial commit)
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
<<<<<<< HEAD
=======
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
>>>>>>> 8d4367b (initial commit)
                default: sendMessage(chatId, "Sorry, command was not recognized.");
            }
        }
    }

    private void startCommandReceived(long chatId, String nameFirst){

        String answer = "Привет " + nameFirst+ "! Тебя приветствует помощник! Я буду за тебя считать то, что тебе посчитать самостоятельно лень.";

<<<<<<< HEAD
=======
        log.info("Raplied to user " + nameFirst);
>>>>>>> 8d4367b (initial commit)
        sendMessage(chatId, answer);

    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        } catch (TelegramApiException e){
<<<<<<< HEAD

=======
            log.error("Error occurred: " + e.getMessage());
>>>>>>> 8d4367b (initial commit)
        }

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }
<<<<<<< HEAD
}
=======

    static final String HELP_TEXT = "Тут будет пояснительная записка для тех кому лень потыкать кнопочки\n\n"+
            " пункт /price отправит Вас посмотреть текущие цены на материал\n\n"+
            " пункт /calcPavment перенесет в калькулятор покрытий\n\n"+
            " и т.д.";
}
>>>>>>> 8d4367b (initial commit)
