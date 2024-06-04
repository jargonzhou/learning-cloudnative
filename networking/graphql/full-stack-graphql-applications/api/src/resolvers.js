export const resolvers = {
  Business: {
    waitTime: (parent, args, contextValue, info) => {
      const options = [0, 5, 10, 15, 30, 45]
      return options[Math.floor(Math.random() * options.length)]
    }
  }
}