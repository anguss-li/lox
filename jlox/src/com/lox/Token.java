package jlox.src.com.lox;

public class Token {
    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final int line;

    /**
     * Implement lox tokens.
     * 
     * @param type    see TokenType
     * @param lexeme  the raw substring of source code corresponding to the token
     * @param literal the token's value if it is a literal; null otherwise
     * @param line    the token's line in source code
     */
    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    /**
     * Represent the token as a string.
     */
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
