package com.lowlifelove.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

@Component
public class FileUtil {

    @Value("${article.storage.path}")
    private String articleBasePath;

    public String readTxtFile(String relativePath) {
        try {
            Path fullPath = Paths.get(articleBasePath, relativePath);
            byte[] bytes = Files.readAllBytes(fullPath);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "[读取失败：" + e.getMessage() + "]";
        }
    }
}



