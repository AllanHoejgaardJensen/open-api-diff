package dk.hoejgaard.openapi.diff.output;

import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class FormatterTest {

    @Test
    public void testOut(){
        assertEquals("This Is The Result 10 ", Formatter.out("this.is.the.result.10"));
        assertEquals("This Is The Result 10 ", Formatter.out("this.is.the result.10"));
        assertEquals("This Is The Result 10 ", Formatter.out("this.is the.result.10"));
        assertEquals("This Is The Result 10 ", Formatter.out("this is.the.result.10"));
        assertEquals("This Is The Result 10 ", Formatter.out("this is the result 10"));
        assertEquals("This Is The Result 10 ", Formatter.out("this is the result-10"));
        assertEquals("This Is The Result 10 ", Formatter.out("this.is.the.result-10"));
        assertEquals("This Is The Result Too ", Formatter.out("this.is.the.result-too"));
        assertEquals("", Formatter.out(""));
    }

    @Test
    public void testXOut(){
        assertEquals("Of This Is Clear ", Formatter.outX("existing.compliance.of.this.is.clear"));
        assertEquals("For This Is Clear ", Formatter.outX("existing.compliance.for.this.is.clear"));
        assertEquals("Of This Is Clear ", Formatter.outX("existing.compliance.of.this.is.clear"));
        assertEquals("Any Observation For This Is Clear ", Formatter.outX("any.observation.for.this.is.clear"));
        assertEquals("This Type Of ",
            Formatter.outX("existing.compliance.for.this.type.of.observation.continues"));
        assertEquals("Of This Is Clear As The Observation Continues ",
            Formatter.outX("existing.compliance.of.this.is.clear.as.the.observation.continues"));
        assertEquals("", Formatter.outX(""));
    }


}
