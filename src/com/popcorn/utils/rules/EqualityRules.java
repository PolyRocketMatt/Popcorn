package com.popcorn.utils.rules;

import com.popcorn.utils.values.LiteralValue;

public class EqualityRules {

    public static boolean isEqual(LiteralValue left, LiteralValue right) {
        switch (left.getType()) {
            case INT:
                int leftIntVal = (int) left.getValue();
                int rightIntVal = (int) right.getValue();

                return leftIntVal == rightIntVal;
            case FLOAT:
                float leftFloatVal = (float) left.getValue();
                float rightFloatVal = (float) right.getValue();

                return leftFloatVal == rightFloatVal;
            case BOOL:
                boolean leftBoolVal = (boolean) left.getValue();
                boolean rightBoolVal = (boolean) right.getValue();

                return leftBoolVal == rightBoolVal;
            case STRING:
                String leftStringVal = (String) left.getValue();
                String rightStringVal = (String) right.getValue();

                return leftStringVal.equals(rightStringVal);
        }

        return false;
    }

}
