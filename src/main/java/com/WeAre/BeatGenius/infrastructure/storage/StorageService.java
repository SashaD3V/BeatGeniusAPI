package com.WeAre.BeatGenius.infrastructure.storage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {
  private final Path root = Paths.get("uploads");

  public StorageService() {
    try {
      Files.createDirectories(root);
    } catch (Exception e) {
      throw new RuntimeException("Impossible d'initialiser le dossier de stockage");
    }
  }

  public String store(MultipartFile file) {
    try {
      String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
      Files.copy(file.getInputStream(), this.root.resolve(filename));
      return "http://localhost:8080/uploads/" + filename;
    } catch (Exception e) {
      throw new RuntimeException("Impossible de sauvegarder le fichier. Error: " + e.getMessage());
    }
  }
}
