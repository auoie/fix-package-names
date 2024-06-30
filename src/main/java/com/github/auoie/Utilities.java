package com.github.auoie;

import com.github.slugify.Slugify;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

public class Utilities {
  public static void fixPackageNamesRecursive(Path directoryPath) throws IOException {
    if (Files.isRegularFile(directoryPath)) {
      System.out.println("file: " + directoryPath);
      return;
    }
    try (var newFiles = Files.list(directoryPath)) {
      newFiles.forEach(
          newFile -> {
            if ((Files.isDirectory(newFile) || Files.isRegularFile(newFile))
                && !Files.isSymbolicLink(newFile)) {
              try {
                fixPackageNamesRecursive(newFile);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            }
          });
      System.out.println("directory: " + directoryPath);
    }
  }

  public static String getNewDirectoryName(Slugify slg, String fileName) {
    fileName = slg.slugify(fileName);
    if (fileName.length() == 0) {
      return fileName;
    }
    fileName = fileName.replaceAll("-", "_");
    char firstChar = fileName.charAt(0);
    if ('0' <= firstChar && firstChar <= '9') {
      return "_" + fileName;
    }
    return fileName;
  }

  public static void fixPackageNames(Path directoryPath, boolean onlyPlan) throws IOException {
    // Using post-order tree traversal
    Deque<Path> stack = new ArrayDeque<>();
    Deque<Path> nextStack = new ArrayDeque<>();
    stack.add(directoryPath);
    while (!stack.isEmpty()) {
      Path curFilePath = stack.pop();
      if (Files.isDirectory(curFilePath)) {
        try (var newFiles = Files.list(curFilePath)) {
          newFiles.forEach(
              newFile -> {
                if ((Files.isDirectory(newFile) || Files.isRegularFile(newFile))
                    && !Files.isSymbolicLink(newFile)) {}
                stack.push(newFile);
              });
        }
      }
      nextStack.push(curFilePath);
    }
    Slugify slg = Slugify.builder().underscoreSeparator(true).build();
    while (!nextStack.isEmpty()) {
      Path curDirPath = nextStack.pop();
      if (Files.isDirectory(curDirPath)) {
        String currentDirectoryName = curDirPath.getFileName().toString();
        String newDirectoryName = getNewDirectoryName(slg, currentDirectoryName);
        var parentPath = curDirPath.getParent();
        System.out.println(curDirPath);
        System.out.println(currentDirectoryName + " -> " + newDirectoryName);
        if (!onlyPlan) {
          Path newPath = parentPath.resolve(newDirectoryName);
          if (!Files.exists(newPath)) {
            System.out.println("Renaming...");
            System.out.println(newPath);
            Files.move(curDirPath, newPath);
          }
        }
        if (!nextStack.isEmpty()) {
          System.out.println();
        }
      }
    }
  }
}
