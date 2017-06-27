# API Comparison Results
## Delivered by Open API Diff tooling


The report layout uses three sections: short, elaborate and compliance
The reports shows the added endpoints, the removed and the changed endpoints on a short form.
The changed endpoints are succeeding that presented in a more elaborate form

The syntax used is:

 - ` + ` means added.

 - ` - ` means removed.

 - ` @ ` means altered.

 - ` ! ` means issue found in Existing API.

 - ` > ` means issues found in the New API.

 - *` C   `* means (Compliance).



The APIs compared are:
 - ` ! ` ./sample-api/elaborate_example_v1.json
 - ` > ` ./sample-api/elaborate_example_v3f.json

## Added Endpoints
  Added Endpoints  |  Info
----------------- | -------
 + ` GET `  `/accounts/{regNo}-{accountNo}/cards`    | obtain cards associated with a given account
 + ` GET `  `/virtual-accounts/{regNo}-{accountNo}`    | gets the information from a single virtual account
 + ` PUT `  `/virtual-accounts/{regNo}-{accountNo}`    | Create new or update existing virtual account
 + ` GET `  `/virtual-accounts`    | lists virtual accounts


## Removed Endpoints
 `   No Removed Endpoints  `


## Changed Endpoints
  Changed or Observable Endpoint  |  Info
------------------- | -------
 @ ` GET `  ` /accounts/{regNo}-{accountNo} ` | gets the information from a single account
 @ ` PUT `  ` /accounts/{regNo}-{accountNo} ` | Create new or update existing account
 @ ` GET `  ` /accounts ` | lists accounts
 @ ` GET `  ` /account-events-metadata ` | metadata for the events endpoint
 @ ` GET `  ` /account-events ` | obtain all events emitted by the account-event service
 @ ` GET `  ` /account-events/{category} ` | obtain all events scoped to a certain category
 @ ` GET `  ` /account-events/{category}/{id} ` | obtain the individual events from an account
 @ ` GET `  ` /accounts/{regNo}-{accountNo}/reconciled-transactions/{id} ` | obtain a single reconciled transaction from a given account
 @ ` PUT `  ` /accounts/{regNo}-{accountNo}/reconciled-transactions/{id} ` | Create new or update reconciled transaction
 @ ` GET `  ` /accounts/{regNo}-{accountNo}/reconciled-transactions ` | obtain reconciled transactions (added API capabilities not though not implemented)
 @ ` GET `  ` /accounts/{regNo}-{accountNo}/transactions/{id} ` | obtain the individual single transaction from an account
 @ ` PUT `  ` /accounts/{regNo}-{accountNo}/transactions/{id} ` | creates a single transaction on an account
 @ ` GET `  ` /accounts/{regNo}-{accountNo}/transactions ` | obtain all transactions on account for a given account


## The Elaborated Report for Changed Endpoints 
###  @ `/accounts/{regNo}-{accountNo}`

####     Operations:

#####   *` C   `* `GET`  *`/accounts/{regNo}-{accountNo}`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 503 Observation | **added header: Retry-After which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 401 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 412 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 429 Observation | **added header: Retry-After which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 403 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 404 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 415 Observation | **added header: Content-Type which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 301  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 404  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 403  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 429  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 304  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` accounts {regNo}-{accountNo} content-type producers removed | **this may cause problem for some clients removed are: [[application/hal+json;concept=account;v=1]]** 
 ` ! ` GET Response Code 503  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 401  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 412  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
#####   *` C   `* `PUT`  *`/accounts/{regNo}-{accountNo}`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` PUT Response Code 201 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` PUT Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 505 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 400 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 429 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` PUT Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.^[0-9]+$.to.^[0-9]{10}$** 
 ` ! ` PUT Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Response 307 Added | **adding 307 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 400  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Response 301 Added | **adding 301 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 



###  @ `/accounts`

####     Operations:

#####   *` C   `* `GET`  *`/accounts`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 



###  @ `/account-events-metadata`

####     Operations:

#####   *` C   `* `GET`  *`/account-events-metadata`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` Non Compliant Required Header Setup | **a required header X-Client-Version was not found** 
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 



###  @ `/account-events`

####     Operations:

#####   *` C   `* `GET`  *`/account-events`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 



###  @ `/account-events/{category}`

####     Operations:

#####   *` C   `* `GET`  *`/account-events/{category}`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 



###  @ `/account-events/{category}/{id}`

####     Operations:

#####   *` C   `* `GET`  *`/account-events/{category}/{id}`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 404 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 404  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 



###  @ `/accounts/{regNo}-{accountNo}/reconciled-transactions/{id}`

