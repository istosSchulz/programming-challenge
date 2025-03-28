/**
 * model is hidden from the outside of the module and only accessible by the
 * service which poses the public interface into the module via its public
 * methods. Due to the lack of DI environment all constructors are public.
 */

/**
 * Module containing all the logic to solve the for the country challenge. The
 * model is hidden from the outside of the module and only accessible by the
 * service which poses the public interface into the module via its public
 * methods.
 *
 * Deliberate implementation choices were:
 * <ul>
 * <li>All classes reside in base package level of the module due to current
 * simplicity of the task to be solved.</li>
 * <li>All constructors of {@link de.bcxp.challenge.country.CountryService} are
 * public due to the lack of DI environment.</li>
 * <li>{@link de.bcxp.challenge.country.CountryService} is the implementation,
 * not an interface. like it would usually be in DI-environments or modulithic
 * approaches. Skipped this path due to simplicity of given task to achieve
 * since there's only one point using the class and it has to know it's
 * implementation anyway due to the lack of DI-environment.</li>
 * <li>Option to provide custom input file location/name was considered but not
 * used.</li>
 * <li>All possible fields in {@link CountryModel} are included although being
 * optional and not used.</li>
 * <li>Initial data import or manual data import opposed to implicit data import
 * in
 * {@link de.bcxp.challenge.country.CountryService#countryWithHighestPopulationDensity()}
 * was considered by not implemented.</li>
 * </ul>
 *
 */
package de.bcxp.challenge.country;