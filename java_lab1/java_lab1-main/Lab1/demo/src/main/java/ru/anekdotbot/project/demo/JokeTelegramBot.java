package ru.anekdotbot.project.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;
import ru.anekdotbot.project.demo.config.BotConfig;
import ru.anekdotbot.project.demo.model.JokeData;
import ru.anekdotbot.project.demo.repository.JokeRepository;

@Slf4j
@Component
public class JokeTelegramBot extends TelegramLongPollingBot{
    
    @Autowired
    private JokeRepository jokeRepository;

    final BotConfig config;

    public JokeTelegramBot(BotConfig config){
        this.config = config;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String memberName = update.getMessage().getFrom().getFirstName();

            switch (messageText){
                case "/start":
                    startBot(chatId, memberName);
                    break;
                case "/jokes":
                    sendAllJokes(chatId);
                default: log.info("Unexpected message");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken(){
        return config.getToken();
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Я анекдотический чат-бот. Команды:\n/jokes-выводит все анекдоты");

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private void sendAllJokes(long chatId){
        StringBuilder messageText = new StringBuilder();
        for(JokeData joke : jokeRepository.findAll()){
            messageText.append(joke.getBody()).append("\n");
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText.toString());

        try {
            execute(message);
            log.info("Joke sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }
    
}
