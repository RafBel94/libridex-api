# API Documentation

## Authentication Endpoints

### **Login**
#### URL
```
https://libridex-api-8ym32.ondigitalocean.app/api/auth/login
```

#### HTTP Method
```
POST
```

#### Parameters
| Name          | Type       | Location  | Required | Description                        |
|---------------|------------|-----------|----------|------------------------------------|
| email         | String     | Body      | Yes      | User's email                       |
| password      | String     | Body      | Yes      | User's password                    |

#### Request example
##### Inside body -> raw -> JSON in PostMan
```json
{
  "email": "user@example.com",
  "password": "securepassword123"
}
```

#### Possible Responses
| Status Code | Description                     | Message                              | Data                                      |
|-------------|---------------------------------|--------------------------------------|-------------------------------------------|
| 200         | Login successful.               | "User logged in successfully"        | `email`, `token`, `role`                          |
| 400         | Validation errors.              | List of validation error messages    | Empty                                     |
| 401         | Unauthorized request.           | "Unauthorized request"               | Empty                                     |

---

#### Success Example
![login success](https://i.imgur.com/R8wiXgW.png)
<br>

### **Register**
#### URL
```
https://libridex-api-8ym32.ondigitalocean.app/api/auth/register
```

#### HTTP Method
```
POST
```

#### Parameters
| Name          | Type       | Location  | Required | Description                        |
|---------------|------------|-----------|----------|------------------------------------|
| email         | String     | Body      | Yes      | User's email                       |
| password      | String     | Body      | Yes      | User's password                    |
| repeatPassword| String     | Body      | Yes      | User's password (again)            |

#### Request example
##### Inside body -> raw -> JSON in PostMan
```json
{
  "email": "user@example.com",
  "password": "securepassword123",
  "repeatPassword": "securepassword123"
}
```

#### Possible Responses
| Status Code | Description                     | Message                              | Data                                      |
|-------------|---------------------------------|--------------------------------------|-------------------------------------------|
| 201         | User registered successfully.   | "User registered successfully"       | Empty                                     |
| 400         | Validation errors.              | List of validation error messages    | Empty                                     |

---

#### Success Example
![register success](https://i.imgur.com/nC5G9LW.png)
<br>

## Books Endpoints

### **Add Book**
#### URL
```
https://libridex-api-8ym32.ondigitalocean.app/api/books
```

#### HTTP Method
```
POST
```

#### Parameters
| Name         | Type       | Location  | Required | Description                        |
|--------------|------------|-----------|----------|------------------------------------|
| Authorization| String     | Header    | Yes      | Bearer token for authentication   |
| title        | String     | Body      | Yes      | Book title                        |
| author       | String     | Body      | Yes      | Book author                       |
| genre        | String     | Body      | Yes      | Book genre                        |
| publishingDate | String    | Body      | Yes      | Book publishing date in YYYY-MM-DD format |
| image        | String     | Body      | Yes      | URL of the book cover image       |

#### Request example
##### Inside body -> raw -> JSON in PostMan
```json
{
  "title": "Sample Book",
  "author": "Author Name",
  "genre": "Fiction",
  "publishingDate": "2023-01-01",
  "image": "https://example.com/book-cover.jpg"
}
```

#### Possible Responses
| Status Code | Description                     | Message                              | Data                                      |
|-------------|---------------------------------|--------------------------------------|-------------------------------------------|
| 201         | Book successfully created.      | "Book created successfully"          | Details of the created book               |
| 400         | Validation errors.              | List of validation error messages    | Empty                                     |
| 401         | Unauthorized request.           | "Unauthorized request"               | Empty                                     |

---

#### Success Example
![add book success](https://i.imgur.com/HCEdaMr.png)
<br>

### **Update Book**
#### URL
```
https://libridex-api-8ym32.ondigitalocean.app/api/books/{id}
```

#### HTTP Method
```
PUT
```

#### Parameters
| Name          | Type       | Location  | Required | Description                        |
|---------------|------------|-----------|----------|------------------------------------|
| Authorization | String     | Header    | Yes      | Bearer token for authentication   |
| id            | Integer    | Path      | Yes      | ID of the book to be updated      |
| title         | String     | Body      | No       | Updated book title               |
| author        | String     | Body      | No       | Updated book author              |
| genre         | String     | Body      | No       | Updated book genre               |
| publishingDate | String    | Body      | No       | Updated book publishing date in YYYY-MM-DD format |
| image         | String     | Body      | No       | Updated URL of the book cover image |

#### Request example
##### Inside body -> raw -> JSON in PostMan
```json
{
  "title": "Updated Book",
  "author": "Updated Author",
  "genre": "Drama",
  "publishingDate": "2023-02-01",
  "image": "https://example.com/new-cover.jpg"
}
```

#### Possible Responses
| Status Code | Description                     | Message                              | Data                                      |
|-------------|---------------------------------|--------------------------------------|-------------------------------------------|
| 200         | Book successfully updated.      | "Book updated successfully"          | Details of the updated book               |
| 400         | Validation errors.              | List of validation error messages    | Empty                                     |
| 401         | Unauthorized request.           | "Unauthorized request"               | Empty                                     |
| 404         | Book not found.                 | "Book with id {id} not found"        | Empty                                     |

---

#### Success Example
![update book success](https://i.imgur.com/0b6L1mK.png)
<br>

### **Get All Books**
#### URL
```
https://libridex-api-8ym32.ondigitalocean.app/api/books
```

#### HTTP Method
```
GET
```

#### Parameters
| Name          | Type       | Location  | Required | Description                        |
|---------------|------------|-----------|----------|------------------------------------|
| Authorization | String     | Header    | Yes      | Bearer token for authentication   |

#### Possible Responses
| Status Code | Description                     | Message                              | Data                                      |
|-------------|---------------------------------|--------------------------------------|-------------------------------------------|
| 200         | List of books retrieved.        | "Books retrieved successfully"       | List of all books                         |
| 401         | Unauthorized request.           | "Unauthorized request"               | Empty                                     |

---

#### Success Example
![get all books success](https://i.imgur.com/X3B5qSg.png)
<br>

### **Get Book by ID**
#### URL
```
https://libridex-api-8ym32.ondigitalocean.app/api/books/{id}
```

#### HTTP Method
```
GET
```

#### Parameters
| Name          | Type       | Location  | Required | Description                        |
|---------------|------------|-----------|----------|------------------------------------|
| Authorization | String     | Header    | Yes      | Bearer token for authentication   |
| id            | Integer    | Path      | Yes      | ID of the book to retrieve        |

#### Possible Responses
| Status Code | Description                     | Message                              | Data                                      |
|-------------|---------------------------------|--------------------------------------|-------------------------------------------|
| 200         | Book details retrieved.         | "Book retrieved successfully"        | Details of the retrieved book             |
| 401         | Unauthorized request.           | "Unauthorized request"               | Empty                                     |
| 404         | Book not found.                 | "Book with id {id} not found"        | Empty                                     |

---

#### Success Example
![get book by id success](https://i.imgur.com/eglh47b.png)
<br>

### **Delete Book by ID**
#### URL
```
https://libridex-api-8ym32.ondigitalocean.app/api/books/{id}
```

#### HTTP Method
```
DELETE
```

#### Parameters
| Name          | Type       | Location  | Required | Description                        |
|---------------|------------|-----------|----------|------------------------------------|
| Authorization | String     | Header    | Yes      | Bearer token for authentication   |
| id            | Integer    | Path      | Yes      | ID of the book to delete          |

#### Possible Responses
| Status Code | Description                     | Message                              | Data                                      |
|-------------|---------------------------------|--------------------------------------|-------------------------------------------|
| 200         | Book successfully deleted.      | "Book deleted successfully"          | Empty                                     |
| 401         | Unauthorized request.           | "Unauthorized request"               | Empty                                     |
| 404         | Book not found.                 | "Book with id {id} not found"        | Empty                                     |

---

#### Success Example
![delete book by id success](https://i.imgur.com/Gqph4D2.png)
<br>

### **Search Books**
#### URL
```
https://libridex-api-8ym32.ondigitalocean.app/api/books/search
```

#### HTTP Method
```
GET
```

#### Parameters
| Name                  | Type       | Location  | Required | Description                                      |
|-----------------------|------------|-----------|----------|--------------------------------------------------|
| Authorization         | String     | Header    | Yes      | Bearer token for authentication                  |
| authors               | List       | Params    | No       | List of authors to filter by                     |
| genres                | List       | Params    | No       | List of genres to filter by                      |
| sortBy                | String     | Params    | No       | Field to sort results by                         |
| beforePublishingDate  | String     | Params    | No       | Upper limit for publishing date filter           |
| afterPublishingDate   | String     | Params    | No       | Lower limit for publishing date filter           |
| query                 | String     | Params    | No       | Search query to filter by title, author or genre |

#### Valid `sortBy` Parameters
- `title_asc`
- `title_desc`
- `author_asc`
- `author_desc`
- `genre_asc`
- `genre_desc`
- `publishingDate_asc`
- `publishingDate_desc`
- `createdAt_asc`
- `createdAt_desc`

#### Request example
##### Inside PostMan query params
```json
genres -> Fiction, Terror, Fantasy
authors -> Author A, Author B
sortBy -> title_asc
beforePublishingDate -> 2023-12-31
afterPublishingDate -> 2023-01-01
```

#### Possible Responses
| Status Code | Description                     | Message                              | Data                                      |
|-------------|---------------------------------|--------------------------------------|-------------------------------------------|
| 200         | Books retrieved successfully.   | "Books retrieved successfully"       | List of books matching the filters        |
| 401         | Unauthorized request.           | "Unauthorized request"               | Empty                                     |
| 400         | Invalid query parameters.       | List of validation error messages    | Empty                                     |

---

#### Success Example
![search books success](https://i.imgur.com/Zc1485W.png)