####     Operations:

#####   *` C   `* `GET`  *`/accounts/{regNo}-{accountNo}/reconciled-transactions/{id}`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 404 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 404  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.null.to.[0-9]{10}$** 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path RegNo | **pattern.changed.from.null.to.[0-9]{4}$** 
#####   *` C   `* `PUT`  *`/accounts/{regNo}-{accountNo}/reconciled-transactions/{id}`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` PUT Response Code 201 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` PUT Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` PUT Response Code 505 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 400 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 429 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` PUT Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.^[0-9]+$.to.^[0-9]{10}$** 
 ` ! ` PUT Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Response 307 Added | **adding 307 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 400  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Response 301 Added | **adding 301 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` accounts {regNo}-{accountNo} reconciled-transactions {id} producer versioning non-compliant | **content producers are not following the scheme of having application/hal+json;concept=[projection];v=[version] with a SINGLE version available. The observed changed and added producers are: [common][added] [[application/hal+json]] [[application/hal+json;concept=reconciledtransaction;v=1]] [[]]** 



###  @ `/accounts/{regNo}-{accountNo}/reconciled-transactions`

####     Operations:

#####   *` C   `* `GET`  *`/accounts/{regNo}-{accountNo}/reconciled-transactions`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.null.to.[0-9]{10}$** 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path RegNo | **pattern.changed.from.null.to.[0-9]{4}$** 



###  @ `/accounts/{regNo}-{accountNo}/transactions/{id}`

####     Operations:

#####   *` C   `* `GET`  *`/accounts/{regNo}-{accountNo}/transactions/{id}`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` Transaction Response Body Id | **Transaction.response.body.id.required.changed.from.false.to.true** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` Transaction Response Body Amount | **Transaction.response.body.amount.required.changed.from.false.to.true** 
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` Transaction Response Body Description | **Transaction.response.body.description.required.changed.from.false.to.true** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 404 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 404  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.null.to.[0-9]{10}$** 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path RegNo | **pattern.changed.from.null.to.[0-9]{4}$** 
#####   *` C   `* `PUT`  *`/accounts/{regNo}-{accountNo}/transactions/{id}`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` Transaction Response Body Id | **Transaction.response.body.id.required.changed.from.false.to.true** 
 ` > ` TransactionUpdate Body Description | **body.property.changed.{body.description.=[pattern.changed.from..{1,256}.to..{1,512}], body.amount.=[pattern.changed.from.^([0-9]{1,9})((\.)([0-9]{2}))?.to.^(-{0,1}[0-9]{1,9})((\.)([0-9]{2}))?]}** 
 ` > ` PUT Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 505 Observation | **added header: X-Log-Token which is required** 
 ` > ` TransactionUpdate Body Amount | **body.property.changed.{body.description.=[pattern.changed.from..{1,256}.to..{1,512}], body.amount.=[pattern.changed.from.^([0-9]{1,9})((\.)([0-9]{2}))?.to.^(-{0,1}[0-9]{1,9})((\.)([0-9]{2}))?]}** 
 ` > ` PUT Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 202 Observation | **added header: Location which is required** 
 | **added header: Retry-After which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` Transaction Response Body Amount | **Transaction.response.body.amount.required.changed.from.false.to.true** 
 ` > ` PUT Response Code 201 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` Transaction Response Body Description | **Transaction.response.body.description.required.changed.from.false.to.true** 
 ` > ` PUT Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` PUT Response Code 400 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 429 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` > ` Header Accept | **pattern.changed.from._^application/hal+json[;concept=[a_z]+;v=[0_9]+].to._^application/hal+json[;concept=[a_z]{255};v=[0_9]+]** 
 ` ! ` PUT Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` TransactionUpdate Body Description | **body.property.changed.{body.description.=[pattern.changed.from..{1,256}.to..{1,512}], body.amount.=[pattern.changed.from.^([0-9]{1,9})((\.)([0-9]{2}))?.to.^(-{0,1}[0-9]{1,9})((\.)([0-9]{2}))?]}** 
 ` > ` Path AccountNo | **pattern.changed.from.null.to.[0-9]{10}$** 
 ` ! ` PUT Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Response 307 Added | **adding 307 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 400  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path RegNo | **pattern.changed.from.null.to.[0-9]{4}$** 
 ` > ` TransactionUpdate Body Amount | **body.property.changed.{body.description.=[pattern.changed.from..{1,256}.to..{1,512}], body.amount.=[pattern.changed.from.^([0-9]{1,9})((\.)([0-9]{2}))?.to.^(-{0,1}[0-9]{1,9})((\.)([0-9]{2}))?]}** 
 ` > ` Response 301 Added | **adding 301 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 202  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` PUT Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 



###  @ `/accounts/{regNo}-{accountNo}/transactions`

####     Operations:

#####   *` C   `* `GET`  *`/accounts/{regNo}-{accountNo}/transactions`* -> compliance: `false` 

#### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
#### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.null.to.[0-9]{10}$** 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path RegNo | **pattern.changed.from.null.to.[0-9]{4}$** 





## The Elaborated Compliance Report

###  @ `/accounts/{regNo}-{accountNo}/cards`

  `   no observations    `



###  @ `/virtual-accounts/{regNo}-{accountNo}`

  `   no observations    `



###  @ `/virtual-accounts`

  `   no observations    `



###  @ `/accounts/{regNo}-{accountNo}`

####     Operations:

#####   *` C   `* `GET`  *`/accounts/{regNo}-{accountNo}`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 503 Observation | **added header: Retry-After which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 401 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 412 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 429 Observation | **added header: Retry-After which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 403 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 404 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 415 Observation | **added header: Content-Type which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 301  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 404  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 403  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 429  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 304  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` accounts {regNo}-{accountNo} content-type producers removed | **this may cause problem for some clients removed are: [[application/hal+json;concept=account;v=1]]** 
 ` ! ` GET Response Code 503  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 401  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 412  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
