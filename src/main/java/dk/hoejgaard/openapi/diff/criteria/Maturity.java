package dk.hoejgaard.openapi.diff.criteria;

/**
 * Maturity levels for the comparison of APIs
 *
 * <p>
 *  FULL is the most elaborate level of comparison and also the most opinionated
 * <p>
 *  HAL is the HAL edition of HATEOAS in an opinionated way
 * <p>
 *  HATEOAS is practically the HAL edition of HATEOAS in an opinionated way
 * <p>
 *  LOW is the minimal level
 * <p>
 *  NONE is the lowest possible level
 *
 */
public enum Maturity {
    NONE,
    LOW,
    HATEOAS,
    HAL,
    FULL
}
