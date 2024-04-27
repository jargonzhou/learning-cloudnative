# Example ApolloServer

- [Get started with Apollo Server](https://www.apollographql.com/docs/apollo-server/getting-started)

```shell
npm init --yes && npm pkg set type="module"
npm install @apollo/server graphql
npm install @apollo/sandbox --save-dev
npm start
```

Access `http://localhost:4000/`.

```graphql
# query
query GetBooks {
  books {
    title
    author
  }
}
# or
{
  books {
    title
    author
  }
}

# response
{
  "data": {
    "books": [
      {
        "title": "The Awakening",
        "author": "Kate Chopin"
      },
      {
        "title": "City of Glass",
        "author": "Paul Auster"
      }
    ]
  }
}
```