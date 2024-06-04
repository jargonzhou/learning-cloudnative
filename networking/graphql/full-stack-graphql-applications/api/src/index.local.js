import { ApolloServer } from '@apollo/server';
import { startStandaloneServer } from '@apollo/server/standalone';

import { db } from './db.js';
import { resolvers } from './resolvers.local.js';

// ========================================================
// GraphQL Schema
// ========================================================

const typeDefs = `
type Business {
  businessId: ID!
  name: String
  address: String
  avgStars: Float
  photos(first: Int = 3, offset: Int = 0): [Photo!]!
  reviews(first: Int = 3, offset: Int = 0): [Review!]!
}

type User {
  userId: ID!
  name: String
  photos: [Photo!]!
  reviews: [Review!]!
}

type Photo {
  business: Business!
  user: User!
  photoId: ID!
  url: String
}

type Review {
  reviewId: ID!
  stars: Float
  text: String
  user: User!
  business: Business!
}

enum BusinessOrdering {
  name_asc
  name_desc
}

type Query {
  allBusinesses(first: Int = 10, offset: Int = 0): [Business!]!
  businessBySearchTerm(
    search: String!
    first: Int = 10
    offset: Int = 0
    orderBy: BusinessOrdering = name_asc
  ): [Business]
  userById(id: ID!): User
}
`

const server = new ApolloServer({
  typeDefs,
  resolvers,
})

const { url } = await startStandaloneServer(server, {
  listen: { port: 4000 },
  // https://www.apollographql.com/docs/apollo-server/data/context
  context: async ({ req, res }) => ({
    db: db
  })
});
console.log(`Server ready at ${url}`)