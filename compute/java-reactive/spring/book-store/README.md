# An example from 'Cloud Native Spring in Action'

components:
- config-repo
  - A Git repository with branch `master`.
- config-service
  - A Spring Cloud Config Server project with `config-repo` (a git repository).
- catalog-service
  - A Spring Web MVC project with role OAuth2 Resource Service.
- order-service
  - A Spring WebFlux project with role OAuth2 Resource Service.
- edge-service
  - A Spring Cloud Gateway project with Resilience4j, Keycloak, role OAuth2 Client.
- dispatcher-service
  - TODO.