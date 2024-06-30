package com.github.auoie;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "fix-package-names",
    mixinStandardHelpOptions = true,
    description =
        "Convert folder names to package names compatible with javac. For example, converts '1."
            + " Hello World' to '_1_hello_world'")
class FixPackageNames implements Callable<Integer> {
  @Option(
      required = true,
      names = {"-p", "--path"},
      description = "path to convert folder names to package names")
  private File inputPathName;

  @Option(
      names = {"-f", "--force"},
      description = "Proceed without asking for confirmation")
  private boolean force;

  @Option(
      names = {"--plan"},
      description = "Print the renaming plan without actually renaming the folders")
  private boolean onlyPlan;

  @Override
  public Integer call() {
    Path path = inputPathName.toPath().normalize().toAbsolutePath();
    if (!Files.exists(path)) {
      System.err.println("Unable to find path: " + path);
      return 1;
    }
    if (!Files.isDirectory(path)) {
      System.err.println("Not a directory: " + path);
      return 1;
    }
    if (Files.isSymbolicLink(path)) {
      System.err.println("Is a symbolic link: " + path);
      return 1;
    }
    if (!force) {
      System.out.println("Found path: " + path);
      System.out.print("Do you want to rename folder in and including this path? (y/N): ");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String line = null;
      try {
        line = reader.readLine();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
      line = line.trim();
      if (!line.equals("y")) {
        System.err.println("You answered: " + line + ". Exiting");
        return 1;
      }
    }
    try {
      Utilities.fixPackageNames(path, onlyPlan);
    } catch (IOException e) {
      System.err.println("Encountered an error:");
      System.err.println(e.getMessage());
      System.err.println(Arrays.toString(e.getStackTrace()));
      return 1;
    }
    return 0;
  }
}
