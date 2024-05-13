import { GraphQLID, GraphQLInt, GraphQLList, GraphQLNonNull, GraphQLObjectType, GraphQLString } from "graphql";
import { User } from "./user.js";
import { extractPrefixedColumns } from "../utils.js";
import { Approach } from "./approach.js";
import { SearchResultItem } from "./search.js";

export const Task = new GraphQLObjectType(
  {
    name: 'Task',
    interfaces: () => [SearchResultItem], // interface
    description: 'Tasks',
    fields: {
      id: {
        description: 'Task ID',
        type: new GraphQLNonNull(GraphQLID)
      },
      content: {
        description: 'Task content',
        type: new GraphQLNonNull(GraphQLString)
      },
      createdAt: {
        description: 'Task create time',
        type: new GraphQLNonNull(GraphQLString),
        resolve: (source, args, context, info) => source.createdAt.toISOString()
      },
      author: {
        description: 'Author of task',
        type: new GraphQLNonNull(User),
        // N+1 queries problem
        // resolve: (source, args, { pgApi }, info) => pgApi.userInfo(source.userId)
        // 1->1
        // resolve: (source, args, { pgApi }, info) => extractPrefixedColumns({prefixedObject: source, prefix: 'author'})
        // dataloader
        resolve: (source, args, { pgApi, loaders }, info) => loaders.users.load(source.userId)
      },
      approachCount: {
        description: 'Approach count of task',
        type: new GraphQLNonNull(GraphQLInt)
      },
      approachList: {
        description: 'Approaches for task',
        type: new GraphQLNonNull(new GraphQLList(new GraphQLNonNull(Approach))),
        // resolve: (source, args, {pgApi}, info) => pgApi.approachList(source.id)
        // dataloader
        resolve: (source, args, {pgApi, loaders}, info) => loaders.approachLists.load(source.id)
      },
      tags: {
        description: 'Tags of task',
        type: new GraphQLNonNull(new GraphQLList(new GraphQLNonNull(GraphQLString))),
        resolve: (source, args, context, info) => source.tags.split(',')
      }
    },

  }
)