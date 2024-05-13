import { GraphQLID, GraphQLInterfaceType, GraphQLNonNull, GraphQLString } from "graphql";
// import { Task } from "./task.js";
// import { Approach } from "./approach.js";

export const SearchResultItem = new GraphQLInterfaceType({
  name: 'SearchResultItem',
  description: 'Search result items',
  fields: () => ({
    id: {
      description: 'ID of item',
      type: new GraphQLNonNull(GraphQLID)
    },
    content: {
      description: 'Content of item',
      type: new GraphQLNonNull(GraphQLString)
    }
  }),
  resolveType: (value, context, info, abstractType) => {
    // type is a filed in value: from sql
    if (value.type === 'task') {
      // Support for returning GraphQLObjectType from resolveType was removed in graphql-js@16.0.0 please return type name instead.
      return 'Task'
    }
    if (value.type === 'approach') {
      return 'Approach'
    }
  }
})