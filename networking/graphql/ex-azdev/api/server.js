import express from 'express'; // yarn add express
import { createHandler } from 'graphql-http/lib/use/express';
import cors from 'cors'
import { schema } from './schema/index.js';
import { PgApi } from './db/pg.js';
import { isDev } from './config.js';
import DataLoader from 'dataloader'
import { MongoApi } from './db/mongo.js';

// TODO: aggregate app initialization setup method

// ========================================================
// Data loader
// ========================================================
const pgApi = await PgApi()
const mongoApi = await MongoApi()
const loaders = {
  users: new DataLoader(async (userIds) => pgApi.usersInfo(userIds)),
  approachLists: new DataLoader(async (taskIds) => pgApi.approachLists(taskIds)),
  tasks: new DataLoader(async (taskIds) => pgApi.tasksInfo(taskIds)),
  searchResults: new DataLoader(async (searchItems) => pgApi.searchResults(searchItems)),

  detailLists: new DataLoader(async (approachIds) => mongoApi.detailLists(approachIds))
}
// TODO: more mutator operations
const mutators = {
  ...pgApi.mutators,
  ...mongoApi.mutators
}

// ========================================================
// Create a express instance serving all methods on `/graphql`
// where the GraphQL over HTTP express request handler is
// ========================================================

const app = express();
app.use(cors())
const authHandler = async (req, res, next) => {
  const authToken =
    req && req.headers && req.headers.authorization
      ? req.headers.authorization.slice(7) // "Bearer "
      : null;
  const currentUser = await pgApi.userFromAuthToken(authToken);
  if (authToken && !currentUser) {
    return res.status(401).send({
      errors: [{ message: 'Invalid access token' }],
    });
  }
  if (isDev) {
    console.log('authToken', authToken, 'currentUser', currentUser)
  }

  next()
}

const graphqlHandler = createHandler({
  schema: schema,
  context: {
    pgApi: pgApi,
    loaders: loaders,
    mutators: mutators
  },
  formatError: (err) => {
    const errorReport = {
      message: err.message,
      locations: err.locations,
      stack: err.stack ? err.stack.split('\n') : [],
      path: err.path,
    };
    if (isDev) {
      console.log(err)
      return errorReport
    }
    return err
  }
})

// TODO: pass around auth entity
app.all('/graphql', authHandler, graphqlHandler);

app.listen({ port: 4000 });
console.log('Listening to port 4000');