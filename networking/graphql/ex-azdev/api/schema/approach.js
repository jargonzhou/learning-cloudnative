import { GraphQLEnumType, GraphQLID, GraphQLInt, GraphQLList, GraphQLNonNull, GraphQLObjectType, GraphQLString } from "graphql";
import { User } from "./user.js";
import { Task } from "./task.js";
import { isDev } from "../config.js";
import { SearchResultItem } from "./search.js";

// ref: How to use the graphql.GraphQLEnumType function in graphql
// https://snyk.io/advisor/npm-package/graphql/functions/graphql.GraphQLEnumType
export const ApproachDetailCategory = new GraphQLEnumType({
  name: 'ApproachDetailCategory',
  description: 'Approach detail category',
  values: {
    NOTE: {
      description: 'Note',
      value: 'NOTE'
    },
    EXPLANATION: {
      description: 'Explanation',
      value: 'EXPLANATION'
    },
    WARNING: {
      description: 'Warning',
      value: 'WARNING'
    }
  }
})

export const ApproachDetail = new GraphQLObjectType({
  name: 'ApproachDetail',
  description: 'Approach details',
  fields: {
    content: {
      description: 'Content of approach detail',
      type: new GraphQLNonNull(GraphQLString)
    },
    category: {
      description: 'Category of approach detail',
      type: ApproachDetailCategory
    }
  }
})

export const Approach = new GraphQLObjectType({
  name: 'Approach',
  description: 'Approaches',
  interfaces: () => [SearchResultItem], // interface
  description: 'Approaches',
  fields: () => ({ // syntax to solve Task ReferenceError
    id: {
      description: 'Approach ID',
      type: new GraphQLNonNull(GraphQLID)
    },
    content: {
      description: 'Content of approach',
      type: new GraphQLNonNull(GraphQLString)
    },
    createdAt: {
      description: 'Approach create time',
      type: new GraphQLNonNull(GraphQLString)
    },
    author: {
      description: 'Authro of approach',
      type: new GraphQLNonNull(User),
      // resolve: (source, args, { pgApi }, info) => pgApi.userInfo(source.userId)
      // dataloader
      resolve: (source, args, { pgApi, loaders }, info) => loaders.users.load(source.userId)
    },
    detailList: {
      description: 'Details of approach',
      type: new GraphQLNonNull(new GraphQLList(new GraphQLNonNull(ApproachDetail))),
      resolve: (source, args, { pgApi, loaders }, info) => loaders.detailLists.load(source.id)
    },
    voteCount: {
      description: 'Vote count of approach',
      type: new GraphQLNonNull(GraphQLInt)
    },
    task: {
      description: 'The task of approach',
      type: new GraphQLNonNull(Task), // ReferenceError: Cannot access 'Task' before initialization
      resolve: (source, args, { pgApi, loaders }, info) => {
        // if (isDev) {
        //   console.log("info.path", info.path)
        // }
        return loaders.tasks.load(source.taskId)
      }
    }
  })
})