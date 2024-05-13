import { GraphQLSchema, GraphQLObjectType, GraphQLString, GraphQLNonNull, GraphQLInt, printSchema, GraphQLList, GraphQLID } from 'graphql';
import { Task } from './task.js';
import { isDev } from '../config.js';
import { SearchResultItem } from './search.js';
import { AuthInput, UserInput, UserPayload } from './user.js';

// ========================================================
// GraphQL Schema
// ========================================================

// types
export const NumebrsInRange = new GraphQLObjectType({
  name: 'NumebrsInRange',
  description: 'Aggregate info on a range of numbers.',
  fields: {
    sum: {
      description: 'Sum on a range of numbers',
      type: new GraphQLNonNull(GraphQLInt)
    },
    count: {
      description: 'Count on a range of numbers.',
      type: new GraphQLNonNull(GraphQLInt)
    }
  }
})

// schema
const queryType = new GraphQLObjectType({
  name: 'Query',
  description: '',
  fields: {
    // ========================================================
    hello: {
      type: GraphQLString,
      resolve: () => 'world',
    },
    // ========================================================
    currentTime: {
      type: GraphQLString,
      resolve: () => {
        const isoString = new Date().toISOString()
        return isoString
      }
    },
    // ========================================================
    // {
    //   sumNumbersInRange(begin: 1, end: 10) {
    //     sum
    //     count
    //   }
    // }
    sumNumbersInRange: {
      type: NumebrsInRange,
      args: {
        begin: {
          description: 'Begin of the range',
          type: new GraphQLNonNull(GraphQLInt)
        },
        end: {
          description: 'End of the range',
          type: new GraphQLNonNull(GraphQLInt)
        }
      },
      resolve: function (source, { begin, end }) {
        if (end < begin) {
          throw new Error(`Invalid range because end ${end} < begin ${begin}`)
        }

        let sum = 0
        let count = 0
        for (let i = begin; i <= end; i++) {
          sum += i
          count++
        }
        return { sum, count }
      }
    },
    // ========================================================
    taskMainList: {
      type: new GraphQLList(new GraphQLNonNull(Task)),
      resolve: async (source, args, { pgApi }, info) => {
        return await pgApi.taskMainList()
      }
    },
    // ========================================================
    taskInfo: {
      type: Task,
      args: {
        id: {
          type: new GraphQLNonNull(GraphQLID)
        }
      },
      resolve: async (source, args, { pgApi, loaders }, info) => {
        return loaders.tasks.load(args.id)
      }
    },
    // ========================================================
    search: {
      type: new GraphQLNonNull(new GraphQLList(new GraphQLNonNull(SearchResultItem))),
      args: {
        term: {
          type: new GraphQLNonNull(GraphQLString)
        }
      },
      resolve: async (source, args, { pgApi, loaders }, info) => {
        return loaders.searchResults.load(args.term)
      }
    }

  }
})

const mutationType = new GraphQLObjectType({
  name: 'Mutation',
  description: '',
  fields: () => ({
    // ========================================================
    userCreate: {
      description: '',
      type: new GraphQLNonNull(UserPayload),
      args: {
        input: {
          type: new GraphQLNonNull(UserInput)
        }
      },
      resolve: async (source, args, { mutators }, info) => {
        return mutators.userCreate(args.input)
      }
    },
    // ========================================================
    userLogin: {
      description: '',
      type: new GraphQLNonNull(UserPayload),
      args: {
        input: {
          description: '',
          type: new GraphQLNonNull(AuthInput)
        }
      },
      resolve: async (source, args, { mutators }, info) => {
        return mutators.userLogin(args.input)
      }
    }

  })
})


export const schema = new GraphQLSchema({
  query: queryType,
  mutation: mutationType
  // TODO: subscription
});

// output the schema
console.log(printSchema(schema))