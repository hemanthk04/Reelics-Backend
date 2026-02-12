package com.projects.reelics.controllers;

import com.projects.reelics.dto.TelegramSubmitRequest;
import com.projects.reelics.models.Track;
import com.projects.reelics.services.TelegramInputService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/tel")
public class TelegramController {
    private final TelegramInputService inputService;
    public TelegramController(TelegramInputService inputService){
        this.inputService = inputService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submit(@Valid @RequestBody TelegramSubmitRequest request){
        Track track = inputService.submitInstagramURL(request.getInstagramURL());
        return ResponseEntity.ok(
                Map.of(
                        "status", "ACCEPTED",
                        "trackId", track.getId(),
                        "message", "Instagram Link accepted, Queued for the Next level of Processing"
                )
        );
    }
}
