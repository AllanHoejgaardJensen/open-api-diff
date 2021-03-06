# Text Report

The [complete report](https://bitbucket.org/ahjensen/open-api-diff/src/65abc91a53668c8b771d020d2230610f1c1dc40f/sample-reports/APIDIFF-TXT.txt?at=master&fileviewer=file-view-default) is available in the git repo.

A couple of screenshots from the report is show underneath.
A with a bit of explanation to each of the screenshots will follow:

## The Introduction and Quick Overview

![Console Intro and Overview](../sample-reports/CONSOLE-IntroAndOverview.png)

The introductory part of the report is to state what is going to be in the report and how the notation of the report
should be understood. The Overview section gives a highlevel overview over what has happened as "The Big Picture" perspective.

## Elaboration on Changed Parts

![Console Elaborated Change](../sample-reports/CONSOLE-Elaborated-Change.png)

The elaborated section gives a more detailed overview over what has happened and what is the issues found that leads to 
breaking situations that will disturb the clients (user of the service) and the potentially breaking situations, that 
may disturb the clients. Furthermore it will state opinionated claims on what would be convenient to include in the existing 
API in order to make the transition towards the future API easier for clients and thus allow for the maximum development
and deployment speed for the service developers.

## Elaboration on Opinionated Compliance

![Console Elaborated Compliance](../sample-reports/CONSOLE-Elaborated-Compliance.png)

The elaborated section including every observation found to be of interest in relation to compliance to the opinionated 
view on APIs that should be easy to evolve. 
The observations are breaking, potentially breaking, changes and suggested improvements to the service design as well as 
details for the headers, parameters, properties, content-types, responses.

![Text Intro and Overview](../sample-reports/TXT-IntroAndOverview.png)

Will display the same as the console part, with the console coloring filtered.
