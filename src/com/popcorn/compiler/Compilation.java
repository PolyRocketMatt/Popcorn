package com.popcorn.compiler;

import com.popcorn.compiler.binding.Binder;
import com.popcorn.compiler.binding.node.BoundNode;
import com.popcorn.interpreter.Interpreter;
import com.popcorn.utils.SyntaxTree;
import com.popcorn.utils.diagnostics.Diagnostic;
import com.popcorn.utils.enums.ValueType;
import com.popcorn.utils.utilities.ConversionUtils;
import com.popcorn.utils.values.LiteralValue;
import com.popcorn.utils.values.VariableSymbol;

import java.util.List;

public class Compilation {

    private List<Diagnostic> diagnostics;
    private Interpreter interpreter;
    private Binder binder;
    private SyntaxTree tree;

    public Compilation(SyntaxTree tree) {
        diagnostics = tree.getDiagnostics();
        interpreter = new Interpreter();
        binder = new Binder();

        this.tree = tree;
    }

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public Binder getBinder() {
        return binder;
    }

    public SyntaxTree getTree() {
        return tree;
    }

    public void setTree(SyntaxTree tree) {
        this.tree = tree;
    }

    // TODO: 10/02/2020 Make type checker more accessible
    // TODO: 11/02/2020 Fix clear binder diagnostics
    public BoundNode createBoundNode() throws Exception {
        BoundNode boundNode = binder.bindExpression(tree.getRoot());

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

    public LiteralValue evaluate() {
        try {
            BoundNode node = createBoundNode();

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
