package dk.hoejgaard.openapi.diff.compare.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;


import static org.junit.Assert.*;

public class MapsTest {

    @Test
    public void testNullMaps() {
        Maps<String, String> diff = Maps.diff(null, null);
        assertNotNull(diff);
    }

    @Test
    public void testNullReferenceList() {
        Maps<String, String> diff = Maps.diff(null, new LinkedHashMap<>());
        assertNotNull(diff);
        assertEquals(0, diff.getRemoved().size());
        assertEquals(0, diff.getAdded().size());
        assertEquals(0, diff.getCommon().size());
    }

    @Test
    public void testNullReferenceListWithCandidateList() {
        Map<String, String> candidateSet = new LinkedHashMap<>();
        String commonStrA = "A";
        candidateSet.put(commonStrA, commonStrA);
        String commonStrB = "B";
        candidateSet.put(commonStrB, commonStrB);
        String commonStrC = "C";
        candidateSet.put(commonStrC, commonStrC);
        Maps<String, String> diff = Maps.diff(null, candidateSet);
        assertNotNull(diff);
        assertEquals(0, diff.getRemoved().size());
        assertEquals(3, diff.getAdded().size());
        assertEquals(0, diff.getCommon().size());
    }

    @Test
    public void testReferenceListWithNullCandidateList() {
        Map<String, String> referenceSet = new LinkedHashMap<>();
        String commonStrA = "A";
        referenceSet.put(commonStrA, commonStrA);
        String commonStrB = "B";
        referenceSet.put(commonStrB, commonStrB);
        String commonStrC = "C";
        referenceSet.put(commonStrC, commonStrC);
        Maps<String, String> diff = Maps.diff(referenceSet, null);
        assertNotNull(diff);
        assertEquals(3, diff.getRemoved().size());
        assertEquals(0, diff.getAdded().size());
        assertEquals(0, diff.getCommon().size());
    }

    @Test
    public void testEmptyNewList() {
        Map<String, String> referenceSet = new LinkedHashMap<>();
        Map<String, String> candidateSet = new LinkedHashMap<>();
        String commonStrA = "Common A";
        referenceSet.put(commonStrA, commonStrA);
        String commonStrB = "Common B";
        referenceSet.put(commonStrB, commonStrB);
        String commonStrC = "Common C";
        referenceSet.put(commonStrC, commonStrC);
        Maps<String, String> diff = Maps.diff(referenceSet, candidateSet);
        assertEquals(0, diff.getCommon().size());
        assertEquals(0, diff.getAdded().size());
        assertEquals(3, diff.getRemoved().size());
    }

    @Test
    public void testEmptyOldList() {
        Map<String, String> referenceSet = new LinkedHashMap<>();
        Map<String, String> candidateSet = new LinkedHashMap<>();
        String commonStrA = "New A";
        candidateSet.put(commonStrA, commonStrA);
        String commonStrB = "New B";
        candidateSet.put(commonStrB, commonStrB);
        String commonStrC = "New C";
        candidateSet.put(commonStrC, commonStrC);
        Maps<String, String> diff = Maps.diff(referenceSet, candidateSet);
        assertEquals(0, diff.getCommon().size());
        assertEquals(3, diff.getAdded().size());
        assertEquals(0, diff.getRemoved().size());
    }

    @Test
    public void testIdenticalList() {
        Map<String, String> referenceSet = new LinkedHashMap<>();
        Map<String, String> candidateSet = new LinkedHashMap<>();
        String commonStrA = "Common String A";
        referenceSet.put(commonStrA, commonStrA);
        candidateSet.put(commonStrA, commonStrA);
        String commonStrB = "Common String B";
        referenceSet.put(commonStrB, commonStrB);
        candidateSet.put(commonStrB, commonStrB);
        String commonStrC = "Common String C";
        referenceSet.put(commonStrC, commonStrC);
        candidateSet.put(commonStrC, commonStrC);
        Maps<String, String> diff = Maps.diff(referenceSet, candidateSet);
        assertEquals(3, diff.getCommon().size());
        assertTrue(diff.getAdded().isEmpty());
        assertTrue(diff.getRemoved().isEmpty());
    }

    @Test
    public void testTwoCommonOneAddedList() {
        Map<String, String> referenceSet = new LinkedHashMap<>();
        Map<String, String> candidateSet = new LinkedHashMap<>();
        String commonStrA = "Common A";
        referenceSet.put(commonStrA, commonStrA);
        candidateSet.put(commonStrA, commonStrA);
        String commonStrB = "Common B";
        referenceSet.put(commonStrB, commonStrB);
        candidateSet.put(commonStrB, commonStrB);
        String strC = "New C";
        candidateSet.put(strC, strC);
        Maps<String, String> diff = Maps.diff(referenceSet, candidateSet);
        assertEquals(2, diff.getCommon().size());
        assertEquals(1, diff.getAdded().size());
        assertTrue(diff.getRemoved().isEmpty());
    }

    @Test
    public void testOneCommonTwoAddedList() {
        Map<String, String> referenceSet = new LinkedHashMap<>();
        Map<String, String> candidateSet = new LinkedHashMap<>();
        String commonStrA = "Common A";
        referenceSet.put(commonStrA, commonStrA);
        candidateSet.put(commonStrA, commonStrA);
        String strB = "New B";
        candidateSet.put(strB, strB);
        String strC = "New C";
        candidateSet.put(strC, strC);
        Maps<String, String> diff = Maps.diff(referenceSet, candidateSet);
        assertEquals(1, diff.getCommon().size());
        assertEquals(2, diff.getAdded().size());
        assertTrue(diff.getRemoved().isEmpty());
    }

    @Test
    public void testOneRemovedFromList() {
        Map<String, String> referenceSet = new LinkedHashMap<>();
        Map<String, String> candidateSet = new LinkedHashMap<>();
        String commonStrA = "Old A";
        referenceSet.put(commonStrA, commonStrA);
        String strB = "New B";
        candidateSet.put(strB, strB);
        String strC = "New C";
        candidateSet.put(strC, strC);
        Maps<String, String> diff = Maps.diff(referenceSet, candidateSet);
        assertEquals(0, diff.getCommon().size());
        assertEquals(2, diff.getAdded().size());
        assertEquals(1, diff.getRemoved().size());
    }
}
