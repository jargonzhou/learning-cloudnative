
import { ApolloServer } from "@apollo/server"
import { startStandaloneServer } from '@apollo/server/standalone';

import neo4j from 'neo4j-driver'
import { Neo4jGraphQL } from '@neo4j/graphql'

import {resolvers} from './resolvers.js'

const driver = neo4j.driver(
  "bolt://localhost:7687",
  neo4j.auth.basic("neo4j", "neo4j+devops")
)

const typeDefs = `
type Business {
  businessId: ID!
  waitTime: Int! @customResolver # @ignore
  averageStars: Float! 
    @cypher(
      statement: "MATCH (this)<-[:REVIEWS]-(r:Review) RETURN avg(r.stars) AS averageStars ", columnName: "averageStars"
    )
  recommended(first: Int = 1): [Business!]!
    @cypher(
      statement: """
      MATCH (this)<-[:REVIEWS]-(:Review)<-[:WROTE]-(u:User)
      MATCH (u)-[:WROTE]->(:Review)-[:REVIEWS]->(rec:Business)
      WITH rec, COUNT(*) AS score
      RETURN rec ORDER BY score DESC LIMIT $first
      """,
      columnName: "rec"
    )
  name: String!
  city: String!
  state: String!
  address: String!
  location: Point!
  reviews: [Review!]! @relationship(type: "REVIEWS", direction: IN)
  categories: [Category!]! @relationship(type: "IN_CATEGORY", direction: OUT)
}

type User {
  userID: ID!
  name: String!
  reviews: [Review!]! @relationship(type: "WROTE", direction: OUT)
}

type Review {
  reviewId: ID!
  stars: Float!
  date: Date!
  text: String
  user: User! @relationship(type: "WROTE", direction: IN)
  business: Business! @relationship(type: "REVIEWS", direction: OUT)
}

type Category {
  name: String!
  businesses: [Business!]! @relationship(type: "IN_CATEGORY", direction: IN)
}

# P.93
type Query {
  fuzzyBusinessByName(searchString: String): [Business]
    @cypher(
      statement: """
      CALL db.index.fulltext.queryNodes('businessNameIndex', $searchString + '~')
      YIELD node 
      RETURN node AS n
      """, 
      columnName: "n"
    )
}
`

const neo4jSchema = new Neo4jGraphQL({ typeDefs, resolvers, driver })
neo4jSchema.getSchema()
.then(async (schema) => {
  const server = new ApolloServer({ schema })
  const { url } = await startStandaloneServer(server, {
    listen: { port: 4000 },
  });
  console.log(`Server ready at ${url}`)
})
.catch((reason) => {
  console.log(reason)
})