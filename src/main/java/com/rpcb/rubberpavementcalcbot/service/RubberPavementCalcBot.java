package com.rpcb.rubberpavementcalcbot.service;

import com.rpcb.rubberpavementcalcbot.botmenu.BotMenuCommands;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.rpcb.rubberpavementcalcbot.constant.VarConst.*;

@Slf4j
@Component
public class RubberPavementCalcBot extends TelegramLongPollingBot {

    final BotConfig config;

    @Autowired
    private UserRepository userRepository;

    public RubberPavementCalcBot(BotConfig config) {
        this.config = config;

        BotMenuCommands botMenu = new BotMenuCommands();
        try{
            //добавлем сови пункты в меню
            this.execute(new SetMyCommands(botMenu.botMenuCommands(), new BotCommandScopeDefault(), null));
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

                case "/price":
                    price(chatId);
                    break;

                case "/calcPavment":
                    calcPavment(chatId);
                    break;

                case "/calendar":
                    break;

                case "/price update":
                    break;

                case "/consumption update":
                    break;

                case "/calcValut":
                    break;

                default: sendMessage(chatId, "Sorry, command was not recognized.");
            }
        }else if(update.hasCallbackQuery()){
            String callbackMethod = update.getCallbackQuery().getData();
            if(callbackMethod.equals(BINDER) || callbackMethod.equals(EPDM) || callbackMethod.equals(CSBR) ){
                callbackPrice(update);
            }else if(callbackMethod.equals(COVER_STANDART)
                    || callbackMethod.equals(COVER_SENDVICH_STANDART)
                    || callbackMethod.equals(COVER_EPDM)
                    || callbackMethod.equals(COVER_SENDVICH_EPDM)
                    || callbackMethod.equals(COVER_SPRAY)
                    || callbackMethod.equals(COVER_TERRA)){
                calcCover(update);
            }
        }
    }

    private void calcCover(Update update) {

        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        SendMessage message = new SendMessage();
        message.setText("Set message:");
        message.setChatId(String.valueOf(chatId));

        executeMet(message);

    }

    private void calcPavment(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выбери тип покрытия для расчета!");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        var buttonStandart = new InlineKeyboardButton();
        buttonStandart.setText("Стандартное покрытие");
        buttonStandart.setCallbackData(COVER_STANDART);

        var buttonSendvichSrandart = new InlineKeyboardButton();
        buttonSendvichSrandart.setText("Сендвич стандарт");
        buttonSendvichSrandart.setCallbackData(COVER_SENDVICH_STANDART);

        row.add(buttonStandart);
        row.add(buttonSendvichSrandart);
        rowsInline.add(row); //первая линия кнопок

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        var buttonEPDM = new InlineKeyboardButton();
        buttonEPDM.setText("Покрытие EPDM");
        buttonEPDM.setCallbackData(COVER_EPDM);

        var buttonSendvichEPDM = new InlineKeyboardButton();
        buttonSendvichEPDM.setText("Сендвич EPDM");
        buttonSendvichEPDM.setCallbackData(COVER_SENDVICH_EPDM);

        row2.add(buttonEPDM);
        row2.add(buttonSendvichEPDM);
        rowsInline.add(row2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();

        var buttonSpray = new InlineKeyboardButton();
        buttonSpray.setText("Спрей покрытие");
        buttonSpray.setCallbackData(COVER_SPRAY);

        var buttonTerra = new InlineKeyboardButton();
        buttonTerra.setText("Каменное покрытие");
        buttonTerra.setCallbackData(COVER_TERRA);

        row3.add(buttonSpray);
        row3.add(buttonTerra);
        rowsInline.add(row3);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        executeMet(message);
    }

    private void callbackPrice(Update update) {

        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage message = new SendMessage();

        String text = "";

        if(callbackData.equals(BINDER)){
            text = "Прайс лист, связующее:";
            message.setChatId(String.valueOf(chatId));
            message.setText(text);

        }else if(callbackData.equals(EPDM)){
            text = "Прайс лист, EPDM:";
            message.setChatId(String.valueOf(chatId));
            message.setText(text);

        }else{
            text = "Прайс лист, CSBR:";
            message.setChatId(String.valueOf(chatId));
            message.setText(text);
        }
        executeMet(message);
    }

    private void price(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Какой прайс тебе показать?");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        var buttonBinder = new InlineKeyboardButton();
        buttonBinder.setText("Связующее");
        buttonBinder.setCallbackData(BINDER);

        var buttonEPDM = new InlineKeyboardButton();
        buttonEPDM.setText("EPDM");
        buttonEPDM.setCallbackData(EPDM);

        var buttonCSBR = new InlineKeyboardButton();
        buttonCSBR.setText("colorSBR");
        buttonCSBR.setCallbackData(CSBR);

        row.add(buttonBinder);
        row.add(buttonEPDM);
        row.add(buttonCSBR);

        rowsInline.add(row);

        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

        executeMet(sendMessage);
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

           log.info("Raplied to user " + nameFirst);
        sendMessage(chatId, answer);

    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        // создание клавиатуры
        keyboardMarkupMetod(chatId);

        executeMet(message);
    }

    public void keyboardMarkupMetod(long chatId){ //экранная клавиатура
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); //подгоняем размер
        keyboardMarkup.setOneTimeKeyboard(false); // скрываем после использования
        keyboardMarkup.setSelective(false);

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

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    public void executeMet(SendMessage message){
        try{
            execute(message);
        } catch (TelegramApiException e){

            log.error("Error occurred: " + e.getMessage());
        }
    }
}


