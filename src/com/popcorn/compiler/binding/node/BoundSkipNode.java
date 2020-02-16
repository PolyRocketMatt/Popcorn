package com.popcorn.compiler.binding.node;

import com.popcorn.utils.enums.BoundNodeKind;

public class BoundSkipNode implements BoundNode {

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.SKIP;
    }
}