##### Recorded Changes
  Changes                       |  Info
------------------------------- | -------
 ` > ` accounts {regNo}-{accountNo} content-type producers removed | this may cause problem for some clients removed are: [[application/hal+json;concept=account;v=1]] 
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
##### Recorded Changes to Content-Types in API
  Changes to Content-Types      |  Info
------------------------------- | -------
 ` > ` ` + ` *Application Hal Json Concept Account V 3*  | *application/hal+json;concept=account;v=3* 
 ` > ` **` - `** **Application Hal Json Concept Account V 1**  | **application/hal+json;concept=account;v=1** 
##### Recorded Changes to Parameters in API
  `   no observations    `
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *202*  | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` **` @ `** *203*  | **adding 203 Non Authoritative means the client gets information from a different source than it anticipated and thus the caching headers, modified since etc. may not be as valid as they are when information origins from the resource itself** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` GET Response Code 200 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` > ` GET Response Code 301 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Expires which is required - no additional info 
 |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 301  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 304 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Expires which is required - no additional info 
 |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 304  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 307 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 401 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 401  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 403 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 403  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 404 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 404  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 410 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 410  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 412 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 412  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 415 Observation  |  |  added header: Content-Type which is required - no additional info 
 |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 415  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 429 Observation  |  |  added header: Retry-After which is required - no additional info 
 |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 429  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 503 Observation  |  |  added header: Retry-After which is required - no additional info 
 |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 503  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
#####   *` C   `* `PUT`  *`/accounts/{regNo}-{accountNo}`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` PUT Response Code 201 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` PUT Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 505 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 400 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 429 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` PUT Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.^[0-9]+$.to.^[0-9]{10}$** 
 ` ! ` PUT Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Response 307 Added | **adding 307 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 400  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Response 301 Added | **adding 301 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header Content Encoding For PUT  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit For PUT  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit 24h For PUT  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Remaining For PUT  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Reset For PUT  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For PUT  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For PUT  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
##### Recorded Changes to Content-Types in API
  Changes to Content-Types      |  Info
------------------------------- | -------
 ` > ` ` + ` *Application Json Concept Accountupdate V 1*  | *application/json;concept=accountupdate;v=1* 
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *301*  | **adding 301 Permanently Moved may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` > ` **` @ `** *304*  | **adding 304 Modified allows clients to know whether they need to re-get information, beware that the implementation in the service must required less effort than the GET would have caused** 
 ` > ` **` @ `** *307*  | **adding 307 Temporarily Moved may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` > ` **` @ `** *410*  | **adding 410 Gone is not harming the client, it merely tells the client that at some point after having moved the resource to another place you will no longer receive 301's and when this happen a 410 be presented** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` PUT Response Code 201 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header Content Encoding For PUT  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit For PUT  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit 24h For PUT  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Remaining For PUT  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Reset For PUT  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` > ` PUT Response Code 400 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 400  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` PUT Response Code 415 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 415  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` PUT Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For PUT  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` PUT Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` PUT Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For PUT  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` PUT Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used



###  @ `/accounts`

####     Operations:

#####   *` C   `* `GET`  *`/accounts`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  `   no observations    `
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *202*  | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` **` @ `** *203*  | **adding 203 Non Authoritative means the client gets information from a different source than it anticipated and thus the caching headers, modified since etc. may not be as valid as they are when information origins from the resource itself** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` GET Response Code 200 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` > ` GET Response Code 301 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 304 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Expires which is required - no additional info 
 ` > ` GET Response Code 307 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 410 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 410  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 415 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 415  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used



