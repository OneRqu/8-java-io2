package com.example.task01;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Task01Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println(extractSoundName(new File("task01/src/main/resources/3727.mp3")));

    }

    public static String extractSoundName(File file) throws IOException, InterruptedException {
        if (!file.exists()) {
            throw new IOException("File " + file.getAbsolutePath() + " doesn't exist");
        }

        String[] command = {"C:/ffmpeg/bin/ffprobe.exe", "-v", "error", "-of", "flat", "-show_format", file.getAbsolutePath()};
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            String target = "format.tags.title=";

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(target)) {
                    String titleTag = line.substring(target.length());
                    titleTag = titleTag.substring(1, titleTag.length() - 1);

                    int exitValue = process.waitFor();
                    if (exitValue != 0) {
                        throw new IOException("ffprobe exited with code " + exitValue);
                    }

                    return titleTag;
                }
            }
        }

        int exitValue = process.waitFor();
        if (exitValue != 0) {
            throw new IOException("ffprobe exited with code " + exitValue);
        }

        return null;
    }
}