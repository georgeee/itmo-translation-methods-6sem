package ru.georgeee.itmo.sem6.tranlsation.lab03;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.AstNode;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.Var;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.*;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.op.*;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.stmt.*;

import java.io.IOException;
import java.util.Scanner;

public class JavaGenerator implements Generator {
    private static final String INDENT_STEP = "   ";
    private int indent;
    private Appendable out;
    @Getter
    @Setter
    private String packageName = "";
    @Getter
    @Setter
    private String className = "Main";

    @Override
    public void generate(CompoundStmt program, Appendable out) throws GenerationException, IOException {
        init(out);
        try {
            appendClassHeader();
            generateCompoundStmt(program);
            appendClassFooter();
        } catch (RuntimeGenerationException e) {
            throw e.getCause();
        } catch (RuntimeIOException e) {
            throw e.getCause();
        }
    }

    private void appendClassHeader() {
        if (StringUtils.isNotEmpty(packageName)) {
            appendIndNL("package " + packageName + ";");
        }
        appendIndNL("public class " + className + " {");
        incInd();
        appendIndNL("private final java.util.Scanner scanner = new java.util.Scanner(System.in);");
        appendNL();
        Scanner scanner = new Scanner(System.in);
        appendIndNL("boolean readBool() {");
        incInd();
        appendIndNL("return this.scanner.nextBoolean();");
        decInd();
        appendIndNL("}");
        appendNL();
        appendIndNL("int readInt() {");
        incInd();
        appendIndNL("return this.scanner.nextInt();");
        decInd();
        appendIndNL("}");
        appendNL();
        appendIndNL("void print(boolean b) {");
        incInd();
        appendIndNL("System.out.println(b);");
        decInd();
        appendIndNL("}");
        appendNL();
        appendIndNL("void print(int i) {");
        incInd();
        appendIndNL("System.out.println(i);");
        decInd();
        appendIndNL("}");
        appendNL();
        appendIndNL("public static void main(String[] args) {");
        incInd();
        appendIndNL("new " + className + "().doIt();");
        decInd();
        appendIndNL("}");
        appendNL();
        appendIndNL("void doIt() {");
        incInd();
    }

    private void appendClassFooter() {
        decInd();
        appendIndNL("}");
        appendNL();
        decInd();
        appendIndNL("}");
    }

    private void generateCompoundStmt(CompoundStmt program) {
        for (Stmt stmt : program.getStatements()) {
            generateStmt(stmt);
        }
    }

    private void init(Appendable out) {
        this.out = out;
        indent = 0;
    }

    private void generateStmt(Stmt stmt) {
        if (stmt instanceof CompoundStmt) {
            generateCompoundStmt((CompoundStmt) stmt);
        } else if (stmt instanceof ConditionStmt) {
            generateConditionStmt((ConditionStmt) stmt);
        } else if (stmt instanceof CtxStmt) {
            generateCtxStmt((CtxStmt) stmt);
        } else if (stmt instanceof ExprStmt) {
            generateExprStmt((ExprStmt) stmt);
        } else if (stmt instanceof LoopStmt) {
            generateLoopStmt((LoopStmt) stmt);
        } else if (stmt instanceof PrintStmt) {
            generatePrintStmt((PrintStmt) stmt);
        } else if (stmt instanceof ReadStmt) {
            generateReadStmt((ReadStmt) stmt);
        } else if (stmt instanceof VarStmt) {
            generateVarStmt((VarStmt) stmt);
        } else {
            throw unexpectedCase(stmt);
        }
    }

    private void generateVarStmt(VarStmt stmt) {
        Var var = stmt.getVar();
        if (var instanceof BoolVar) {
            appendIndNL("boolean " + getName(var) + " = false;");
        } else if (var instanceof IntVar) {
            appendIndNL("int " + getName(var) + " = 0;");
        } else {
            throw unexpectedCase(var);
        }
    }

