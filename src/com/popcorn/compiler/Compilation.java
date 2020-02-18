package com.popcorn.compiler;

import com.popcorn.compiler.binding.Binder;
import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.compiler.node.Node;
import com.popcorn.compiler.node.statements.ObjectStatementNode;
import com.popcorn.exception.PopcornException;
import com.popcorn.interpreter.Interpreter;
import com.popcorn.utils.SyntaxTree;
import com.popcorn.utils.diagnostics.Diagnostic;
import com.popcorn.utils.enums.ValueType;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.values.LiteralValue;
import com.popcorn.utils.values.VariableSymbol;

import java.util.ArrayList;
import java.util.List;

public class Compilation {

    private List<Diagnostic> diagnostics;
    private Interpreter interpreter;
    private Binder binder;
    private List<LiteralValue> values;
    private SyntaxTree tree;

    private long compilationTime;
    private boolean wasSuccessful;

    public Compilation(SyntaxTree tree, long parseTime) {
        diagnostics = tree.getDiagnostics();
        interpreter = new Interpreter();
        binder = new Binder();
        values = new ArrayList<>();
        compilationTime = parseTime;
        wasSuccessful = false;

        this.tree = tree;
    }

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public List<LiteralValue> getValues() {
        return values;
    }

    public SyntaxTree getTree() {
        return tree;
    }

    public void setTree(SyntaxTree tree) {
        this.tree = tree;
    }

    public void exec() {
        long interpretationDeltaTime = System.currentTimeMillis();

        if (tree.getParentNode().getObject() instanceof ObjectStatementNode) {
            for (Node node : ((ObjectStatementNode) tree.getParentNode().getObject()).getBody()) {
                values.add(evaluate(node));
            }

            wasSuccessful = true;
        }

        interpretationDeltaTime = System.currentTimeMillis() - interpretationDeltaTime;
        compilationTime = interpretationDeltaTime;
    }

    // TODO: 10/02/2020 Make type checker more accessible
    // TODO: 11/02/2020 Fix clear binder diagnostics
    private BoundNode createBoundNode(Node node) throws PopcornException {
        BoundNode boundNode = binder.bindNode(node);

        if (binder.getDiagnostics().getDiagnostics().isEmpty()) {
            for (VariableSymbol symbol : binder.getVariables()) {
                if (!interpreter.getVariables().containsKey(symbol))
                    interpreter.getVariables().put(symbol, new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null));
            }
        } else {
            for (Diagnostic diagnostic : binder.getDiagnostics().getDiagnostics())
                System.out.println(diagnostic.getMessage());
            binder.getDiagnostics().getDiagnostics().clear();
        }

        return boundNode;
    }

    public LiteralValue evaluate(Node unboundNode) {
        try {
            BoundNode node = createBoundNode(unboundNode);

            if (node != null) {
                LiteralValue value = interpreter.evaluate(node);

                if (interpreter.getDiagnostics().getDiagnostics().isEmpty()) {
                    return value;
                } else {
                    for (Diagnostic diagnostic : interpreter.getDiagnostics().getDiagnostics())
                        System.out.println(diagnostic.getMessage());
                    interpreter.getDiagnostics().getDiagnostics().clear();
                }
            }
        } catch (PopcornException ex) {
            System.out.println(ex.getMessage());
        }

        return new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null);
    }

    public long getCompilationTime() {
        return compilationTime;
    }

    public boolean wasSuccessful() {
        return wasSuccessful;
    }
}
