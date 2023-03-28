# Whitebox Accounts
On-Line bank account using event-source, domain events and spring boot.

## Code Structure
### Domain
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
