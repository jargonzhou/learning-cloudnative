import dotenv from 'dotenv';

console.log('env', process.cwd())
dotenv.config({
  // path: 'api/.env', // DEBUG
  // debug: true
});


export const isDev = process.env.NODE_ENV !== 'production';

export const port = process.env.PORT;

export const pgConnectionString = process.env.PG_CONNECTION_STRING;

export const mdbConnectionString = process.env.MDB_CONNECTION_STRING;
