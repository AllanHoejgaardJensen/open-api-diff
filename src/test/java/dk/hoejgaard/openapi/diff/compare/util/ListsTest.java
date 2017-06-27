package dk.hoejgaard.openapi.diff.compare.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


import static org.junit.Assert.*;

public class ListsTest {

    @Test
    public void testNullLists() {
        Lists<String> diff = Lists.diff(null, null);
        assertNotNull(diff);
    }

    @Test
    public void testNullReferenceList() {
        Lists<String> diff = Lists.diff(null, new ArrayList<>());
        assertNotNull(diff);
        assertTrue(diff.getRemoved().isEmpty());
        assertTrue(diff.getAdded().isEmpty());
        assertTrue(diff.getCommon().isEmpty());
    }

    @Test
    public void testNullReferenceListWithCandidateList() {
        List<String> candidateSet = new ArrayList<>();
        String commonStrA = "A";
        candidateSet.add(commonStrA);
        String commonStrB = "B";
        candidateSet.add(commonStrB);
        String commonStrC = "C";
        candidateSet.add(commonStrC);
        Lists<String> diff = Lists.diff(null, candidateSet);
        assertNotNull(diff);
        assertTrue(diff.getRemoved().isEmpty());
        assertEquals(3, diff.getAdded().size());
        assertTrue(diff.getCommon().isEmpty());
    }

    @Test
    public void testReferenceListWithNullCandidateList() {
        List<String> referenceSet = new ArrayList<>();
        String commonStrA = "A";
        referenceSet.add(commonStrA);
        String commonStrB = "B";
        referenceSet.add(commonStrB);
        String commonStrC = "C";
        referenceSet.add(commonStrC);
        Lists<String> diff = Lists.diff(referenceSet, null);
        assertNotNull(diff);
        assertEquals(3, diff.getRemoved().size());
        assertTrue(diff.getAdded().isEmpty());
        assertTrue(diff.getCommon().isEmpty());
    }

    @Test
    public void testEmptyNewList() {
        List<String> referenceSet = new ArrayList<>();
        List<String> candidateSet = new ArrayList<>();
        String commonStrA = "Common A";
        referenceSet.add(commonStrA);
        String commonStrB = "Common B";
        referenceSet.add(commonStrB);
        String commonStrC = "Common C";
        referenceSet.add(commonStrC);
        Lists<String> diff = Lists.diff(referenceSet, candidateSet);
        assertTrue(diff.getCommon().isEmpty());
        assertTrue(diff.getAdded().isEmpty());
        assertEquals(3, diff.getRemoved().size());
    }

    @Test
    public void testEmptyOldList() {
        List<String> referenceSet = new ArrayList<>();
        List<String> candidateSet = new ArrayList<>();
        String commonStrA = "New A";
        candidateSet.add(commonStrA);
        String commonStrB = "New B";
        candidateSet.add(commonStrB);
        String commonStrC = "New C";
        candidateSet.add(commonStrC);
        Lists<String> diff = Lists.diff(referenceSet, candidateSet);
        assertTrue(diff.getCommon().isEmpty());
        assertEquals(3, diff.getAdded().size());
        assertTrue(diff.getRemoved().isEmpty());
    }

    @Test
    public void testIdenticalList() {
        List<String> referenceSet = new ArrayList<>();
        List<String> candidateSet = new ArrayList<>();
        String commonStrA = "Common String A";
        referenceSet.add(commonStrA);
        candidateSet.add(commonStrA);
        String commonStrB = "Common String B";
        referenceSet.add(commonStrB);
        candidateSet.add(commonStrB);
        String commonStrC = "Common String C";
        referenceSet.add(commonStrC);
        candidateSet.add(commonStrC);
        Lists<String> diff = Lists.diff(referenceSet, candidateSet);
        assertEquals(3, diff.getCommon().size());
        assertTrue(diff.getAdded().isEmpty());
        assertTrue(diff.getRemoved().isEmpty());
    }

    @Test
    public void testTwoCommonOneAddedList() {
        List<String> referenceSet = new ArrayList<>();
        List<String> candidateSet = new ArrayList<>();
        String commonStrA = "Common A";
        referenceSet.add(commonStrA);
        candidateSet.add(commonStrA);
        String commonStrB = "Common B";
        referenceSet.add(commonStrB);
        candidateSet.add(commonStrB);
        String strC = "New C";
        candidateSet.add(strC);
        Lists<String> diff = Lists.diff(referenceSet, candidateSet);
        assertEquals(2, diff.getCommon().size());
        assertEquals(1, diff.getAdded().size());
        assertTrue(diff.getRemoved().isEmpty());
    }

    @Test
    public void testOneCommonTwoAddedList() {
        List<String> referenceSet = new ArrayList<>();
        List<String> candidateSet = new ArrayList<>();
        String commonStrA = "Common A";
        referenceSet.add(commonStrA);
        candidateSet.add(commonStrA);
        String strB = "New B";
        candidateSet.add(strB);
        String strC = "New C";
        candidateSet.add(strC);
        Lists<String> diff = Lists.diff(referenceSet, candidateSet);
        assertEquals(1, diff.getCommon().size());
        assertEquals(2, diff.getAdded().size());
        assertTrue(diff.getRemoved().isEmpty());
    }

    @Test
    public void testOneRemovedFromList() {
        List<String> referenceSet = new ArrayList<>();
        List<String> candidateSet = new ArrayList<>();
        String commonStrA = "Old A";
        referenceSet.add(commonStrA);
        String strB = "New B";
        candidateSet.add(strB);
        String strC = "New C";
        candidateSet.add(strC);
        Lists<String> diff = Lists.diff(referenceSet, candidateSet);
        assertTrue(diff.getCommon().isEmpty());
        assertEquals(2, diff.getAdded().size());
        assertEquals(1, diff.getRemoved().size());
    }
}
