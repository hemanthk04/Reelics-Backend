package com.projects.reelics.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Map;

@Service
public class DriveUploaderService {
    private static final Logger log = LoggerFactory.getLogger(DriveUploaderService.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public Map<String, String> upload(File audioFile){
        try{
            ProcessBuilder pb = new ProcessBuilder("python3", "/tools/drive_uploader.py", audioFile.getAbsolutePath());

            pb.redirectErrorStream(true);
            Process process= pb.start();

            StringBuilder output = new StringBuilder();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                String line;
                while((line = reader.readLine()) != null){
                    output.append(line);
                }
            }
            int exitCode = process.waitFor();

            log.info("Drive uploader exited with code {}", exitCode);
            log.info("Drive uploader output: {}", output);

            if (exitCode != 0) {
                throw new RuntimeException("Drive upload failed: " + output);
            }
            return mapper.readValue(output.toString(), Map.class);
        }
        catch (Exception e){
            throw new RuntimeException("Drive upload execution failed", e);
        }
    }
}
