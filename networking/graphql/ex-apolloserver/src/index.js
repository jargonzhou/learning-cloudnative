import { ApolloServer } from '@apollo/server';
import { startStandaloneServer } from '@apollo/server/standalone';
import { ApolloServerPluginLandingPageLocalDefault, ApolloServerPluginLandingPageProductionDefault } from '@apollo/server/plugin/landingPage/default';


// ========================================================
// GraphQL Schema
// ========================================================

// A schema is a collection of type definitions (hence "typeDefs")
// that together define the "shape" of queries that are executed against
// your data.
const typeDefs = `#graphql
  # Comments in GraphQL strings (such as this one) start with the hash (#) symbol.

  # This "Book" type defines the queryable fields for every book in our data source.
  type Book {
    title: String
    author: String
  }

  # The "Query" type is special: it lists all of the available queries that
  # clients can execute, along with the return type for each. In this
  # case, the "books" query returns an array of zero or more Books (defined above).
  type Query {
    books: [Book]
  }
`;

// ========================================================
// data set
// ========================================================

const bookDataSet = [
  {
    title: 'The Awakening',
    author: 'Kate Chopin',
  },
  {
    title: 'City of Glass',
    author: 'Paul Auster',
  },
];

// ========================================================
// resolver
// ========================================================

// Resolvers define how to fetch the types defined in your schema.
// This resolver retrieves books from the "books" array above.
const resolvers = {
  Query: {
    books: () => bookDataSet,
  },
};

// ========================================================
// ApolloServer instance
//
// api:
// https://www.apollographql.com/docs/apollo-server/api/apollo-server
// 
// plugins:
// Landing pages: https://www.apollographql.com/docs/apollo-server/api/plugin/landing-pages
// ========================================================

// The ApolloServer constructor requires two parameters: your schema
// definition and your set of resolvers.
const server = new ApolloServer({
  typeDefs,
  resolvers,

  plugins: [
    // Install a landing page plugin based on NODE_ENV
    process.env.NODE_ENV === 'production'
      ? ApolloServerPluginLandingPageProductionDefault({
        graphRef: 'my-graph-id@my-graph-variant',
        footer: false,
      })
      : ApolloServerPluginLandingPageLocalDefault({
        footer: false,
      }),
  ],
});

// Passing an ApolloServer instance to the `startStandaloneServer` function:
//  1. creates an Express app
//  2. installs your ApolloServer instance as middleware
//  3. prepares your app to handle incoming requests
const { url } = await startStandaloneServer(server, {
  listen: { port: 4000 },
});

console.log(`🚀  Server ready at: ${url}`);

