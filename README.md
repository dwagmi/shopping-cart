# BCG Coding Challenge - Backend

Add swagger [docs](https://www.baeldung.com/spring-rest-openapi-documentation)

Available at
http://localhost:8080/swagger-ui/index.html
or
http://localhost:8080/docs


## Database 
In-memory H2 database. 
Console available at http://localhost:8080/h2-console

### Hibernate inheritance 
- Used for Promotion parent class to child Promotion classes 
- https://www.baeldung.com/hibernate-inheritance
- Many to Many: https://www.baeldung.com/hibernate-many-to-many


## Assumptions 

- Items have to be added to cart for freeItem promotion to apply, it is not automatically added. 
## How to improve 
- Handle more exceptions and errors 
- Full E2E testing. 
- Introduce user sessions to cart. 
- Add cart expiry
- Make it more robust. 
- Add a frontend. 
- Handle negative quantities.
- Handling out of stock/optimistic updates
- Refactor clean code, extract out repeat funcs
- Make it more robust to be production ready - 
  - which parts can be read-only/immutable
  - concurrent updates. 