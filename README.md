# fix-package-names

Recursively converts folder names to `javac`-compatible package names.
For example, converts `1. Hello-World` to `_1_hello_world`.

## Usage

```bash
mvn dependency:build-classpath -DincludeScope=compile -Dmdep.outputFile=out/cp.txt
javac -cp "$(cat ./out/cp.txt)" src/main/java/**/*.java -d target/classes
java -cp ./target/classes:"$(cat out/cp.txt)" com.github.auoie.Main --help
```

After you fix the folder names, fix all of the package names using Intellij.
See [this question](https://youtrack.jetbrains.com/issue/IJPL-26321/Provide-package-name-fixing-at-project-package-level)
to do that.

## Help

```text
java -cp ./target/classes:"$(cat out/cp.txt)" com.github.auoie.Main --help
Usage: fix-package-names [-fhV] [--plan] -p=<inputPathName>
Convert folder names to package names compatible with javac. For example,
converts '1. Hello World' to '_1_hello_world'
  -f, --force     Proceed without asking for confirmation
  -h, --help      Show this help message and exit.
  -p, --path=<inputPathName>
                  path to convert folder names to package names
      --plan      Print the renaming plan without actually renaming the folders
  -V, --version   Print version information and exit.
```