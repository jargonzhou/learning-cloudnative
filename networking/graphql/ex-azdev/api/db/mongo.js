import mdb from 'mongodb';

import { isDev, mdbConnectionString } from '../config.js';

/**
 * @typedef MongoClient
 * @type {object}
 * @property {mdb.MongoClient} mdb
 * @property {Function} mdbClose
 * 
 * @returns 
 */
async function mongoClient() {
  const client = new mdb.MongoClient(mdbConnectionString, {});
  try {
    await client.connect();
    const mdb = client.db();

    // Test the connection
    const collections = await mdb.collections();
    console.log(
      'Connected to MongoDB | Collections count:',
      collections.length
    );

    return {
      mdb,
      mdbClose: () => client.close(),
    };
  } catch (err) {
    console.error('Error in MongoDB Client', err);
    process.exit(1);
  }
}

/**
 * @typedef MongoApi
 * @type {Function}
 * @property {Function} detailLists
 * 
 * @returns {MongoApi}
 */
export const MongoApi = async () => {
  const { mdb, mdbClose } = await mongoClient()

  return {
    detailLists: async (approachIds) => await detailLists(mdb, approachIds),
    mutators: {
      
    }
  }
}

async function mdbFindDocumentsByField(mdb, {
  collectionName,
  fieldName,
  fieldValues,
}) {
  if (isDev) {
    console.log('Mongo Query', collectionName, fieldName, fieldValues)
  }

  return mdb
    .collection(collectionName)
    .find({ [fieldName]: { $in: fieldValues } })
    .toArray()
}

async function detailLists(mdb, approachIds) {
  const mongoDocuments = await mdbFindDocumentsByField(mdb, {
    collectionName: 'approachDetails',
    fieldName: 'pgId',
    fieldValues: approachIds,
  });
  if (isDev) {
    console.log('Mongo Result', mongoDocuments)
  }

  return approachIds.map((approachId) => {
    const approachDoc = mongoDocuments.find(
      (doc) => approachId === doc.pgId
    );

    if (!approachDoc) {
      return [];
    }

    const { explanations, notes, warnings } = approachDoc;
    const approachDetails = [];
    if (explanations) {
      approachDetails.push(
        ...explanations.map((explanationText) => ({
          content: explanationText,
          category: 'EXPLANATION',
        }))
      );
    }
    if (notes) {
      approachDetails.push(
        ...notes.map((noteText) => ({
          content: noteText,
          category: 'NOTE',
        }))
      );
    }
    if (warnings) {
      approachDetails.push(
        ...warnings.map((warningText) => ({
          content: warningText,
          category: 'WARNING',
        }))
      );
    }
    return approachDetails;
  });
} 