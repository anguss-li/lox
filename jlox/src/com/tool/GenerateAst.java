package jlox.src.com.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    /**
     * Metaprogramatically generate boilerplate code for the Lox AST, defining
     * classes for all our expression subtypes
     * 
     * @param args the output directory
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"));
    }

    /**
     * Write boilerplate code for AST expression types to a set output directory.
     * 
     * @param outputDir the output directory
     * @param baseName  the output filename and name of the outer abstract class
     * @param types     the expression types
     * @throws IOException if the .java file cannot be written
     */
    private static void defineAst(
            String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package jlox.src.com.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        // The AST classes.
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        // The base accept() method (for the Visitor pattern).
        writer.println();
        writer.println(" abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    /**
     * Output boilerplate code for the Visitor interface for an AST expression
     * type: https://en.wikipedia.org/wiki/Visitor_pattern
     * 
     * @param writer
     * @param baseName
     * @param types
     */
    private static void defineVisitor(
            PrintWriter writer, String baseName, List<String> types) {
        writer.println(" interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println(" R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println(" }");
    }

    /**
     * Output boilerplate code for an AST expression type
     * 
     * @param writer    the writer in charge of creating the .java file
     * @param baseName  the output filename and name of the outer abstract class
     * @param className the name of the AST expression type
     * @param fieldList the parameters of the AST expression type
     */
    private static void defineType(
            PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println(" static class " + className + " extends " + baseName + " {");

        // Constructor
        writer.println(" " + className + "(" + fieldList + ") {");

        // Store parameters in fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println(" this." + name + " = " + name + ";");
        }

        writer.println(" }");

        // Visitor pattern.
        writer.println();
        writer.println(" @Override");
        writer.println(" <R> R accept(Visitor<R> visitor) {");
        writer.println(" return visitor.visit" + className + baseName + "(this);");
        writer.println(" }");

        // Fields.
        writer.println();
        for (String field : fields) {
            writer.println(" final " + field + ";");
        }

        writer.println(" }");
    }
}
