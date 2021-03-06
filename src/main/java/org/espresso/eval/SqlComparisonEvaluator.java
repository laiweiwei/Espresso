/*
 * Copyright 2012 Espresso Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.espresso.eval;

import static org.espresso.eval.ClassUtil.isFloatPrecision;
import static org.espresso.eval.ClassUtil.isNumber;

/**
 * Implements the different possible numeric comparisons, or assumes the operands
 * implement "Comparable".
 *
 * @author <a href="mailto:antenangeli@yahoo.com">Alberto Antenangeli</a>
 */
public enum SqlComparisonEvaluator {
    LONG_LONG {
        @Override
        public int compare(final Object leftOperand, final Object rightOperand) {
            long left = ((NumberWrapper) leftOperand).asLong();
            long right = ((NumberWrapper) rightOperand).asLong();
            return left == right ? 0 : (left < right ? -1 : 1);
        }
    },
    LONG_DOUBLE {
        @Override
        public int compare(Object leftOperand, Object rightOperand) {
            double left = ((NumberWrapper) leftOperand).asLong();
            double right = ((NumberWrapper) leftOperand).asDouble();
            return left == right ? 0 : (left < right ? -1 : 1);
        }
    },
    DOUBLE_LONG {
        @Override
        public int compare(Object leftOperand, Object rightOperand) {
            double left = ((NumberWrapper) leftOperand).asDouble();
            double right = ((NumberWrapper) leftOperand).asDouble();
            return left == right ? 0 : (left < right ? -1 : 1);
        }
    },
    DOUBLE_DOUBLE {
        @Override
        public int compare(Object leftOperand, Object rightOperand) {
            double left = ((NumberWrapper) leftOperand).asDouble();
            double right = ((NumberWrapper) leftOperand).asDouble();
            return left == right ? 0 : (left < right ? -1 : 1);
        }
    },
    OTHER_OTHER {
        @Override
        public int compare(Object leftOperand, Object rightOperand) {
            return ((Comparable) leftOperand).compareTo(rightOperand);
        }
    };

    public abstract int compare(final Object leftOperand, final Object rightOperand);

    public static SqlComparisonEvaluator pickEvaluator(final Object left, final Object right) {
        final Class leftClass = left.getClass();
        final Class rightClass = right.getClass();
        if (isNumber(leftClass) && isNumber(rightClass)) {
            if (isFloatPrecision(leftClass))
                if (isFloatPrecision(rightClass))
                    return DOUBLE_DOUBLE;
                else
                    return DOUBLE_LONG;
            else if (isFloatPrecision(rightClass))
                return LONG_DOUBLE;
            else return LONG_LONG;
        }
        return OTHER_OTHER;
    }
}
