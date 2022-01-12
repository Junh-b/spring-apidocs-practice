package net.junhabaek.springapidocs.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<HomeMessage> home(){
        HomeMessage homeMessage = HomeMessage.createHomeMessage("home");
        return ResponseEntity.ok(homeMessage);
    }

    @Data
    public static class HomeMessage{
        private String message;

        public static HomeMessage createHomeMessage(String message){
            HomeMessage homeMessage = new HomeMessage();
            homeMessage.setMessage(message);
            return homeMessage;
        }
    }
}
