package dk.hoejgaard.openapi.diff.model;

import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContentTypeTest {

    @Test
    public void testContentTypeInstantiationJsonNoProjection() {
        ContentType ct =  new ContentType("application/json");
        assertEquals("application", ct.getType());
        assertEquals("json", ct.getSubtype());
        assertEquals("", ct.getParameters());
        assertEquals("", ct.getProjection());
        assertTrue(ct.isDefaultContentType());
        assertTrue(ct.isJsonConceptCompliant());
        assertFalse(ct.isSchemeCompliant());
        assertEquals("application/json", ct.toString());
        assertTrue(ContentType.isJsonOnly("application/json"));
        assertFalse(ContentType.isJsonOnly("application/hal+json"));

        assertFalse(ContentType.isHALJson("application/json"));
        assertTrue(ContentType.isHALJson("application/hal+json"));
    }

    @Test
    public void testContentTypeInstantiationNoProjection() {
        ContentType ct =  new ContentType("application/hal+json");
        assertEquals("application", ct.getType());
        assertEquals("hal+json", ct.getSubtype());
        assertEquals("", ct.getParameters());
        assertEquals("", ct.getProjection());
        assertTrue(ct.isDefaultContentType());
        assertTrue(ct.isJsonConceptCompliant());
        assertFalse(ct.isSchemeCompliant());
    }

    @Test
    public void testContentTypeInstantiationNoVersion() {
        ContentType ct =  new ContentType("application/hal+json;concept=account");
        assertEquals("application", ct.getType());
        assertEquals("hal+json", ct.getSubtype());
        assertEquals("concept=account", ct.getParameters());
        assertEquals("account", ct.getProjection());
        assertEquals("", ct.getVersion());
        assertFalse(ct.isDefaultContentType());
        assertTrue(ct.isJsonConceptCompliant());
        assertFalse(ct.isSchemeCompliant());
        assertEquals("application/hal+json;concept=account", ct.toString());
    }

    @Test
    public void testContentTypeInstantiation() {
        ContentType ct =  new ContentType("application/hal+json;concept=account;v=3");
        assertEquals("application", ct.getType());
        assertEquals("hal+json", ct.getSubtype());
        assertEquals("account", ct.getProjection());
        assertEquals("concept=account;v=3", ct.getParameters());
        assertEquals("3", ct.getVersion());
        assertFalse(ct.isDefaultContentType());
        assertTrue(ct.isJsonConceptCompliant());
        assertTrue(ct.isSchemeCompliant());
        assertEquals("application/hal+json;concept=account;v=3", ct.toString());
    }

}
