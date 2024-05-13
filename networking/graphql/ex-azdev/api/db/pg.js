
import pg from 'pg'
import { isDev, pgConnectionString } from '../config.js';
import { randomString } from '../utils.js';

// ========================================================
// Consutruct PostgreSQL client
// ========================================================

/**
 * @typedef PgClient
 * @type {object}
 * @property {pg.Pool} pgPool
 * @property {Function} pgClose
 */

// https://jsdoc.app/tags-type
/**
 * @returns {PgClient}
 */
async function pgClient() {
  const pgPool = new pg.Pool({
    connectionString: pgConnectionString,
    // log: (...messages) => {
    //   console.log(messages)
    // }
  });

  // Test the connection
  const client = await pgPool.connect();
  const tableCountResp = await client.query(
    'select count(*) from information_schema.tables where table_schema = $1;',
    ['azdev']
  );
  client.release();

  console.log(
    'Connected to PostgreSQL | Tables count:',
    tableCountResp.rows[0].count
  );

  pgPool.on('error', (err) => {
    console.error('Unexpected PG client error', err);
    process.exit(-1);
  });

  return {
    pgPool,
    pgClose: async () => await pgPool.end(),
  };
}


/**
 * @typedef PgApiMutators
 * @type {object}
 * @property {Function} userCreate
 * @property {Function} userLogin
 * 
 * @typedef PgApi
 * @type {object}
 * @property {Function} taskMainList
 * @property {Function} tasksInfo
 * @property {Function} userInfo
 * @property {Function} usersInfo
 * @property {Function} approachList
 * @property {Function} approachLists
 * @property {Function} searchResults {@link searchResults}
 * @property {PgApiMutators} mutators
 * 
 * @returns {PgApi}
 */
export const PgApi = async () => {
  const dbClient = await pgClient()

  return {
    taskMainList: async () => await taskMainList(dbClient),
    tasksInfo: async (taskIds) => await tasksInfo(dbClient, taskIds),

    userInfo: async (userId) => await userInfo(dbClient, userId),
    usersInfo: async (userIds) => await usersInfo(dbClient, userIds),

    approachList: async (taskId) => await approachList(dbClient, taskId),
    approachLists: async (taskIds) => await approachLists(dbClient, taskIds),

    searchResults: async (searchItems) => await searchResults(dbClient, searchItems),

    userFromAuthToken: async (authToken) => await userFromAuthToken(dbClient, authToken),

    mutators: {
      userCreate: async (userInput) => await userCreate(dbClient, userInput),
      userLogin: async (authInput) => await userLogin(dbClient, authInput)
    }
  }
}

/**
 * @param {PgClient} dbClient 
 * @returns 
 */
async function taskMainList(dbClient) {
  // const sql = `SELECT t.id, t.content, t.tags, t.user_id AS "userId", t.approach_count AS "approachCount", t.is_private AS "isPrivate", t.created_at AS "createdAt"
  //   FROM azdev.tasks t
  //   INNER JOIN azdev.users u ON t.user_id = u.id
  //   WHERE t.is_private = FALSE
  //   ORDER BY t.created_at DESC
  //   LIMIT 100`

  // tasks 1->1 users
  // const sql = `SELECT t.id, 
  //   t.content, t.tags, 
  //   t.user_id AS "userId", 
  //   t.approach_count AS "approachCount", 
  //   t.is_private AS "isPrivate", 
  //   t.created_at AS "createdAt",
  //   u.id AS "author_id",
  //   u.username AS "author_username",
  //   u.first_name AS "author_firstName",
  //   u.last_name AS "author_lastName",
  //   u.created_at AS "author_createdAt"
  //   FROM azdev.tasks t
  //   JOIN azdev.users u ON (t.user_id = u.id)`

  // dataloader
  const sql = `SELECT t.id, t.content, t.tags, t.user_id AS "userId", t.approach_count AS "approachCount", t.is_private AS "isPrivate", t.created_at AS "createdAt"
    FROM azdev.tasks t
    INNER JOIN azdev.users u ON t.user_id = u.id
    WHERE t.is_private = FALSE
    ORDER BY t.created_at DESC
    LIMIT 100`

  if (isDev) {
    console.log(sql)
  }
  const pgResp = await dbClient.pgPool.query(sql);
  if (isDev) {
    console.log(pgResp.rows)
  }
  return pgResp.rows;
}

async function tasksInfo(dbClient, taskIds) {
  const sql = `SELECT t.id, t.content, t.tags, t.user_id AS "userId", t.approach_count AS "approachCount", t.is_private AS "isPrivate", t.created_at AS "createdAt"
  FROM azdev.tasks t
  WHERE t.is_private = FALSE
  AND t.id = ANY ($1::int[])`

  if (isDev) {
    console.log(sql, ':params', taskIds)
  }
  const pgResp = await dbClient.pgPool.query(sql, [taskIds]);
  const rows = pgResp.rows;
  return taskIds.map((taskId) => rows.find((row) => row.id == taskId))
}

/**
 * @param {PgClient} dbClient
 * @param {Number} userId 
 */
async function userInfo(dbClient, userId) {
  return usersInfo(dbClient, [userId])[0]
}

async function usersInfo(dbClient, userIds) {
  const sql = `SELECT id, username,
    first_name AS "firstName", last_name AS "lastName",
    created_at AS "createdAt"
    FROM azdev.users
    WHERE id = ANY ($1::int[])`
  if (isDev) {
    console.log(sql, ':params', userIds)
  }
  const pgResp = await dbClient.pgPool.query(sql, [userIds]);
  return pgResp.rows;
}

