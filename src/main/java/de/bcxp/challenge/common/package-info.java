/**
 * This module contains commonly shared functions and cross-cutting concerns
 * housing the {@link de.bcxp.challenge.common.DataParser} to be implemented by
 * any class providing its own import technique for domain data.
 *
 * Deliberate implementation choices were:
 * <ul>
 * <li>{@link com.fasterxml.jackson.dataformat.csv.CsvParser} being the only
 * implementation of {@link de.bcxp.challenge.common.DataParser} is public and
 * createable, which contradicts modulithic approach, due to the lack of
 * DI-environment and auto-creation mechanism of instances.</li>
 * <li>Ideas for decoupling other data import techniques were considered but not
 * fully implemented due to the lack of DI-environment and bean discovery
 * mechanism.</li>
 * <li>{@link com.fasterxml.jackson.dataformat.csv.CsvParser} was explicitly not
 * covered by unit tests since it's almost completely 3rd party function. Should
 * be covered by integration tests (or higher) which weren't implemented.</li>
 * <li>Caching of ObjectReader for each doimin (within
 * {@link de.bcxp.challenge.common.CSVDataParser} was implemented although being
 * obsolete for this usecase. Would make more sense if there were more than just
 * one feature per problem domain or if there were services aggregating several
 * problem domains.</li>
 * <li>{@link com.fasterxml.jackson.dataformat.csv.CsvParser} doesn't hide all
 * the implementation details from the outside yet but due to complete task
 * having being time boxed this was considered bearable.</li>
 * </ul>
 *
 */
package de.bcxp.challenge.common;