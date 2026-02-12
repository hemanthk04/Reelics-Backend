package com.projects.reelics.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;


@Service
public class DownloadAudioService {
    private static final Logger log  = LoggerFactory.getLogger(DownloadAudioService.class);
    private static final String AUDIO_DIR = "/tmp/reelics/audio";

    public File downloadAudio(String instagramURL, UUID trackId){
        try{
            new File(AUDIO_DIR).mkdirs();
            String outputTemplate = AUDIO_DIR + "/" + trackId + ".%(ext)s";

            ProcessBuilder processBuilder= new ProcessBuilder(
                    "yt-dlp",
                    "-f", "bestaudio",
                    "--extract-audio",
                    "--audio-format", "mp3",
                    "--audio-quality", "192K",
                    "-o", outputTemplate, instagramURL
            );

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();

            File audioFile= new File(AUDIO_DIR+"/"+trackId+ ".mp3");

            if(!audioFile.exists()){
                throw new RuntimeException("Audio download Failed");
            }

            log.info("Audio downloaded successfully for track {}", trackId);
            return audioFile;
        } catch (Exception e){
            throw new RuntimeException("Audio download error", e);
        }
    }
}
