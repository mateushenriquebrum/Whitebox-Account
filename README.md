# Whitebox Accounts
On-Line bank account using event-source, domain events and spring boot.

## Code Structure
#### Domain
Domains are objects that act as pure data structures and algorithms, they should represent the core ideas in a business like: Account, Customer, Debit, Credit, etc. 
It is easy to test, compose and reason about, also it is the most likely piece of code to be changed when the business evolves.
### Application
Applications are coarse-grained domain cases, the main goal is to coordinate the domain interaction and access external impure resources like: database, message system, etc. in a safe way through abstraction. 
They don't tend to change aggressively as the business evolves, usually new coordinate processes are created (use cases) using|composing the domain. 
They are ease to test with mocks, but usually do not aggregate huge value due its simplicity, should expect little|none logic here.
### Infrastructure
Infrastructure is all impure code, a.k.a IOs, it aims to deal with non-deterministic behavior, also responsible for mapping some contexts and protecting the application from exceptional cases.
It is hard to test, failure prone and show loads of boilerplates, the only feasible way to test this layer is by e2e aiming to test the correctness of assembled parts, also depending on the level of complexity, a functional test is enough. 
It tends to change little once stable.

## Consistence Boundaries
### Aggregators - Business Transaction
Aggregators are some kind of business boundaries (business transactions), ensure that all entities are consistence flow the whole use case flow, a example of this idea is [Bank](https://github.com/mateushenriquebrum/OnLineBank/blob/main/src/main/java/de/whitebox/application/bank/Bank.java), as it validates, create, compose the whole structure in a consistent way, also all the entities should be over the discretion of shared data boundaries (system transactions).
### Optimistic Lock - System Transactioin
Most applications with low concurrency have a better result working with Optimistic Locks in such a way it diminishes or eliminates database retention, also it is so generic that it can be applied in any kind of shared data, e.g Memory, File, Database, etc.

## Event-Source
By the contrary of many applications, it uses a behavioral persistence, it keeps tracking of all discret changes in a domain, serializes it and when needed recompose reducing to the latest state. The main advantage is that we understand the whole entity's life cycle, facilitating debugging, tracking logic flaws, improving observability and as a side effect it leverages domain events consecutively CQSR.

## API (Query)
They can be checked here using (OpenAPI)[http://localhost:8080/swagger-ui/index.html].