###  @ `/account-events-metadata`

####     Operations:

#####   *` C   `* `GET`  *`/account-events-metadata`* -> compliance: `false` 

#### Design Issues
  Issues          |  Info
----------------------- | -------
 ` > ` Future Compliance For GET Response Code 200 Observation | **improvement suggestion - important status code or headers missing for new API, may break future APIs for consumers, see compliance section for further information if full depth is used** 
 ` > ` Future Difference Recorded Response 200 Missing Special Header Content Encoding For GET | **Content-Encoding: informs on the body - is it compressed or not e.g. gzip** 
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` Non Compliant Required Header Setup | **a required header X-Client-Version was not found** 
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
 ` > ` **`- `** **X-Client-Version | **io.swagger.models.parameters.HeaderParameter@eaefcd9** 
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *202*  | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` **` @ `** *203*  | **adding 203 Non Authoritative means the client gets information from a different source than it anticipated and thus the caching headers, modified since etc. may not be as valid as they are when information origins from the resource itself** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` GET Response Code 200 Observation  |  |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` > ` Future Compliance For GET Response Code 200 Observation  | improvement suggestion - important status code or headers missing for new API, may break future APIs for consumers, see compliance section for further information if full depth is used 
 ` > ` Future Difference Recorded Response 200 Missing Special Header Content Encoding For GET  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` > ` GET Response Code 301 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 304 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Expires which is required - no additional info 
 ` > ` GET Response Code 307 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 410 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 410  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 415 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 415  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used



###  @ `/account-events`

####     Operations:

#####   *` C   `* `GET`  *`/account-events`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  `   no observations    `
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *202*  | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` **` @ `** *203*  | **adding 203 Non Authoritative means the client gets information from a different source than it anticipated and thus the caching headers, modified since etc. may not be as valid as they are when information origins from the resource itself** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` GET Response Code 200 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` > ` GET Response Code 301 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 304 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Expires which is required - no additional info 
 ` > ` GET Response Code 307 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 410 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 410  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 415 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 415  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used



###  @ `/account-events/{category}`

####     Operations:

#####   *` C   `* `GET`  *`/account-events/{category}`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  `   no observations    `
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *202*  | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` **` @ `** *203*  | **adding 203 Non Authoritative means the client gets information from a different source than it anticipated and thus the caching headers, modified since etc. may not be as valid as they are when information origins from the resource itself** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` GET Response Code 200 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` > ` GET Response Code 301 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 304 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Expires which is required - no additional info 
 ` > ` GET Response Code 307 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 410 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 410  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 415 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 415  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used



###  @ `/account-events/{category}/{id}`

####     Operations:

#####   *` C   `* `GET`  *`/account-events/{category}/{id}`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 404 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 404  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  `   no observations    `
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *202*  | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` **` @ `** *203*  | **adding 203 Non Authoritative means the client gets information from a different source than it anticipated and thus the caching headers, modified since etc. may not be as valid as they are when information origins from the resource itself** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` GET Response Code 200 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` > ` GET Response Code 301 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 304 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Expires which is required - no additional info 
 ` > ` GET Response Code 307 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 404 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 404  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 410 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 410  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 415 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 415  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used



###  @ `/accounts/{regNo}-{accountNo}/reconciled-transactions/{id}`

####     Operations:

#####   *` C   `* `GET`  *`/accounts/{regNo}-{accountNo}/reconciled-transactions/{id}`* -> compliance: `false` 

#### Design Issues
  Issues          |  Info
----------------------- | -------
 ` > ` Changepath AccountNo | **path.accountNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult** 
 ` > ` Changepath RegNo | **path.regNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult** 
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 404 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 404  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.null.to.[0-9]{10}$** 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path RegNo | **pattern.changed.from.null.to.[0-9]{4}$** 
##### Recorded Changes
  Changes                       |  Info
------------------------------- | -------
 ` > ` Path AccountNo | path.accountNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult 
 ` > ` Path RegNo | path.regNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult 
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *202*  | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` **` @ `** *203*  | **adding 203 Non Authoritative means the client gets information from a different source than it anticipated and thus the caching headers, modified since etc. may not be as valid as they are when information origins from the resource itself** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` GET Response Code 200 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` > ` GET Response Code 301 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 304 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Expires which is required - no additional info 
 ` > ` GET Response Code 307 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 404 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 404  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 410 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 410  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 415 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 415  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
#####   *` C   `* `PUT`  *`/accounts/{regNo}-{accountNo}/reconciled-transactions/{id}`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` PUT Response Code 201 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` PUT Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` PUT Response Code 505 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 400 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 429 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` PUT Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.^[0-9]+$.to.^[0-9]{10}$** 
 ` ! ` PUT Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Response 307 Added | **adding 307 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 400  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Response 301 Added | **adding 301 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` accounts {regNo}-{accountNo} reconciled-transactions {id} producer versioning non-compliant | **content producers are not following the scheme of having application/hal+json;concept=[projection];v=[version] with a SINGLE version available. The observed changed and added producers are: [common][added] [[application/hal+json]] [[application/hal+json;concept=reconciledtransaction;v=1]] [[]]** 
