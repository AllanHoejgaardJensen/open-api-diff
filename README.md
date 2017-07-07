# Open API diff
<img align="left" src="logo.png">
Open API Diff is a tool that compares two APIs and looks for differences that will break the backwards compliance of 
the API. The idea is an existing API is compared to the coming version of the same service.
The tool uses the client (the users of the service) perspective in order to keep the users of the API and thus 
the focus is on enabling change of the API (through new versions) in increments that allows the clients to follow.
There is more information on tooling, how to use it and how to build this project, as well as information on what 
types of information the tool can report and finally there is a part on the various opinionated parts of the tool.

 *It currently supports version 2, a version 3 is on the agenda - if the tool is found useful.*
 
  [![Build status](https://travis-ci.org/AllanHoejgaardJensen/open-api-diff.svg?branch=master)](https://travis-ci.org/AllanHoejgaardJensen/open-api-diff)
  [![Coverage Status](https://codecov.io/gh/AllanHoejgaardJensen/open-api-diff/coverage.svg?branch=master)](https://codecov.io/gh/AllanHoejgaardJensen/open-api-diff)

## Value proposal
The value proposal is to make a tool that helps service developer to keep on evolving their services in the highest 
possible pace. If a service is used by many different clients it is important that they can continue to operate and 
create value for the users (and that creates value at the service as well). If the service API cannot be evolved 
because one or more clients are having trouble working with new versions either every version is a completely new 
service or the existing service has some built in mechanisms that helps the client developers to cope with change and 
offers then a easy and swift fall-back. This is the goal of this tool.
 
## Introduction
The Open API diff tool compares 2 APIs (an existing API and the future API). The two APIs can be analyzed according to
a certain `depth` a certain `maturity` and something called `version`s (see below). 

In other words the tool is aimed at comparing an existing API, with a future API and deliver results on 
breaking changes, potentially breaking changes etc. that could make clients (service users) unhappy. 

### Reports

The tool comprises currently 3 different reports:

 * [HTML report](md/html-report.md)
 * [MarkDown report](md/markdown-report.md)
 * [A Console Report (including text based report)](md/text-report.md)
  
The Reports will contain information based on the `depth`, `maturity` and `version` settings. The most elaborate of the 
reports will list the following sections:

#### Endpoint overview for:
 * added, 
 * removed and 
 * changed resources

#### Elaborate Endpoint Changes:
 * breaking stuff
 * potentially breaking stuff

The report will show breaking and potentially breaking observations that occurs as a result of a change from the 
existing API to the coming API, but it will also include suggested improvements to the existing in order to be able 
to go from the existing towards a new future version of the service with the least possible pain for the client.
Basically these suggestions could be considered as candidates for a gradual implementation in the existing API and 
be a part of the existing API before moving to the future API. The reasoning behind that is that clients needs to 
incorporate some of e.g. the response codes into their programming model in order to react easily to API evolution.
  
#### Elaborate Compliance of Endpoints:
 * breaking stuff
 * potentially breaking stuff
 * changes
 
 * improvements to existing API
 * changes to content-types
 * changes to parameters
 * changes to properties
 * changes to responses

The report will show breaking and potentially breaking observations that occurs as a result of a change from the 
existing API to the coming API and includes suggested improvements to the existing in order to be able 
to go from the existing towards a new future version of the service with the least possible pain for the client.
Basically these suggestions could be considered as candidates for a gradual implementation in the existing API and 
be a part of the existing API before moving to the future API. The reasoning behind that is that clients needs to 
incorporate some of e.g. the response codes into their programming model in order to react easily to API evolution.

## Settings
The tool can be used with different settings, the level of depth in the analysis can be set and so can the maturity 
and versions. 

### The depth can be set into:
#### depth
* `all` everything that can be checked upon is included in the report, this together with maturity `full` will give the most elaborate report.
* `breaking` this will only report breaking changes
* `potentially breaking` this will report breaking and potentially breaking changes
* `laissez-faire` this will report changes at the endpoint level

###The maturity can be set into:
#### maturity
  * `full` this is the most elaborated check 
  * `low`  this is the most relaxed check and usually used together with `laissez-faire` depth
  
Using the `full` maturity gives feedback at Richardson maturity model highest level for opinionated REST services 
using HAL (see section on the opinionated parts below)

## Building the Open API diff project
The Open API Diff project is maven based and thus it can be build by using: 
 
    mvn verify  
    
This will compile and run tests (unit-, integration- and verification-tests)
More information on the thoughts on the structure etc. in the [project](md/project.md) 

## Using the Open API diff tool

Use with different levels of how to work with the opinionated stuff such as 
the diff-level, maturity level, etc. can be found in [the usage information](md/tool-cli-usage.md)

## Reporting

The reports are made in a first rudimentary form and it is hoped that the use of the tool will lead to reports that are 
taylormade to the needs that hopefully is there for service developers to be able to determine in an easy and predictable 
way whether the coming API is backward compliant or not and whether some clients may see the new version breaking stuff
in the application using the service and thus providing its users with a bad experience.

### Existing Report Examples

Currently 3 reports are supported ([HTML](md/html-report.md), [MarkDown](md/markdown-report.md), [TXT](md/text-report.md)).

## Findings 
[The findings will be shown and exemplified by the use of the HTML based report](md/findings-report.md) in order to 
show something visual along with an explanation of why this finding exist and why it is a part of the report. 

## Future work
The current edition is merely a tool in its early stages, if this is found useful, it could evolve into a more elaborate 
tool. Currently this seems to require some consensus on the opinionated parts in order to get the most value from the 
comparison of APIs. Support for configuration of a response, header, etc. profile, that suits individual needs. 
Support for API capabilities as shown in the [api capabilities](https://github.com/Nykredit/api-capabilities) 
is one of the things under consideration, but first a stable version of the current version needs to be verified by others 
in order to see the potential for the tool.

## Inspiration
The inspiration was found in the existing tools for swagger-diff, and the wish to take responses, headers and 
content-types into the tooling. The implementation took its starting point in the deepoove project on swagger-diff.
A big thanks goes to the swagger-diff people.

The samples from the swagger-diff project has been kept in the project to let people having experiences with the 
swagger-diff project have an easy way to get started with this if they want to use a more opinionated tool. The
diffs done in swagger-diff can be done in this tool tool. Being the opinionated tool it is these chacks are not the 
default ones.