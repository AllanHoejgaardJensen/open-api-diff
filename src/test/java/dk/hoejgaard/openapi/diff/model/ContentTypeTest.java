package dk.hoejgaard.openapi.diff.model;

import dk.hoejgaard.openapi.diff.model.ContentType;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class ContentTypeTest {

    @Test
    public void testContentTypeInstantiationNoProjection() {
        ContentType ct =  new ContentType("application/hal+json");
        assertEquals("application", ct.getType());
        assertEquals("hal+json", ct.getSubtype());
        assertEquals("", ct.getParameters());
        assertEquals("", ct.getProjection());
    }

    @Test
    public void testContentTypeInstantiationNoVersion() {
        ContentType ct =  new ContentType("application/hal+json;concept=account");
        assertEquals("application", ct.getType());
        assertEquals("hal+json", ct.getSubtype());
        assertEquals("concept=account", ct.getParameters());
        assertEquals("account", ct.getProjection());
        assertEquals("", ct.getVersion());
    }

    @Test
    public void testContentTypeInstantiation() {
        ContentType ct =  new ContentType("application/hal+json;concept=account;v=3");
        assertEquals("application", ct.getType());
        assertEquals("hal+json", ct.getSubtype());
        assertEquals("account", ct.getProjection());
        assertEquals("concept=account;v=3", ct.getParameters());
        assertEquals("3", ct.getVersion());
    }

}
