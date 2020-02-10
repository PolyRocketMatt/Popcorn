package com.popcorn.compiler;

import com.popcorn.compiler.binding.Binder;
import com.popcorn.compiler.binding.node.BoundExpressionNode;
import com.popcorn.utils.SyntaxTree;
import com.popcorn.utils.diagnostics.Diagnostic;

import java.util.List;

public class Compilation {

    private List<Diagnostic> diagnostics;
    private SyntaxTree tree;

    public Compilation(SyntaxTree tree) {
        this.diagnostics = tree.getDiagnostics();
        this.tree = tree;
    }

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public SyntaxTree getTree() {
        return tree;
    }

    // TODO: 10/02/2020 Make type checker more accessible
    public BoundExpressionNode getBoundRoot() throws Exception {
        Binder binder = new Binder();

        diagnostics.addAll(binder.getDiagnostics().getDiagnostics());

        return binder.bindExpression(tree.getRoot());
    }
}
