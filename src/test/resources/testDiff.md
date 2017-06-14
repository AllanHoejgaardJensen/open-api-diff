# ChangeLog
## DiffReport


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
 - ` ! ` 
 - ` > ` 

## Added Endpoints
 `   No Added Endpoints  `


## Removed Endpoints
 `   No Removed Endpoints  `


## Changed Endpoints
  Changed or Observable Endpoint  |  Info
------------------- | -------
 @ ` PUT `  ` /pet ` | Update an existing pet
 @ ` POST `  ` /pet ` | Add a new pet to the store
 @ ` GET `  ` /pet/findByStatus ` | Finds Pets by status
 @ ` GET `  ` /pet/findByTags ` | Finds Pets by tags
 @ ` GET `  ` /pet/{petId} ` | Find pet by ID
 @ ` POST `  ` /pet/{petId} ` | Updates a pet in the store with form data
 @ ` DELETE `  ` /pet/{petId} ` | Deletes a pet
 @ ` POST `  ` /pet/{petId}/uploadImage ` | uploads an image
 @ ` GET `  ` /store/inventory ` | Returns pet inventories by status
 @ ` POST `  ` /store/order ` | Place an order for a pet
 @ ` GET `  ` /store/order/{orderId} ` | Find purchase order by ID
 @ ` DELETE `  ` /store/order/{orderId} ` | Delete purchase order by ID
 @ ` POST `  ` /user ` | Create user
 @ ` POST `  ` /user/createWithArray ` | Creates list of users with given input array
 @ ` POST `  ` /user/createWithList ` | Creates list of users with given input array
 @ ` GET `  ` /user/login ` | Logs user into the system
 @ ` GET `  ` /user/logout ` | Logs out current logged in user session
 @ ` GET `  ` /user/{username} ` | Get user by user name
 @ ` PUT `  ` /user/{username} ` | Updated user
 @ ` DELETE `  ` /user/{username} ` | Delete user


## The Elaborated Report for Changed Endpoints 
###  @ `/pet`

####     Operations:

#####   *` C   `* `POST`  *`/pet`* -> compliance: `false` 

#### Breaking Changes
  `   no observations    `
#### Potentially Breaking Changes
  `   no observations    `
#####   *` C   `* `PUT`  *`/pet`* -> compliance: `false` 

#### Breaking Changes
  `   no observations    `
#### Potentially Breaking Changes
  `   no observations    `



###  @ `/pet/{petId}`

####     Operations:

#####   *` C   `* `POST`  *`/pet/{petId}`* -> compliance: `false` 

#### Breaking Changes
  `   no observations    `
#### Potentially Breaking Changes
  `   no observations    `
#####   *` C   `* `DELETE`  *`/pet/{petId}`* -> compliance: `false` 

#### Breaking Changes
  `   no observations    `
#### Potentially Breaking Changes
  `   no observations    `



###  @ `/user`

####     Operations:

#####   *` C   `* `POST`  *`/user`* -> compliance: `false` 

#### Breaking Changes
  `   no observations    `
#### Potentially Breaking Changes
  `   no observations    `



###  @ `/user/login`

####     Operations:

#####   *` C   `* `GET`  *`/user/login`* -> compliance: `false` 

#### Breaking Changes
  `   no observations    `
#### Potentially Breaking Changes
  `   no observations    `



###  @ `/user/{username}`

####     Operations:

#####   *` C   `* `PUT`  *`/user/{username}`* -> compliance: `false` 

#### Breaking Changes
  `   no observations    `
#### Potentially Breaking Changes
  `   no observations    `





## The Elaborated Compliance Report

###  @ `/pet`

####     Operations:

#####   *` C   `* `POST`  *`/pet`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  `   no observations    `
##### Potentially Breaking Changes
  `   no observations    `
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  `   no observations    `
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
 ` > ` `+ ` *tags*  | *Required* 
##### Recorded Changes to Properties in API
  Changes to Properties (Body Definitions)      |  Info
------------------------------------------------- | -------
 ` > ` `+ ` *body.newField*  | *Optional* 
 ` > ` `+ ` *body.category.newCatField*  | *Optional* 
##### Recorded Changes to Responses in API
  `   no observations    `
#####   *` C   `* `PUT`  *`/pet`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  `   no observations    `
##### Potentially Breaking Changes
  `   no observations    `
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  `   no observations    `
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  `   no observations    `
##### Recorded Changes to Properties in API
  Changes to Properties (Body Definitions)      |  Info
------------------------------------------------- | -------
 ` > ` `+ ` *body.newField*  | *Optional* 
 ` > ` `+ ` *body.category.newCatField*  | *Optional* 
##### Recorded Changes to Responses in API
  `   no observations    `



###  @ `/pet/{petId}`

####     Operations:

#####   *` C   `* `POST`  *`/pet/{petId}`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  `   no observations    `
##### Potentially Breaking Changes
  `   no observations    `
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  `   no observations    `
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
 ` > ` `+ ` *newFormDataParam*  | *Optional* 
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  `   no observations    `
#####   *` C   `* `DELETE`  *`/pet/{petId}`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  `   no observations    `
##### Potentially Breaking Changes
  `   no observations    `
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  `   no observations    `
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
 ` > ` `+ ` *newHeaderParam*  | *Optional* 
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  `   no observations    `



###  @ `/user`

####     Operations:

#####   *` C   `* `POST`  *`/user`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  `   no observations    `
##### Potentially Breaking Changes
  `   no observations    `
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  `   no observations    `
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  `   no observations    `
##### Recorded Changes to Properties in API
  Changes to Properties (Body Definitions)      |  Info
------------------------------------------------- | -------
 ` > ` `+ ` *body.newUserField*  | *Optional* 
##### Recorded Changes to Responses in API
  `   no observations    `



###  @ `/user/login`

####     Operations:

#####   *` C   `* `GET`  *`/user/login`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  `   no observations    `
##### Potentially Breaking Changes
  `   no observations    `
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  `   no observations    `
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  Changes to Parameters (Headers, Query,...)      |  Info
------------------------------------------------- | -------
 ` > ` **`- `** **password | **io.swagger.models.parameters.QueryParameter@c551fd2c** 
##### Recorded Changes to Properties in API
  `   no observations    `
##### Recorded Changes to Responses in API
  `   no observations    `



###  @ `/user/{username}`

####     Operations:

#####   *` C   `* `PUT`  *`/user/{username}`* -> compliance: `false` 

#### Design Issues
  `   no observations    `
##### Breaking Changes
  `   no observations    `
##### Potentially Breaking Changes
  `   no observations    `
##### Recorded Changes
  `   no observations    `
##### Recorded Flaws in existing API
  `   no observations    `
##### Recorded Changes to Content-Types in API
  `   no observations    `
##### Recorded Changes to Parameters in API
  `   no observations    `
##### Recorded Changes to Properties in API
  Changes to Properties (Body Definitions)      |  Info
------------------------------------------------- | -------
 ` > ` `+ ` *body.newUserField*  | *Optional* 
##### Recorded Changes to Responses in API
  `   no observations    `