    private void generateReadStmt(ReadStmt stmt) {
        Var var = stmt.getVar();
        appendInd(getName(var));
        if (var instanceof BoolVar) {
            appendNL(" = readBool();");
        } else if (var instanceof IntVar) {
            appendNL(" = readInt();");
        } else {
            throw unexpectedCase(var);
        }
    }

    private RuntimeGenerationException unexpectedCase(AstNode node) {
        return unexpectedCase("unknown ast node type for " + node);
    }

    private RuntimeGenerationException unexpectedCase(Var var) {
        return unexpectedCase("unknown var type for " + var);
    }

    private RuntimeGenerationException unexpectedCase() {
        return unexpectedCase("");
    }

    private RuntimeGenerationException unexpectedCase(String desc) {
        if (StringUtils.isEmpty(desc)) {
            desc = "";
        } else {
            desc = ", " + desc;
        }
        try {
            throw new GenerationException("Unexpected case: some internal generater error" + desc);
        } catch (GenerationException e) {
            return new RuntimeGenerationException(e);
        }
    }

    private String getName(Var var) {
        if (var instanceof BoolVar) {
            return "b$" + var.getName().substring(1);
        } else if (var instanceof IntVar) {
            return var.getName();
        } else {
            throw unexpectedCase(var);
        }
    }

    private void generatePrintStmt(PrintStmt stmt) {
        appendInd("print(");
        generateExpr(stmt.getExpr(), true);
        appendNL(");");
    }

    private void generateLoopStmt(LoopStmt stmt) {
        appendInd("while (");
        generateExpr(stmt.getCondition(), true);
        appendNL("){");
        incInd();
        generateStmt(stmt.getBody());
        decInd();
        appendIndNL("}");
    }

    private void generateExprStmt(ExprStmt stmt) {
        appendInd();
        generateExpr(stmt.getExpr(), true);
        appendNL(";");
    }

    private void generateCtxStmt(CtxStmt stmt) {
        appendIndNL("{");
        incInd();
        generateStmt(stmt.getStmt());
        decInd();
        appendIndNL("}");
    }

    private void generateConditionStmt(ConditionStmt stmt) {
        appendInd("if (");
        generateExpr(stmt.getCondition(), true);
        appendNL("){");
        incInd();
        generateStmt(stmt.getTrueStmt());
        decInd();
        appendIndNL("} else {");
        incInd();
        generateStmt(stmt.getFalseStmt());
        decInd();
        appendIndNL("}");
    }

    private void decInd() {
        --indent;
    }

    private void incInd() {
        ++indent;
    }

    private void generateExpr(Expr expr) {
        generateExpr(expr, false);
    }

    private void generateExpr(Expr expr, boolean omitParens) {
        if (expr instanceof BoolLiteral) {
            append(String.valueOf(((BoolLiteral) expr).getValue()));
        } else if (expr instanceof BoolVar || expr instanceof IntVar) {
            append(getName((Var) expr));
        } else if (expr instanceof IntLiteral) {
            append(String.valueOf(((IntLiteral) expr).getValue()));
        } else {
            if (!omitParens) {
                append("(");
            }
            if (expr instanceof AssignArithOp) {
                AssignArithOp op = (AssignArithOp) expr;
                generateAssign(op.getVar(), op.getExpr());
            } else if (expr instanceof AssignLogOp) {
                AssignLogOp op = (AssignLogOp) expr;
                generateAssign(op.getVar(), op.getExpr());
            } else if (expr instanceof BinaryArithCondOp) {
                BinaryArithCondOp op = (BinaryArithCondOp) expr;
                generateBinary(op.getLeftOperand(), op.getRightOperand(), getSign(op.getType()));
            } else if (expr instanceof BinaryArithOp) {
                BinaryArithOp op = (BinaryArithOp) expr;
                generateBinary(op.getLeftOperand(), op.getRightOperand(), getSign(op.getType()));
            } else if (expr instanceof BinaryLogOp) {
                BinaryLogOp op = (BinaryLogOp) expr;
                generateBinary(op.getLeftOperand(), op.getRightOperand(), getSign(op.getType()));
            } else if (expr instanceof EqualityCondOp) {
                EqualityCondOp op = (EqualityCondOp) expr;
                generateBinary(op.getLeftOperand(), op.getRightOperand(), getSign(op.getType()));
            } else if (expr instanceof TernaryArithOp) {
                TernaryArithOp op = (TernaryArithOp) expr;
                generateTernary(op.getCondition(), op.getTrueOperand(), op.getFalseOperand());
            } else if (expr instanceof TernaryLogOp) {
                TernaryLogOp op = (TernaryLogOp) expr;
                generateTernary(op.getCondition(), op.getTrueOperand(), op.getFalseOperand());
            } else if (expr instanceof UnaryArithOp) {
                UnaryArithOp op = (UnaryArithOp) expr;
                generateUnary(op.getOperand(), getSign(op.getType()));
            } else if (expr instanceof UnaryLogOp) {
                UnaryLogOp op = (UnaryLogOp) expr;
                generateUnary(op.getOperand(), getSign(op.getType()));
            } else {
                unexpectedCase(expr);
            }
            if (!omitParens) {
                append(")");
            }
        }
    }

