import { GraphQLID, GraphQLInputObjectType, GraphQLList, GraphQLNonNull, GraphQLObjectType, GraphQLString } from "graphql";
import { Task } from "./task.js";

export const User = new GraphQLObjectType({
  name: 'User',
  description: 'Users',
  fields: {
    id: {
      description: 'User ID',
      type: new GraphQLNonNull(GraphQLID)
    },
    name: {
      description: 'Name of user',
      type: GraphQLString,
      resolve: ({firstName, lastName}, args, context, info) => [firstName, lastName].filter(e => e !== null).join(' ')
    },
    username: {
      description: 'Username of User',
      type: new GraphQLNonNull(GraphQLString)
    },
    // taskList: {
    //   description: '',
    //   type: new GraphQLNonNull(
    //     new GraphQLList(new GraphQLNonNull(Task))
    //   )
    // }
  }
})

// ========================================================
// mutations
// ========================================================

export const UserError = new GraphQLObjectType({
  name: 'UserError',
  description: '',
  fields: () => ({
    message: {
      description: '',
      type: new GraphQLNonNull(GraphQLString)
    }
  })
})

export const UserPayload = new GraphQLObjectType({
  name: 'UserPayload',
  description: '',
  fields: () => ({
    errors: {
      description: '',
      type: new GraphQLNonNull(new GraphQLList(new GraphQLNonNull(UserError)))
    },
    user: {
      description: '',
      type: User
    },
    authToken: {
      description: '',
      type: GraphQLString
    }
  })
})

export const UserInput = new GraphQLInputObjectType({
  name: 'UserInput',
  description: '',
  fields: () => ({
    username: {
      description: '',
      type: new GraphQLNonNull(GraphQLString)
    },
    password: {
      description: '',
      type: new GraphQLNonNull(GraphQLString)
    },
    firstName: {
      description: '',
      type: GraphQLString
    },
    lastName: {
      description: '',
      type: GraphQLString
    }
  })
})

export const AuthInput = new GraphQLInputObjectType({
  name: 'AuthInput',
  description: '',
  fields: () => ({
    username: {
      description: '',
      type: new GraphQLNonNull(GraphQLString)
    },
    password: {
      description: '',
      type: new GraphQLNonNull(GraphQLString)
    }
  })
})