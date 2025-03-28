/**
 * The root package for the data challenge containing the entry point
 * {@link de.bcxp.challenge.App} and containing the resource files. Please
 * create and document your own package structure appropriate to your design.
 *
 * Deliberate design choices:
 * <ul>
 * <li>Favored modulithic cut of the problem domains (weather & countries) and
 * cross-cutting concerns over layered approach with technical cuts.</li>
 * <li>Jackson CSV for input data binding to by type-safe and independent of
 * column ordering within CSV file.</li>
 * <li>Not using a proper Logger instead going with System.out & System.err due
 * to simplicity of the task at hand.</li>
 * <li>Skip broken import entries (instead of aborting the complete import) and
 * swallow general import errors to map them to a single "error" result with the
 * attempt to reduce additional complexity by sacrificing maintainability &
 * extensibility.</li>
 * <li>Using Mocking to mock classes in tests in order to make tests more
 * readable.</li>
 * <li>Using Project Lombok to reduce boiler plate code.</li>
 * <li>Relying on a rather static CSV schema (e.g. header has to be present,
 * weather.day has to be of type int) instead of making it configurable to
 * reduce additional complexity.</li>
 * </ul>
 */
package de.bcxp.challenge;