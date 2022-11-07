package com.jing.gmall.product;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class BloomFilterTest {
    @Test
    public void testBF(){
        Funnel<CharSequence> funnel = Funnels.stringFunnel(StandardCharsets.UTF_8);
        BloomFilter<CharSequence> filter = BloomFilter.create(funnel, 1000000, 0.000001);

        filter.put("");
    }
}
