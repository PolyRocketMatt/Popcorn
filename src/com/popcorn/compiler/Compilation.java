package com.popcorn.compiler;

import com.popcorn.compiler.binding.Binder;
import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.compiler.node.ExpressionNode;
import com.popcorn.compiler.node.Node;
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

    public Compilation(SyntaxTree tree) {
        diagnostics = tree.getDiagnostics();
        interpreter = new Interpreter();
        binder = new Binder();
        values = new ArrayList<>();

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
        for (Node node : tree.getParentNode().getNodes()) {
            if (node instanceof ExpressionNode) {
                values.add(evaluate((ExpressionNode) node));
            }
        }
    }

    // TODO: 10/02/2020 Make type checker more accessible
    // TODO: 11/02/2020 Fix clear binder diagnostics
    private BoundNode createBoundNode(ExpressionNode node) throws Exception {
        BoundNode boundNode = binder.bindExpression(node);

        diagnostics.addAll(binder.getDiagnostics().getDiagnostics());

        if (!diagnostics.isEmpty()) {
            binder.getDiagnostics().getDiagnostics().clear();

            return null;
        } else {
            //Update the interpreter variables
            for (VariableSymbol symbol : binder.getVariables()) {
                if (!interpreter.getVariables().containsKey(symbol)) {
                    interpreter.getVariables().put(symbol, new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null));
                }
            }

            return boundNode;
        }
    }

    public LiteralValue evaluate(ExpressionNode unboundNode) {
        try {
            BoundNode node = createBoundNode(unboundNode);

            if (node != null) {
                LiteralValue value = interpreter.evaluate(node);

                if (interpreter.getDiagnostics().getDiagnostics().isEmpty()) {
                    return value;
                } else {
                    for (Diagnostic diagnostic : interpreter.getDiagnostics().getDiagnostics()) {
                        System.out.println(diagnostic.getMessage());
                    }

                    interpreter.getDiagnostics().getDiagnostics().clear();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new LiteralValue(ConversionUtils.DataType.NOT_DEFINED, ValueType.NULL, null);
    }
}
