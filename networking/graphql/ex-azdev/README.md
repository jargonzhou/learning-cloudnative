# Example AZdev from 'GraphQL in Action'

```shell
npm init --yes
npm install nodemon --save-dev

# express-graphql: This package is no longer maintained. We recommend using `graphql-http` instead.
npm install graphql express-graphql
npm uninstall express-graphql
# https://github.com/graphql/graphql-http
npm install graphql-http
npm install express
npm install cors

npm intall dotenv
npm install pg

npm install dataloader

npm install mongodb
```


## refs

- [graphql-http doc](https://github.com/graphql/graphql-http/tree/main/docs)
- [graphql-http express](https://github.com/graphql/graphql-http/blob/main/docs/modules/use_express.md)
- [express](https://expressjs.com/)
- [express cors](https://expressjs.com/en/resources/middleware/cors.html)

## TODOs

- [ ] More mutator operations
- [ ] Pass around auth entity
- [ ] Aggregate app initialization setup
- [ ] GraphQL clients