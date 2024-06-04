// https://www.apollographql.com/docs/apollo-server/data/resolvers
export const resolvers = {
  Query: {
    allBusinesses: (parent, args, contextValue, info) => {
      return contextValue.db.businesses;
    },
    businessBySearchTerm: (parent, args, contextValue, info) => {
      const compare = (a, b) => {
        const [orderField, order] = args.orderBy.split("_");
        const left = a[orderField],
          right = b[orderField];
        if (left < right) {
          return order === "asc" ? -1 : 1;
        } else if (left > right) {
          return order === "desc" ? -1 : 1;
        } else {
          return 0;
        }
      };
      return contextValue.db.businesses
        .filter(v => {
          return v["name"].indexOf(args.search) !== -1;
        })
        .slice(args.offset, args.first)
        .sort(compare);
    }
  },

  Business: {
    reviews: (parent, args, contextValue, info) => {
      return obj.reviewIds.map(v => {
        return contextValue.db.reviews.find(review => {
          return review.reviewId === v;
        });
      });
    },
    avgStars: (parent, args, contextValue, info) => {
      const reviews = obj.reviewIds.map(v => {
        return contextValue.db.reviews.find(review => {
          return review.reviewId === v;
        });
      });
      return (
        reviews.reduce((acc, review) => {
          return acc + review.stars;
        }, 0) / reviews.length
      );
    }
  },

  Review: {
    user: (parent, args, contextValue, info) => {
      return contextValue.db.users.find(user => {
        return user.userId === obj.userId;
      });
    },
    business: (parent, args, contextValue, info) => {
      return contextValue.db.businesses.find((b) => {
        return b.businessId === obj.businessId;
      });
    }
  },

  User: {
    reviews: (parent, args, contextValue, info) => {
      return obj.reviewIds.map((v) => {
        return contextValue.db.reviews.find((review) => {
          return review.reviewId === v;
        });
      });
    },
  }
};