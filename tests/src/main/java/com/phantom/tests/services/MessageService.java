package com.phantom.tests.services;


import com.phantom.tests.models.Message;
import com.phantom.tests.models.User;
import com.phantom.tests.repos.MessageRepo;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final MessageRepo messageRepo;

    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public Message findById(Long id) {
        return messageRepo.findById(id).orElseThrow();
    }

    public void addMessage(String text, User user) {
        Message message = new Message(text, user);
        messageRepo.save(message);
    }

    public Iterable<Message> findAll() {
        return messageRepo.findAll();
    }

    public void update(Message message, String text) {
        message.setText(text);
        messageRepo.save(message);
    }

    public void viewMessage(Message message) {
        message.setView(true);
        messageRepo.save(message);
    }
}