##### Recorded Changes
  Changes                       |  Info
------------------------------- | -------
 ` > ` accounts {regNo}-{accountNo} reconciled-transactions {id} producer version scheme conflict | versions do not overlap correctly or and/do not use default version. Content-types are [common][added] [[application/hal+json]] [[application/hal+json;concept=reconciledtransaction;v=1]] [[]] 
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header Content Encoding For PUT  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit For PUT  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit 24h For PUT  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Remaining For PUT  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Reset For PUT  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For PUT  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For PUT  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
##### Recorded Changes to Content-Types in API
  Changes to Content-Types      |  Info
------------------------------- | -------
 ` > ` ` + ` *Application Hal Json Concept Reconciledtransaction V 1*  | *application/hal+json;concept=reconciledtransaction;v=1* 
 ` > ` ` + ` *Application Json Concept Reconciledtransactionupdate V 1*  | *application/json;concept=reconciledtransactionupdate;v=1* 
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *301*  | **adding 301 Permanently Moved may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` > ` **` @ `** *304*  | **adding 304 Modified allows clients to know whether they need to re-get information, beware that the implementation in the service must required less effort than the GET would have caused** 
 ` > ` **` @ `** *307*  | **adding 307 Temporarily Moved may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` > ` **` @ `** *410*  | **adding 410 Gone is not harming the client, it merely tells the client that at some point after having moved the resource to another place you will no longer receive 301's and when this happen a 410 be presented** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` PUT Response Code 200 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` > ` PUT Response Code 201 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header Content Encoding For PUT  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit For PUT  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit 24h For PUT  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Remaining For PUT  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Reset For PUT  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` > ` PUT Response Code 400 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 400  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` PUT Response Code 415 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 415  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` PUT Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For PUT  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` PUT Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` PUT Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For PUT  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` PUT Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used



###  @ `/accounts/{regNo}-{accountNo}/reconciled-transactions`

####     Operations:

#####   *` C   `* `GET`  *`/accounts/{regNo}-{accountNo}/reconciled-transactions`* -> compliance: `false` 

#### Design Issues
  Issues          |  Info
----------------------- | -------
 ` > ` Changepath AccountNo | **path.accountNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult** 
 ` > ` Changepath RegNo | **path.regNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult** 
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.null.to.[0-9]{10}$** 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path RegNo | **pattern.changed.from.null.to.[0-9]{4}$** 
##### Recorded Changes
  Changes                       |  Info
------------------------------- | -------
 ` > ` Path AccountNo | path.accountNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult 
 ` > ` Path RegNo | path.regNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult 
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *202*  | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` **` @ `** *203*  | **adding 203 Non Authoritative means the client gets information from a different source than it anticipated and thus the caching headers, modified since etc. may not be as valid as they are when information origins from the resource itself** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` GET Response Code 200 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` > ` GET Response Code 301 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 304 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Expires which is required - no additional info 
 ` > ` GET Response Code 307 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 410 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 410  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 415 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 415  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used



###  @ `/accounts/{regNo}-{accountNo}/transactions/{id}`

####     Operations:

#####   *` C   `* `GET`  *`/accounts/{regNo}-{accountNo}/transactions/{id}`* -> compliance: `false` 

#### Design Issues
  Issues          |  Info