    private void generateUnary(Expr operand, String sign) {
        append(sign);
        generateExpr(operand);
    }

    private void generateTernary(BoolExpr condition, Expr trueOperand, Expr falseOperand) {
        generateExpr(condition);
        append(" ? ");
        generateExpr(trueOperand);
        append(" : ");
        generateExpr(falseOperand);
    }

    private void generateAssign(Var var, Expr expr) {
        append(getName(var));
        append(" = ");
        generateExpr(expr, true);
    }

    private String getSign(UnaryLogOp.Type type) {
        switch (type) {
            case NOT:
                return "!";
        }
        throw unexpectedCase(type);
    }

    private String getSign(UnaryArithOp.Type type) {
        switch (type) {
            case NEG:
                return "-";
        }
        throw unexpectedCase(type);
    }

    private String getSign(EqualityCondOp.Type type) {
        switch (type) {
            case EQ:
                return "==";
            case NEQ:
                return "!=";
        }
        throw unexpectedCase(type);
    }

    private String getSign(BinaryArithCondOp.Type type) {
        switch (type) {
            case GT:
                return ">";
            case GTE:
                return ">=";
            case LT:
                return "<";
            case LTE:
                return "<=";
        }
        throw unexpectedCase(type);
    }

    private String getSign(BinaryLogOp.Type type) {
        switch (type) {
            case AND:
                return "&&";
            case OR:
                return "||";
        }
        throw unexpectedCase(type);
    }

    private String getSign(BinaryArithOp.Type type) {
        switch (type) {
            case ADD:
                return "+";
            case DIV:
                return "/";
            case MOD:
                return "%";
            case MUL:
                return "*";
            case SUB:
                return "-";
        }
        throw unexpectedCase(type);
    }

    private RuntimeGenerationException unexpectedCase(Enum type) {
        return unexpectedCase(type.getDeclaringClass() + " enum's option " + type.name());
    }

    private void generateBinary(Expr left, Expr right, String sign) {
        generateExpr(left);
        append(" " + sign + " ");
        generateExpr(right);
    }

    private void append(String s) {
        try {
            out.append(s);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    private void appendIndNL(String s) {
        appendInd(s);
        appendNL();
    }

    private void appendNL(String s) {
        append(s);
        appendNL();
    }

    private void appendNL() {
        append("\n");
    }

    private void appendInd(String s) {
        appendInd();
        append(s);
    }

    private void appendInd() {
        for (int i = 0; i < indent; ++i) {
            append(INDENT_STEP);
        }
    }

    private static class RuntimeGenerationException extends RuntimeException {
        @Getter
        private final GenerationException cause;

        RuntimeGenerationException(GenerationException cause) {
            super(cause);
            this.cause = cause;
        }
    }

    private static class RuntimeIOException extends RuntimeException {
        @Getter
        private final IOException cause;

        RuntimeIOException(IOException cause) {
            super(cause);
            this.cause = cause;
        }
    }
}
