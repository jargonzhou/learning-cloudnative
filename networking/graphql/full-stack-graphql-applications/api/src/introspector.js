import {toGraphQLTypeDefs} from '@neo4j/introspector'
import neo4j from 'neo4j-driver'
import fs from 'fs'

const driver = neo4j.driver(
  "bolt://localhost:7687",
  neo4j.auth.basic("neo4j", "neo4j+devops")
)
const sessionFactory = () =>
  driver.session({defaultAccessMode: neo4j.session.READ})

async function main() {
  const typeDefs = await toGraphQLTypeDefs(sessionFactory)
  const content = `# Generated at ${new Date()}\n${typeDefs}`
  fs.writeFileSync('../graphql/schema.graphql', content)
  await driver.close()
}
main()