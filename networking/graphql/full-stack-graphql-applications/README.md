# Code of 'Full Stack GraphQL Applications'

## API

```shell
npm install @apollo/server graphql
npm i -D nodemon

# export DEBUG=@neo4j/graphql:*
npm install @neo4j/graphql
npm install neo4j-driver

npm i @neo4j/introspector --save-dev
```

- [index.local.js](./api/src/index.local.js): with static data for resolver implementation
  - Operations: [static-data.graphql](./graphql/static-data.graphql)
- [index.js](./api/src/index.js): use Neo4j GraphQL library
  - populate data: `:play grandstack` in Neo4j Browser
  - debug: `export DEBUG=@neo4j/graphql:*`
  - Operations: [neo4j-graphql.graphql](./graphql/neo4j-graphql.graphql)
- [introspector.js](./api/src/introspector.js): inspect GraphQL schema from Neo4j database
  - `node src/introspector.js`
  - output: [schema.graphql](./graphql/schema.graphql)

## React Client

```shell
npx create-react-app web-react --use-npm
cd web-react
npm install @apollo/client graphql
```

- [index.js](./web-react/src/index.js): `ApolloProvider`
- [App.js](./web-react/src/App.js): `App`
- [BusinessResults.js](./web-react/src/BusinessResults.js): `BusinessResults`