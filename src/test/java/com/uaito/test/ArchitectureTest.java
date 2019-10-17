package com.uaito.test;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import javax.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.uaito")
public class ArchitectureTest {

    @ArchTest
    static final ArchRule Repository =
            classes().that().haveNameMatching(".*Repository").should().resideInAPackage("..repository..")
                    .as("REPOSITORY should reside in a package '..repository..'");

    @ArchTest
    static final ArchRule Comparator =
            classes().that().haveNameMatching(".*Comparator").should().resideInAPackage("..comparator..")
                    .as("Comparator should reside in a package '..comparator..'");

    @ArchTest
    static final ArchRule Controller =
            classes().that().haveNameMatching(".*Controller").should().resideInAPackage("..controllers..")
                    .as("Controller should reside in a package '..controller..'");

    @ArchTest
    static final ArchRule Enum =
            classes().that().haveNameMatching(".*Enum").should().resideInAPackage("..enuns..")
                    .as("Enum should reside in a package '..enuns..'");

    @ArchTest
    static final ArchRule Request =
            classes().that().haveNameMatching(".*Request").should().resideInAPackage("..request..")
                    .as("Request should reside in a package '..request..'");

    @ArchTest
    static final ArchRule Response =
            classes().that().haveNameMatching(".*Response").should().resideInAPackage("..response..")
                    .as("Response should reside in a package '..response..'");

    @ArchTest
    static final ArchRule Service =
            classes().that().haveNameMatching(".*Service").should().resideInAPackage("..service..")
                    .as("Service should reside in a package '..service..'");

    @ArchTest
    static final ArchRule Util =
            classes().that().haveNameMatching(".*Util").should().resideInAPackage("..util..")
                    .as("Util should reside in a package '..util..'");

    @ArchTest
    static final ArchRule entities_must_reside_in_a_domain_package =
            classes().that().areAnnotatedWith(Entity.class).should().resideInAPackage("..domain..")
                    .as("Entities should reside in a package '..domain..'");

    @ArchTest
    static final ArchRule only_Services_may_use_the_repository =
            classes().that().resideInAPackage("..repository..")
                    .should().onlyBeAccessed().byAnyPackage( "..service..");

    @ArchTest final ArchRule Layer =
            layeredArchitecture()
                    .layer("Controller").definedBy("..controllers..")
                    .layer("Service").definedBy("..service..")
                    .layer("Repository").definedBy("..repository..")
                    .layer("Comparator").definedBy("..comparator..")
                    .layer("Util").definedBy("..util..")
                    .layer("Test").definedBy("..test..")

                    .whereLayer("Controller").mayOnlyBeAccessedByLayers("Test")
                    .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service", "Comparator", "Util", "Test")
                    .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service", "Test")
            ;

}
