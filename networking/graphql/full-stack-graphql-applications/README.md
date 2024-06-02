# Code of 'Full Stack GraphQL Applications'

```shell
npm install @apollo/server graphql
npm i -D nodemon
```

Operations:

```graphql
# P.43
query ExampleQuery {
  allBusinesses {
    name
  }
}

# P.46
query ExampleQuery {
  businessBySearchTerm(search: "Library") {
    businessId
    name
    address
    avgStars
  }
}

# P.47
query ExampleQuery {
  businessBySearchTerm(search: "Library") {
    businessId
    name
    address
    avgStars
    reviews {
      stars
      text
    }
  }
}

```