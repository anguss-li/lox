package jlox.src.com.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    private static boolean hadError = false;

    /**
     * Read lox code, either through a source file or through a REPL.
     * 
     * @param args a valid filepath to a lox source file, if desired
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            // Run the provided source file
            runFile(args[0]);
        } else {
            // If no filepath given, present a REPL
            runPrompt();
        }
    }

    /**
     * Read a lox source file and execute it.
     * 
     * @param path a valid filepath to a lox source file
     * @throws IOException
     */
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (hadError) {
            System.exit(65);
        }
    }

    /**
     * Present a REPL to interactively run lox code.
     * 
     * @throws IOException
     */
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            hadError = false;
        }
    }

    /**
     * Run lox source code.
     * 
     * @param source a valid lox program
     */
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // TODO: for now, print tokens
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    /**
     * Display that an error has occurred to the user.
     * 
     * @param line    line of source code where the error originates
     * @param message some helpful message for the user
     */
    public static void error(int line, String message) {
        report(line, "", message);
    }

    /**
     * Record an error has occurred, and inform the user.
     * 
     * @param line    line of source code where the error originates
     * @param where   part of the line where the error originates
     * @param message some helpful message for the user
     */
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}