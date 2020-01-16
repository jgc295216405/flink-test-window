package com.test.enums;

import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum AggregationEnum {
    COUNT() {
        @Override
        public double execute(final List<Double> values) {
            return values.size();
        }
    },
    SUM() {
        @Override
        public double execute(final List<Double> values) {
            double sum = 0;
            for (double d : values)
                sum += d;
            return sum;
        }
    },
    MIN() {
        @Override
        public double execute(final List<Double> values) {
            return Collections.min(values);
        }
    },
    MAX() {
        @Override
        public double execute(final List<Double> values) {
            return Collections.max(values);
        }
    },
    AVG() {
        @Override
        public double execute(final List<Double> values) {
            return SUM.apply(values) / values.size();
        }
    };

    /**
     * @param values
     * @return
     */
    public double apply(final List<Double> values) {
        if (CollectionUtils.isEmpty(values)) {
            return 0;
        }
        values.removeAll(Collections.singleton(null));
        return this.execute(values);
    }

    /**
     * @param values
     * @return
     */
    public double apply(final Double[] values) {
        if (values == null) {
            return 0;
        }
        return this.execute(Arrays.asList(values));
    }

    /**
     * 抽象函数
     *
     * @param values
     * @return
     */
    protected abstract double execute(final List<Double> values);
}
