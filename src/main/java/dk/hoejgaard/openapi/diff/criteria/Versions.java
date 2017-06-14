package dk.hoejgaard.openapi.diff.criteria;

/**
 * Defines the overlapping number of versions in the content versioning scheme
 *
 *   <p>
 * The versions are e.g.
 *   <p>
 *    application/hal+json
 *   <p>
 *    application/hal+json;concept=account;v=1
 *   <p>
 *    application/hal+json;concept=account;v=2
 *   <p>
 *    application/hal+json;concept=account;v=3
 *   <p>
 *    application/hal+json;concept=account;v=4
 *   <p>
 *
 *    if these 4 (+1 - the default version) versions are part of the producer for the projection
 *    accounts that meets the requirements for SINGLE, DOUBLE and TRIPLE
 *
 *   <p>
 * The versions are e.g.
 *   <p>
 *    application/hal+json
 *   <p>
 *    application/hal+json;concept=account;v=1
 *   <p>
 *    application/hal+json;concept=account;v=2
 *   <p>
 *    application/hal+json;concept=account;v=3
 *   <p>
 *
 *    if these 3 (+1) versions are part of the producer for the projection
 *    accounts that meets the requirements for SINGLE, DOUBLE but not TRIPLE
 *   <p>
 *
 *
 *
 * The versions are e.g.
 *   <p>
 *    application/hal+json
 *   <p>
 *    application/hal+json;concept=account;v=1
 *   <p>
 *    application/hal+json;concept=account;v=2
 *   <p>
 *
 *    if these 2 (+1) versions are part of the producer for the projection
 *    accounts that meets the requirements for SINGLE, but not for DOUBLE and TRIPLE
 *
 *   <p>
 *
 * The versions are e.g.
 *  <p>
 *    application/hal+json
 *   <p>
 *    application/hal+json;concept=account;v=1
 *   <p>
 *    if this version (+1) are part of the producer for the projection
 *    accounts that meets the requirements for NONE, but not for SINGLE, DOUBLE and TRIPLE
 *
 */
public enum Versions {
    NONE,
    SINGLE,
    DOUBLE,
    TRIPLE
}
