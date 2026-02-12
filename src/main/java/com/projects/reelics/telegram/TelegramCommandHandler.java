package com.projects.reelics.telegram;

import com.projects.reelics.models.Track;
import com.projects.reelics.services.TrackQueryService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class TelegramCommandHandler {
     private final TrackQueryService queryService;

     public TelegramCommandHandler(TrackQueryService queryService){
         this.queryService = queryService;
     }

     public String handleStatus(UUID trackId){
         Track track = queryService.findById(trackId).orElseThrow(()-> new IllegalArgumentException("Track Not Found"));

         return """
                 › Track Status
                 › %s
                 › Status: %s
                 """.formatted(track.getId(), track.getStatus());
     }

     public String handleMyTracks(){
         List<Track> tracks = queryService.findRecent(10);
         if(tracks.isEmpty()){
             return "No Tracks Yet, Add some !";
         }

         StringBuilder sb = new StringBuilder("Your Recent Additions : \n");
         for (Track t : tracks){
             sb.append("● ").append(t.getId()).append(" →").append(t.getStatus()).append("\n");
         }
         return sb.toString();
     }

    public String handleStart() {
        return """
            Hey there ! Welcome to Reelics!

            Send me an Instagram Reel / Post link
            and I’ll extract the audio for you and You can access that through the Music App

            Commands:
            /mytracks  → Your recent uploads
            /status <trackId> → Track status
            /help → Show help
            """;
    }
    public String handleHelp() {
        return """
            🆘 Reelics Bot Help

            ▶ Send an Instagram link to start processing
            ▶ /mytracks — view recent tracks
            ▶ /status <trackId> — check processing status

            Example:
            /status 550e8400-e29b-41d4-a716-446655440000
            """;
    }


}
