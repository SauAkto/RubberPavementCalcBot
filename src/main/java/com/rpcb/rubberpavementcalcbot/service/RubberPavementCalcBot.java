package com.rpcb.rubberpavementcalcbot.service;

import com.rpcb.rubberpavementcalcbot.config.BotConfig;

import com.rpcb.rubberpavementcalcbot.model.User;
import com.rpcb.rubberpavementcalcbot.model.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RubberPavementCalcBot extends TelegramLongPollingBot {

    final BotConfig config;

    @Autowired
    private UserRepository userRepository;

    public RubberPavementCalcBot(BotConfig config) {
        this.config = config;

        List<BotCommand> listofCommands = new ArrayList<>();//пишем списко команд для меню
        listofCommands.add(new BotCommand("/price", "Прайс листы"));
        listofCommands.add(new BotCommand("/calcPavment", "Расчет покрытий"));
        listofCommands.add(new BotCommand("/calendar", "Список дел"));
        listofCommands.add(new BotCommand("/price update", "Редктирование прайс листов"));
        listofCommands.add(new BotCommand("/consumption update", "Редактирование расхода материала"));
        listofCommands.add(new BotCommand("/help", "Описание функционала"));
        listofCommands.add(new BotCommand("/calcValut", "Калькулятор валют"));
        try{
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){
            log.error("Error setting bot`s command list: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                    registeredUser(update.getMessage(), update.getMessage().getChat().getFirstName());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default: sendMessage(chatId, "Sorry, command was not recognized.");
            }
        }
    }

    private void registeredUser(Message msg, String userName) {

        if(userRepository.findById(msg.getChatId()).isEmpty()){

            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();

            user.setChatId(chatId);
            user.setUserName(chat.getUserName());
            user.setTimeregstered(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("user saved: " + userName);
        }

    }

    private void startCommandReceived(long chatId, String nameFirst){

        String answer = EmojiParser.parseToUnicode("Привет " + nameFirst+ "! Тебя приветствует помощник!"+ ":sunglasses:" +" Я буду за тебя считать то, что тебе посчитать самостоятельно лень.");

      //  String answer = "Привет " + nameFirst+ "! Тебя приветствует помощник! Я буду за тебя считать то, что тебе посчитать самостоятельно лень.";

        log.info("Raplied to user " + nameFirst);
        sendMessage(chatId, answer);

    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        // создание клавиатуры
        keyboardMarkupMetod(chatId);

        try{
            execute(message);
        } catch (TelegramApiException e){

            log.error("Error occurred: " + e.getMessage());
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



    static final String HELP_TEXT = "Тут будет пояснительная записка для тех кому лень потыкать кнопочки\n\n"+
            " пункт /price отправит Вас посмотреть текущие цены на материал\n\n"+
            " пункт /calcPavment перенесет в калькулятор покрытий\n\n"+
            " и т.д.";

    public void keyboardMarkupMetod(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("стандартное покрытие");
        row.add("каучуковое с ЭПДМ");
        row.add("спрей покрытие");

        keyboardRows.add(row);//дабавление в список первой линейки кнопок

        KeyboardRow row1 = new KeyboardRow();

        row1.add("стандартное сэндвич");
        row1.add("сендвич ЭПДМ");
        row1.add("каменное покрытие");

        keyboardRows.add(row1);//дабавление в список второй линейки кнопок

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);
    }
}