----------------------- | -------
 ` > ` Changepath AccountNo | **path.accountNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult** 
 ` > ` Changepath RegNo | **path.regNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult** 
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` Transaction Response Body Id | **Transaction.response.body.id.required.changed.from.false.to.true** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` Transaction Response Body Amount | **Transaction.response.body.amount.required.changed.from.false.to.true** 
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` Transaction Response Body Description | **Transaction.response.body.description.required.changed.from.false.to.true** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 404 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 404  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.null.to.[0-9]{10}$** 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path RegNo | **pattern.changed.from.null.to.[0-9]{4}$** 
##### Recorded Changes
  Changes                       |  Info
------------------------------- | -------
 ` > ` Transaction Transaction Response Body Amount | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Transaction Transaction Response Body Sid | body.property.added: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Path AccountNo | path.accountNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult 
 ` > ` Transaction Transaction Response Body Description | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Path RegNo | path.regNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult 
 ` > ` Transaction Transaction Response Body Id | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
##### Recorded Changes to Content-Types in API
  Changes to Content-Types      |  Info
------------------------------- | -------
 ` > ` ` + ` *Application Hal Json Concept Transaction V 2*  | *application/hal+json;concept=transaction;v=2* 
 ` > ` ` + ` *Application Hal Json Concept Transaction Sparse V 1*  | *application/hal+json;concept=transaction-sparse;v=1* 
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *202*  | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` **` @ `** *203*  | **adding 203 Non Authoritative means the client gets information from a different source than it anticipated and thus the caching headers, modified since etc. may not be as valid as they are when information origins from the resource itself** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` Transaction Response Body Id  |  | Transaction.response.body.id.required.changed.from.false.to.true - no additional info 
 ` > ` Transaction Response Body Description  |  | Transaction.response.body.description.required.changed.from.false.to.true - no additional info 
 ` > ` GET Response Code 200 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` > ` Transaction Response Body Amount  |  | Transaction.response.body.amount.required.changed.from.false.to.true - no additional info 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` > ` Transaction Transaction Response Body Amount  | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Transaction Transaction Response Body Sid  | body.property.added: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Transaction Transaction Response Body Description  | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Transaction Transaction Response Body Id  | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` GET Response Code 301 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 304 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Expires which is required - no additional info 
 ` > ` GET Response Code 307 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 404 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 404  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 410 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 410  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
#####   *` C   `* `PUT`  *`/accounts/{regNo}-{accountNo}/transactions/{id}`* -> compliance: `false` 

#### Design Issues
  Issues          |  Info
----------------------- | -------
 ` > ` Changepath AccountNo | **path.accountNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult** 
 ` > ` Changepath RegNo | **path.regNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult** 
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` Transaction Response Body Id | **Transaction.response.body.id.required.changed.from.false.to.true** 
 ` > ` TransactionUpdate Body Description | **body.property.changed.{body.description.=[pattern.changed.from..{1,256}.to..{1,512}], body.amount.=[pattern.changed.from.^([0-9]{1,9})((\.)([0-9]{2}))?.to.^(-{0,1}[0-9]{1,9})((\.)([0-9]{2}))?]}** 
 ` > ` PUT Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 505 Observation | **added header: X-Log-Token which is required** 
 ` > ` TransactionUpdate Body Amount | **body.property.changed.{body.description.=[pattern.changed.from..{1,256}.to..{1,512}], body.amount.=[pattern.changed.from.^([0-9]{1,9})((\.)([0-9]{2}))?.to.^(-{0,1}[0-9]{1,9})((\.)([0-9]{2}))?]}** 
 ` > ` PUT Response Code 415 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 202 Observation | **added header: Location which is required** 
 | **added header: Retry-After which is required** 
 | **added header: X-Log-Token which is required** 
 ` > ` Transaction Response Body Amount | **Transaction.response.body.amount.required.changed.from.false.to.true** 
 ` > ` PUT Response Code 201 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` Transaction Response Body Description | **Transaction.response.body.description.required.changed.from.false.to.true** 
 ` > ` PUT Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` PUT Response Code 400 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` PUT Response Code 429 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` > ` Header Accept | **pattern.changed.from._^application/hal+json[;concept=[a_z]+;v=[0_9]+].to._^application/hal+json[;concept=[a_z]{255};v=[0_9]+]** 
 ` ! ` PUT Response Code 415  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` TransactionUpdate Body Description | **body.property.changed.{body.description.=[pattern.changed.from..{1,256}.to..{1,512}], body.amount.=[pattern.changed.from.^([0-9]{1,9})((\.)([0-9]{2}))?.to.^(-{0,1}[0-9]{1,9})((\.)([0-9]{2}))?]}** 
 ` > ` Path AccountNo | **pattern.changed.from.null.to.[0-9]{10}$** 
 ` ! ` PUT Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Response 307 Added | **adding 307 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 400  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path RegNo | **pattern.changed.from.null.to.[0-9]{4}$** 
 ` > ` TransactionUpdate Body Amount | **body.property.changed.{body.description.=[pattern.changed.from..{1,256}.to..{1,512}], body.amount.=[pattern.changed.from.^([0-9]{1,9})((\.)([0-9]{2}))?.to.^(-{0,1}[0-9]{1,9})((\.)([0-9]{2}))?]}** 
 ` > ` Response 301 Added | **adding 301 may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` ! ` PUT Response Code 202  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` PUT Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
