package dk.hoejgaard.openapi.diff.criteria;

/**
 * Defines the level of thoroughness used in the comparison between APIs
 */
public enum Diff {
        LAISSEZ_FAIRE,
        POTENTIALLY_BREAKING,
        BREAKING,
        ALL
}
