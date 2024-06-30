package com.github.auoie;

import picocli.CommandLine;

public class Main {
  public static void main(String[] args) {
    int exitCode = new CommandLine(new FixPackageNames()).execute(args);
    System.exit(exitCode);
  }
}