##### Recorded Changes
  Changes                       |  Info
------------------------------- | -------
 ` > ` Transaction Transaction Response Body Amount | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Transaction Transaction Response Body Sid | body.property.added: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Path AccountNo | path.accountNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult 
 ` > ` Transaction Transaction Response Body Description | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Path RegNo | path.regNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult 
 ` > ` Transaction Transaction Response Body Id | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header Content Encoding For PUT  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit For PUT  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit 24h For PUT  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Remaining For PUT  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Reset For PUT  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For PUT  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For PUT  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
##### Recorded Changes to Content-Types in API
  Changes to Content-Types      |  Info
------------------------------- | -------
 ` > ` ` + ` *Application Json Concept Transactionupdate V 1*  | *application/json;concept=transactionupdate;v=1* 
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
##### Recorded Changes to Properties in API
  Changes to Properties (Body Definitions)      |  Info
------------------------------------------------- | -------
 ` > ` `+ ` *body.currency*  | *Required* 
 ` > ` **` @ `** *TransactionUpdate.body.description*  | body.property.changed.{body.description.=[pattern.changed.from..{1,256}.to..{1,512}], body.amount.=[pattern.changed.from.^([0-9]{1,9})((\.)([0-9]{2}))?.to.^(-{0,1}[0-9]{1,9})((\.)([0-9]{2}))?]}
 ` > ` **` @ `** *TransactionUpdate.body.amount*  | body.property.changed.{body.description.=[pattern.changed.from..{1,256}.to..{1,512}], body.amount.=[pattern.changed.from.^([0-9]{1,9})((\.)([0-9]{2}))?.to.^(-{0,1}[0-9]{1,9})((\.)([0-9]{2}))?]}
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *301*  | **adding 301 Permanently Moved may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` > ` **` @ `** *304*  | **adding 304 Modified allows clients to know whether they need to re-get information, beware that the implementation in the service must required less effort than the GET would have caused** 
 ` > ` **` @ `** *307*  | **adding 307 Temporarily Moved may break the client programming model, clients may not be able to follow the location if not implemented and thus experience service as being down** 
 ` > ` **` @ `** *410*  | **adding 410 Gone is not harming the client, it merely tells the client that at some point after having moved the resource to another place you will no longer receive 301's and when this happen a 410 be presented** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` Transaction Response Body Id  |  | Transaction.response.body.id.required.changed.from.false.to.true - no additional info 
 ` > ` Transaction Response Body Description  |  | Transaction.response.body.description.required.changed.from.false.to.true - no additional info 
 ` > ` PUT Response Code 200 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` > ` Transaction Response Body Amount  |  | Transaction.response.body.amount.required.changed.from.false.to.true - no additional info 
 ` > ` Transaction Transaction Response Body Amount  | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Transaction Transaction Response Body Sid  | body.property.added: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Transaction Transaction Response Body Description  | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Transaction Transaction Response Body Id  | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` PUT Response Code 201 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` > ` Transaction Response Body Id  |  | Transaction.response.body.id.required.changed.from.false.to.true - no additional info 
 ` > ` Transaction Response Body Description  |  | Transaction.response.body.description.required.changed.from.false.to.true - no additional info 
 ` > ` Transaction Response Body Amount  |  | Transaction.response.body.amount.required.changed.from.false.to.true - no additional info 
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header Content Encoding For PUT  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit For PUT  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Limit 24h For PUT  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Remaining For PUT  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` ! ` Existing Difference Recorded Response 201 Missing Special Header X RateLimit Reset For PUT  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` > ` Transaction Transaction Response Body Amount  | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Transaction Transaction Response Body Sid  | body.property.added: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Transaction Transaction Response Body Description  | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` Transaction Transaction Response Body Id  | body.property.changed: 
 | Transaction.response.body.id.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.description.=[ required.changed.from.false.to.true ],
 |  Transaction.response.body.amount.=[ required.changed.from.false.to.true ] 
 | 
 ` > ` PUT Response Code 202 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Retry-After which is required - no additional info 
 |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 202  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` PUT Response Code 400 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 400  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` PUT Response Code 415 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 415  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` PUT Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For PUT  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` PUT Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` PUT Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For PUT  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` PUT Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* PUT Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used



###  @ `/accounts/{regNo}-{accountNo}/transactions`

####     Operations:

#####   *` C   `* `GET`  *`/accounts/{regNo}-{accountNo}/transactions`* -> compliance: `false` 