/**
 * 
 * @param {PgClient} dbClient 
 * @param {Number} taskId 
 */
async function approachList(dbClient, taskId) {
  return _approachLists(dbClient, [taskId])
}

async function approachLists(dbClient, taskIds) {
  const list = await _approachLists(dbClient, taskIds)
  return taskIds.map((taskId) => list.filter((row) => row.id === taskId))
}

async function _approachLists(dbClient, taskIds) {
  // tasks 1->m approaches
  const sql = `SELECT id, content, user_id AS "userId", task_id AS "taskId",
    vote_count AS "voteCount", created_at AS "createdAt"
    FROM azdev.approaches
    WHERE task_id = ANY ($1::int[])
    ORDER BY vote_count DESC, created_at DESC`
  if (isDev) {
    console.log(sql, ':params', taskIds)
  }
  const pgResp = await dbClient.pgPool.query(sql, [taskIds]);
  return pgResp.rows;
}

async function searchResults(dbClient, searchItems) {
  const sql = `WITH
    VIEWABLE_TASKS AS (
      SELECT
        *
      FROM
        AZDEV.TASKS N
      WHERE
        (
          IS_PRIVATE = FALSE
          OR USER_ID = null
        )
    )
  SELECT
    ID,
    "taskId",
    CONTENT,
    TAGS,
    "approachCount",
    "voteCount",
    "userId",
    "createdAt",
    TYPE,
    TS_RANK(TO_TSVECTOR(CONTENT), WEBSEARCH_TO_TSQUERY($1)) AS RANK
  FROM
    (
      SELECT
        ID,
        ID AS "taskId",
        CONTENT,
        TAGS,
        APPROACH_COUNT AS "approachCount",
        NULL AS "voteCount",
        USER_ID AS "userId",
        CREATED_AT AS "createdAt",
        'task' AS TYPE
      FROM
        VIEWABLE_TASKS
      UNION ALL
      SELECT
        A.ID,
        T.ID AS "taskId",
        A.CONTENT,
        NULL AS TAGS,
        NULL AS "approachCount",
        A.VOTE_COUNT AS "voteCount",
        A.USER_ID AS "userId",
        A.CREATED_AT AS "createdAt",
        'approach' AS TYPE
      FROM
        AZDEV.APPROACHES A
        JOIN VIEWABLE_TASKS T ON (T.ID = A.TASK_ID)
    ) SEARCH_VIEW
  WHERE
    TO_TSVECTOR(CONTENT) @@ WEBSEARCH_TO_TSQUERY($1)
  ORDER BY
    RANK DESC,
    TYPE DESC`
  if (isDev) {
    console.log(sql, ':params', searchItems)
  }
  const result = searchItems.map(async (searchItem) => {
    const pgResp = await dbClient.pgPool.query(sql, [searchItem]);
    return pgResp.rows;
  })
  return result
}


async function userCreate(dbClient, userInput) {
  const payload = { errors: [] }
  if (userInput.password.length < 6) {
    payload.errors.push({
      message: 'Use a stronger password'
    })
  }

  if (payload.errors.length == 0) {
    const authToken = randomString()
    const sql = `INSERT INTO azdev.users(username, hashed_password, first_name, last_name, hashed_auth_token)
    VALUES ($1, crypt($2, gen_salt('bf')), $3, $4, crypt($5, gen_salt('bf')))
    RETURNING id, username, hashed_password, first_name AS firstName, last_name AS lastName, hashed_auth_token`

    const { username, password, firstName, lastName } = userInput
    const pgResp = await dbClient.pgPool.query(sql,
      [username, password, firstName, lastName, authToken]);
    if (pgResp.rows[0]) {
      payload.user = pgResp.rows[0]
      payload.authToken = authToken
    }
  }
  return payload
}

async function userLogin(dbClient, authInput) {
  const payload = { errors: [] }
  if (!authInput.username || !authInput.password) {
    payload.errors.push({
      message: 'Invalid username or password'
    })
  }
  if (payload.errors.length !== 0) {
    return payload
  }

  const { username, password } = authInput
  const sql = `SELECT id, username, first_name AS "firstName", last_name AS "lastName"
  FROM azdev.users
  WHERE username = $1
  AND hashed_password = crypt($2, hashed_password)`
  const pgResp = await dbClient.pgPool.query(sql, [username, password]);
  const user = pgResp.rows[0]
  if (user) {
    const authToken = randomString()
    const updateTokenSql = `UPDATE azdev.users
    SET hashed_auth_token = crypt($2, gen_salt('bf'))
    WHERE id = $1`
    await dbClient.pgPool.query(updateTokenSql, [user.id, authToken])
    payload.user = user
    payload.authToken = authToken
  } else {
    payload.errors.push({
      message: 'Invalid username or password'
    })
  }

  return payload
}

async function userFromAuthToken(dbClient, authToken) {
  if (!authToken) {
    return null
  }

  const sql = `SELECT id, username, first_name AS "firstName", last_name AS "lastName"
  FROM azdev.users
  WHERE hashed_auth_token = crypt($1, hashed_auth_token)`
  const pgResp = await dbClient.pgPool.query(sql, [authToken]);
  return pgResp.rows[0]
}