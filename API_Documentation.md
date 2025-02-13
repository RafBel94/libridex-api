# API Documentation

## Authentication Endpoints

### **Login**
#### URL
```
https://api.example.com/api/auth/login
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
| Status Code | Description                     |
|-------------|---------------------------------|
| 200         | Login successful. Returns email and JWT token.       |
| 400         | Validation errors               |
| 401         | Unauthorized request            |

---

#### Success Example
![login success](https://i.imgur.com/K4FDyOt.png)
<br>

### **Register**
#### URL
```
https://api.example.com/api/auth/register
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
| repeatPassword      | String     | Body      | Yes      | User's password                    |

#### Request example
##### Inside body -> raw -> JSON in PostMan
```json
{
  "email": "user@example.com",
  "password": "securepassword123",
  "repeatPassword": "securepassword123",
}
```

#### Possible Responses
| Status Code | Description                     |
|-------------|---------------------------------|
| 201         | User registered successfully      |
| 400         | Validation errors               |
---

#### Success Example
![register success](https://i.imgur.com/Zmjsz18.png)
<br>

## Books Endpoints

### **Add Book**
#### URL
```
https://api.example.com/api/books
```

#### HTTP Method
```
POST
```

#### Parameters
| Name         | Type       | Location  | Required | Description                        |
|--------------|------------|-----------|----------|------------------------------------|
| Authorization | String     | Header    | Yes      | Bearer token for authentication   |
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
| Status Code | Description                     |
|-------------|---------------------------------|
| 201         | Book successfully created       |
| 400         | Validation errors               |
| 401         | Unauthorized request            |

---

#### Success Example
![add book success](https://i.imgur.com/1sg3kgA.png)
<br>

### **Update Book**
#### URL
```
https://api.example.com/api/books/{id}
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
| Status Code | Description                     |
|-------------|---------------------------------|
| 200         | Book successfully updated       |
| 400         | Validation errors               |
| 401         | Unauthorized request            |
| 404         | Book not found                  |

---
#### Success Example
![update book success](https://i.imgur.com/ieTAgqE.png)
<br>

### **Get All Books**
#### URL
```
https://api.example.com/api/books
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
| Status Code | Description                     |
|-------------|---------------------------------|
| 200         | List of books retrieved         |
| 401         | Unauthorized request            |

---
#### Success Example
![get all books success](https://i.imgur.com/c9lJbtD.png)
<br>

### **Get Book by ID**
#### URL
```
https://api.example.com/api/books/{id}
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
| Status Code | Description                     |
|-------------|---------------------------------|
| 200         | Book details retrieved          |
| 401         | Unauthorized request            |
| 404         | Book not found                  |

---
#### Success Example
![get book by id success](https://i.imgur.com/1TiDacQ.png)
<br>

### **Delete Book by ID**
#### URL
```
https://api.example.com/api/books/{id}
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
| Status Code | Description                     |
|-------------|---------------------------------|
| 200         | Book successfully deleted       |
| 401         | Unauthorized request            |
| 404         | Book not found                  |

---
#### Success Example
![delete book by id success](https://i.imgur.com/DOyUDNx.png)
<br>

### **Search Books**
#### URL
```
https://api.example.com/api/books/search
```

#### HTTP Method
```
GET
```

#### Parameters
| Name                  | Type       | Location  | Required | Description                                |
|-----------------------|------------|-----------|----------|--------------------------------------------|
| Authorization         | String     | Header    | Yes      | Bearer token for authentication           |
| authors               | List       | Params    | No       | List of authors to filter by              |
| genres                | List       | Params    | No       | List of genres to filter by               |
| sortBy                | String     | Params    | No       | Field to sort results by                  |
| beforePublishingDate  | String     | Params    | No       | Upper limit for publishing date filter    |
| afterPublishingDate   | String     | Params    | No       | Lower limit for publishing date filter    |

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
| Status Code | Description                     |
|-------------|---------------------------------|
| 200         | Books retrieved successfully    |
| 401         | Unauthorized request            |
| 400         | Invalid query parameters        |

---
#### Success Example
![search books success](https://i.imgur.com/FnKl5ZD.png)