#### Design Issues
  Issues          |  Info
----------------------- | -------
 ` > ` Changepath AccountNo | **path.accountNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult** 
 ` > ` Changepath RegNo | **path.regNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult** 
##### Breaking Changes
  Breaking          |  Info
------------------- | -------
 ` > ` GET Response Code 304 Observation | **added header: Location which is required** 
 | **added header: Expires which is required** 
 ` > ` GET Response Code 429 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 410 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 500 Observation | **added header: X-Log-Token which is required** 
 ` > ` GET Response Code 200 Observation | **added header: Content-Encoding which is required** 
 | **added header: X-RateLimit-Limit which is required** 
 | **added header: X-RateLimit-Limit-24h which is required** 
 | **added header: X-RateLimit-Remaining which is required** 
 | **added header: X-RateLimit-Reset which is required** 
 ` > ` GET Response Code 301 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 503 Observation | **added header: X-Log-Token which is required** 
 ` > ` Response 202 Added | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` GET Response Code 307 Observation | **added header: Expires which is required** 
 ` > ` GET Response Code 505 Observation | **added header: X-Log-Token which is required** 
##### Potentially Breaking Changes
  Potentially Breaking          |  Info
------------------------------- | -------
 ` ! ` GET Response Code 505  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path AccountNo | **pattern.changed.from.null.to.[0-9]{10}$** 
 ` ! ` GET Response Code 410  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` ! ` GET Response Code 500  missing | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used 
 ` > ` Path RegNo | **pattern.changed.from.null.to.[0-9]{4}$** 
##### Recorded Changes
  Changes                       |  Info
------------------------------- | -------
 ` > ` Path AccountNo | path.accountNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult 
 ` > ` Path RegNo | path.regNo .existing.compliance.pattern.defined.as.null::makes future proof api design difficult 
##### Recorded Flaws in existing API
  Improvements to existing API  |  Info
------------------------------- | -------
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  missing | X-RateLimit-Limit: Request limit per minute 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  missing | Expires: sets the expiry time for the information retrieved in the response 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  missing | Content-Encoding: informs on the body - is it compressed or not e.g. gzip 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  missing |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  missing | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  missing | X-RateLimit-Limit-24h: Request limit per 24h 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  missing | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined) 
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
 ` > ` `+ ` *select*  | *Optional* 
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  Changes to Responses (Status Code and Headers)      |  Info
----------------------------------------------------- | -------
 ` > ` **` @ `** *202*  | **adding 202 Accepted breaks the client programming model and is not compatible** 
 ` > ` **` @ `** *203*  | **adding 203 Non Authoritative means the client gets information from a different source than it anticipated and thus the caching headers, modified since etc. may not be as valid as they are when information origins from the resource itself** 
 ` > ` **` @ `** *501*  | **adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the implemented parts** 
 ` > ` GET Response Code 200 Observation  |  |  added header: Content-Encoding which is required - no additional info 
 |  added header: X-RateLimit-Limit which is required - no additional info 
 |  added header: X-RateLimit-Limit-24h which is required - no additional info 
 |  added header: X-RateLimit-Remaining which is required - no additional info 
 |  added header: X-RateLimit-Reset which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit For GET  | X-RateLimit-Limit: Request limit per minute
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header Content Encoding For GET  | Content-Encoding: informs on the body - is it compressed or not e.g. gzip
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Reset For GET  | X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Limit 24h For GET  | X-RateLimit-Limit-24h: Request limit per 24h
 ` ! ` Existing Difference Recorded Response 200 Missing Special Header X RateLimit Remaining For GET  | X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)
 ` > ` GET Response Code 301 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 301 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 304 Observation  |  |  added header: Location which is required - no additional info 
 |  added header: Expires which is required - no additional info 
 ` > ` GET Response Code 307 Observation  |  |  added header: Expires which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 307 Missing Special Header Expires For GET  | Expires: sets the expiry time for the information retrieved in the response
 ` > ` GET Response Code 410 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 410  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 429 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 429 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 500 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 500  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
 ` > ` GET Response Code 503 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! ` Existing Difference Recorded Response 503 Missing Default Header X Log Token For GET  |  X-Log-Token: allows the client side reference to activities and logging on the serverside, if added to the Request as a header using the same name X-Log-Token it should be reused on the client side
 ` > ` GET Response Code 505 Observation  |  |  added header: X-Log-Token which is required - no additional info 
 ` ! `  *` C `* GET Response Code 505  | improvement suggestion - important status code or headers missing for existing API, potentially breaking clients ahead, see compliance section for further information if full depth is used
