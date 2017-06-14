# Open API diff
Open API Diff is a tool that compares two APIs and looks for differences that will break the backwards compliance of 
the API. The idea is an existing API is compared to the coming version of the same service.
The tool uses the client (the users of the service) perspective in order to keep the users of the API and thus 
the focus is on enabling change of the API (through new versions) in increments that allows the clients to follow.
There is more information on tooling, how to use it and how to build this project, as well as information on what 
types of information the tool can report and finally there is a part on the various opinionated parts of the tool.

 *It currently supports version 2, a version 3 is on the agenda - if the tool is found useful.*
 
  ![status](https://travis-ci.org/AllanHoejgaardJensen/open-api-diff.svg?branch=master) 

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

 * HTML report
 * MarkDown report
 * a Console Report (including text based report)
  
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
More information on the thoughts on the structure etc. in the [project](project.md) 

## Using the Open API diff tool

Use with different levels of how to work with the opinionated stuff such as 
the diff-level, maturity level, etc.

The absolute most simple way to get started, is issuing the following command after building the project:

     java -Djava.ext.dirs=open-api-diff/sample-api:lib 
          -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff
      
That is equivalent to:

    java -Djava.ext.dirs=open-api-diff/sample-api:lib 
         -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff 
             ./sample-api/elaborate_example_v1.json 
             ./sample-api/elaborate_example_v3f.json 
             ./target/output/reports 
             APIDiff.txt 
             all full 1
      
That is equivalent to:

    java -Djava.ext.dirs=open-api-diff/sample-api:lib 
         -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff 
             ./sample-api/elaborate_example_v1.json 
             ./sample-api/elaborate_example_v3f.json 
             ./target/output/reports 
             APIDiff.txt 
             a f 1

The syntax is (with no line breaks):

    java -Djava.ext.dirs=open-api-diff/sample-api:lib 
         -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff
             [existing API = ./sample-api/elaborate_example_v1.json] 
             [future API = ./sample-api/elaborate_example_v3f.json] 
             [target report folder = ./target/output/reports] 
             [report filename and format = APIDiff.txt]
             [diff depth = a/all] [maturity = f/full] [versions = 1]
      `

If you want a Markdown report (use the .md file extension):

    java -Djava.ext.dirs=open-api-diff/sample-api:lib 
         -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff 
             ./sample-api/elaborate_example_v1.json 
             ./sample-api/elaborate_example_v3f.json 
             ./target/output/reports 
             APIDiff.md
             a f 1

If you want a HTML report (use the .html file extension):

    java -Djava.ext.dirs=open-api-diff/sample-api:lib 
         -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff 
             ./sample-api/elaborate_example_v1.json 
             ./sample-api/elaborate_example_v3f.json
             ./target/output/reports
             APIDiff.html
             a f 1

## Reporting

The reports are made in a first rudimentary form and it is hoped that the use of the tool will lead to reports that are 
taylormade to the needs that hopefully is there for service developers to be able to determine in an easy and predictable 
way whether the coming API is backward compliant or not and whether some clients may see the new version breaking stuff
in the application using the service and thus providing its users with a bad experience.

### Existing Report Examples

Currently 3 reports are supported (HTML, MD, TXT).

##### HTML Report
The [complete report (although not directly viewable)](https://bitbucket.org/ahjensen/open-api-diff/src/65abc91a53668c8b771d020d2230610f1c1dc40f/sample-reports/APIDIFF-HTML.html?at=master&fileviewer=file-view-default) is available in the git repo.
A couple of screenshots from the report is show underneath.
A with a bit of explanation to each of the screenshots will follow:

##### Introduction

![HTML Intro] (sample-reports/HTML-Intro.png)

The introductory part of the report is to state what is going to be in the report and how the notation of the report
should be understood.

##### The Quick Overview

![HTML Overview] (sample-reports/HTML-Overview.png)

The overview section gives a highlevel overview over what has happened as "The Big Picture" perspective

##### Elaboration on Changed Parts

![HTML Elaborated Change] (sample-reports/HTML-Elaborated-Change.png)

The elaborated section gives a more detailed overview over what has happened and what is the issues found that leads to 
breaking situations that will disturb the clients (user of the service) and the potentially breaking situations, that 
may disturb the clients. Furthermore it will state opinionated claims on what would be convenient to include in the existing 
API in order to make the transition towards the future API easier for clients and thus allow for the maximum development
and deployment speed for the service developers.

##### Elaboration on Opinionated Compliance

![HTML Elaborated Compliance] (sample-reports/HTML-Elaborated-Compliance.png)

The elaborated section including every observation found to be of interest in relation to compliance to the opinionated 
view on APIs that should be easy to evolve. 
The observations are breaking, potentially breaking, changes and suggested improvements to the service design as well as 
details for the headers, parameters, properties, content-types, responses.

##### Markdown Report

The [complete report](https://bitbucket.org/ahjensen/open-api-diff/src/65abc91a53668c8b771d020d2230610f1c1dc40f/sample-reports/APIDIFF-MD.md?at=master&fileviewer=file-view-default) is available in the git repo.
A couple of screenshots from the report is show underneath.
A with a bit of explanation to each of the screenshots will follow:

##### The Introduction and Quick Overview

![Markdown Intro and Overview] (sample-reports/MD-Intro.png)

The introductory part of the report is to state what is going to be in the report and how the notation of the report
should be understood. The Overview section gives a highlevel overview over what has happened as "The Big Picture" perspective.

##### Elaboration on Changed Parts

![Markdown Elaborated Change] (sample-reports/MD-Elaborated-Change.png)

The elaborated section gives a more detailed overview over what has happened and what is the issues found that leads to 
breaking situations that will disturb the clients (user of the service) and the potentially breaking situations, that 
may disturb the clients. Furthermore it will state opinionated claims on what would be convenient to include in the existing 
API in order to make the transition towards the future API easier for clients and thus allow for the maximum development
and deployment speed for the service developers.

##### Elaboration on Opinionated Compliance

![Markdown Elaborated Compliance] (sample-reports/MD-Elaborated-Compliance.png)

The elaborated section including every observation found to be of interest in relation to compliance to the opinionated 
view on APIs that should be easy to evolve. 
The observations are breaking, potentially breaking, changes and suggested improvements to the service design as well as 
details for the headers, parameters, properties, content-types, responses.

##### Text Report

The [complete report](https://bitbucket.org/ahjensen/open-api-diff/src/65abc91a53668c8b771d020d2230610f1c1dc40f/sample-reports/APIDIFF-TXT.txt?at=master&fileviewer=file-view-default) is available in the git repo.

A couple of screenshots from the report is show underneath.
A with a bit of explanation to each of the screenshots will follow:

##### The Introduction and Quick Overview

![Console Intro and Overview] (sample-reports/CONSOLE-IntroAndOverview.png)

The introductory part of the report is to state what is going to be in the report and how the notation of the report
should be understood. The Overview section gives a highlevel overview over what has happened as "The Big Picture" perspective.

##### Elaboration on Changed Parts

![Console Elaborated Change] (sample-reports/CONSOLE-Elaborated-Change.png)

The elaborated section gives a more detailed overview over what has happened and what is the issues found that leads to 
breaking situations that will disturb the clients (user of the service) and the potentially breaking situations, that 
may disturb the clients. Furthermore it will state opinionated claims on what would be convenient to include in the existing 
API in order to make the transition towards the future API easier for clients and thus allow for the maximum development
and deployment speed for the service developers.

##### Elaboration on Opinionated Compliance

![Console Elaborated Compliance] (sample-reports/CONSOLE-Elaborated-Compliance.png)

The elaborated section including every observation found to be of interest in relation to compliance to the opinionated 
view on APIs that should be easy to evolve. 
The observations are breaking, potentially breaking, changes and suggested improvements to the service design as well as 
details for the headers, parameters, properties, content-types, responses.

![Text Intro and Overview] (sample-reports/TXT-IntroAndOverview.png)

Will display the same as the console part, with the console coloring filtered.

## Findings 
The findings will be shown and exemplified by the use of the HTML based report in order to show something visual along 
with an explanation of why this finding exist and why it is a part of the report.

### The section will be extended little after little.

### Examples (Opinionated)
Adding a response like `202` will break the backward compatibility of the API for that endpoint. If clients were not 
considering this response they would not react appropriate to the response. If the clients are used to get either a 
`200` or a `404`, they would not have considered `202` and following the `location` header after waiting the amount of time 
suggested by the `retry-after` header.  That is why this is considered a breaking change.

 ![202 as breaking change to the API] (sample-reports/202_added_breaking.png)
 
The `202` may be a response that services needs to introduce, if e.g. the time it takes to complete the response increases 
over time due to whatever reasons, it could be a creation of information that due to changes in legislation takes longer. 
Having clients waiting for a `200` a long time will be a pain for both the clients and for the service.
Therefore it is wise to consider if you need this response, you do not have to return the response before it is necessary. 
If you do not specify the `202` as a potential response you will have to introduce a new service version header like the 
`X-Service-Generation` and let the clients hang if they ask for the old version.
 
Adding ` (>) ` a `301` or `307` works in the same way as the `202`, if clients were not considering getting either of those 
back, the client would not be ready to react by following the `location` header to the new location of the endpoint.
Adding a `301` and `307` to your API makes it easier to move endpoints and having that as a part of your Open API 
specification may be worth considering.

 ![301 as breaking change to the API] (sample-reports/301_added_good_pot-breaking.png)

 ![307 as breaking change to the API] (sample-reports/307_added_pot-breaking.png)

Having an existing API ` (!) ` where a `301` or `307` is not defined is therefore reported as something you evaluate if yu want 
to include in your API or not.

 ![301 missing in the existing API is a breaking change waiting to happen] (sample-reports/301_missing_pot-break.png)

Having the ability to offer a "service" to clients where they can ask if the need to re-read a resource or whether the 
client still has a valid version of the resource is the reason for including `304` in the potentially breaking part. 
Therefore an existing API without `304` might want to be extended with that, the reason for it being perceived as 
breaking is a scalability perspective as this can allow som services (having resources with a non-realtime character)
to scale higher (or have a lower operational cost for the same load).

 ![304 missing in the existing API is a potential for offloading services] (sample-reports/304_missing_pot-break.png)

The most frequent used responses are probably `200` and `404`, failing to have these as a part of the existing API or the 
new is considered a potential breaking change.

 ![404 missing in the API is a potential break] (sample-reports/404_missing_pot-break.png)
 
If the `200` is added to the new API it needs to include som headers like `X-Log-Token` to enable clients to refer to 
incidents in a way of their choosing and in that way handle bugs etc. more efficient, here the some headers concerning
rate limits are added as required by the service in the new version of the API, which makes a lot of sense, as they are 
required this is considered a breaking change to the new API.

 ![200 missing in the API is a potential break] (sample-reports/200_limit-breaking.png)

If a client sets the `Accept` header to a content-type unknown to the server, some services are responding with a `400`, 
whereas the `415` seems more appropriate for this situation, in order to avoid breaking the API when introducing this 
response code later it seems like a good and sound idea to include this from the start.

 ![415 missing in the existing API is a potential break waiting to happen] (sample-reports/415_missing_pot-break.png)

Including a number of responses from the start seems to worth considering, which is why a number of these are considered 
potential breaks, se below.

 ![429 missing in the existing API is a potential break waiting to happen] (sample-reports/429_missing_pot-break.png)
 
Not having this as a part of your API means you cannot ask client politely to back off for 
a while (specified in the `retry-after`) header

 ![412 missing in the existing API is a more precise way to signal pre-conditional errors to clients] (sample-reports/412_missing_pot-break.png)
 
This is a more specific error in the event of validation errors, in many situations the `400` is used and it may, the 
`412` is a little more precise for pre-conditional validation issues. `400` is considered as one of the mandatory alongside 
with `200` and `404`. It could be argued whether `412` should be mandatory or not, it differentiates itself from the `400` 
  by not having the requirement for the client to not repeat the request.

How long must you remember an endpoint, if you did not have the `301` or `307` specified and clients still uses the older 
endpoints and you know that you want to be able to terminate a given endpoint someday somehow. This is where `410` comes 
into the picture:

 ![410 missing in the existing API is a potential break waiting to happen] (sample-reports/410_missing_pot-break.png)

This is arguable whether this should be a mandatory response in every endpoint in a service or not, it is however worth 
considering whether it is something you need. If not specified, the contracts and terms for bringing an endpoint down 
must be clear. 

The `505` response is making ready for the next version of HTTP, so it is kind of along the lines of the `410`, can you bring 
endpoints down or can you cease the support for a given version of content, of API structure and how to do that using 
HTTP response code and other mechanisms to do it in a way that the clients can understand and follow.

 ![505 missing in the existing API is a potential break waiting to happen] (sample-reports/505_missing_pot-break.png)

The error handling from the services side are usually done by 500 and possibly 503 and thus they must be part of the 
responses that any endpoint can return.

 ![500 missing in the existing API is a potential break waiting to happen] (sample-reports/500_missing_pot-break.png)

 ![503 missing in the existing API is a potential break waiting to happen] (sample-reports/503_missing_pot-break.png)
 
 The `503` is suggested having a `retry-after` header to signal back to the client when the service is expected to be 
 available again, the aim of that is that clients can use this for information to the application user and thus informing 
 the users to prepare the user for this situation the best the client can. 
 
  ![503 retry-after header is required] (sample-reports/503_added_required_header-breaking.png)
  
Adding that header and make it required is a potentially breaking thing as clients may not do anything about the header 
and thus the service downtime (or slow performance) may continue. Again here you could argue that this type of header
breaks nothing and clients will be able to continue regardless of this added header - and you would be right - therefore 
it would be nice to have people participating in finding the right amount of what is breaking, what is potentially 
breaking etc. or create a set of different profiles that allows people to have services that could have a label - 
e.g. with a certified level according to a given Open API Diff profile. 

The response code regarding authorization and access `401` and `403` must be part of any protected API and thus they 
are included in the mandatory set of responses here.

  ![401 missing in the existing API is a potential break waiting to happen] (sample-reports/401_missing_pot-break.png)


The elaborated part of the reports attempt to state a few words on the observation:

  ![The elaborated report on responses] (sample-reports/addedResponsesChange.png)

and 

  ![The elaborated report on responses] (sample-reports/Compliance_Responses_Change.png)


On top of that there are som even more opinionated parts, such as the parts on using content-types as versioning 
mechanism for content in endpoints:

  ![The elaborated report on content types] (sample-reports/Compliance_ContentTypes.png)

  ![The elaborated report on content types projection added] (sample-reports/projectionAdded.png)

  
And concrete observations like: 

  ![The elaborated report on content types] (sample-reports/descriptionExtended-pot-break.png)

  ![The elaborated report on content types] (sample-reports/header_pattern_changed-pot-break.png)

  ![The elaborated report on content types] (sample-reports/ResponseCode_200_changes.png)

  ![The elaborated report on content types] (sample-reports/property_changed_to_required.png)

And:

  ![The elaborated report on header changes] (sample-reports/descriptionExtended-pot-break.png)

  ![The elaborated report on path parameter changes] (sample-reports/projectionAdded.png)
  
And suggested improvements to the existing design - here in the form of suggesting that null is not a good definition 
to have in the API, because it creates uncertainty going forward with your design and the greater the number of client 
the services have the larger the risk for various interpretations of a given set of input or output and thus the service 
developers themselves are less clear on what the clients may experience as a result of a change.

  ![The elaborated report on content types] (sample-reports/designImprovement.png)

And:

  ![The elaborated report on content types] (sample-reports/pattern_from_null_pot-break.png)

## Opinionated parts
The opinionated parts are:

 * that [HAL](https://tools.ietf.org/html/draft-kelly-json-hal-08) is used as the way to do HATEOAS, 
 * a version paradigm based on content-type parameters for the endpoint content versioning
 * a version paradigm based on a HTTP header for the API structure versioning
 
### Versioning Content in an Endpoint
The opinionated parts are not used just to be different, they serve a purpose. The use of the versioning mechanism by 
content-type parameters are an elegant way to ease the effort for clients (service users) and maintain speed for the 
evolution of services. The versioning paradigm works as follows:

 1. The service is published with a default producer and a specific producer for a given endpoint:
    * application/hal+json (the default producer)
    * application/hal+json;concept=account;v=1

This allows clients to use the application/hal+json in their Accept header to signal to the service "I am ready for the
newest content produced at the endpoint" - the service makes sure that the `content-type` is set in every HTTP response 
every time the client gets and answer back from the endpoint, in this case that content-type is
 `application/hal+json;concept=account;v=1`. 

After a while the service endpoint must upgrade the content in the endpoint and thus it adds a producer to the endpoint. 

 2. The Service now supports the following producers:
    * application/hal+json (the default producer)
    * application/hal+json;concept=account;v=1
    * application/hal+json;concept=account;v=2

The clients uses the application/hal+json in their Accept header to signal to the service "I am ready for the
newest content produced at the endpoint" - as they may not know that the updated version can also be served from the 
endpoint. Clients can however see that a new version has been introduces as the services now sets the `content-type` 
response header set to `application/hal+json;concept=account;v=2`. 

If the client(s) do not experience any problems with the upgraded version the older one can be deprecated and taken out 
of "production" - on the other hand - if there are clients that experience problems, they can revert to the old version 
 (while they make the necessary change to the client code) by setting the `Accept` header in their requests for the service 
to `application/hal+json;concept=account;v=1` and get the older version served a little longer. Clients can get help from 
their users by having a way that users can report "things that look strange" and that could be reacted to (from the client
code) by fall-back to the last known working content-type for a given endpoint.
Examples of code that uses this principle can be found in the [HATEOAS](https://github.com/Nykredit/HATEOAS/tree/alledgedContentTypeSupport) 
project, a there is a branch showing this principle including support for cross container routing, if a container is used.

Using this principle the [type](https://tools.ietforg/html/draft-kelly-json-hal-08#section-5.3) from the  `_link` 
defined by HAL can be empty as long as the client did not observe any problems with the default 
content-type `application/hal+json`. 

### Versioning the API 
A service API can change from a structural perspective, endpoint may move elsewhere. A service want to do the best it can 
in order to serve clients in the best possible way and thus client are required to set a HTTP header called 
`X-Service-Generation` in order to signal the clients expectation to the service. If the service makes sure that it 
includes response codes like [`301`]((https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)), 
[`307`]((https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)) etc. in its response code set in the API definition, 
the client knows that it needs to react on these responses in form of following the `location` header to the new 
destination. The alternative is that the clients get the experience of a "service being down" which makes clients unhappy. 
 
If a service adds [`202`]((https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)) Accepted as a response code, that 
may also break clients as they may not have included in their programming model that a `202` could occur, which means 
the client does not take into account that it need to follow the `location` header and wait for the amount of time
suggested by the `retry-after` header

### Other opinionated parts
The inclusion of [`415`](https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html), 
[`500`](https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)
and [`429`](https://tools.ietf.org/html/rfc6585#page-3) and others in the fixed set of response codes that should be 
considered in an API in order to make the evolution simpler for the clients and thus give the service implementers the 
ability to move fast and stay competitive. This is not something that is be`ing enforced on you, it will be possible to 
configure your own profile at some point in time in the tool if it brings value to enough people.

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