import crypto from 'crypto';

/**
 * @typedef PrefixedObject
 * @type {object}
 * @property {object} prefixedObject
 * @property {string} prefix
 */

/**
 * Extract .fields with prefix from source object.
 * @param {PrefixedObject} param 
 * @returns {object}
 */
export const extractPrefixedColumns = ({
  prefixedObject,
  prefix,
}) => {
  const prefixRexp = new RegExp(`^${prefix}_(.*)`);
  return Object.entries(prefixedObject).reduce(
    (acc, [key, value]) => {
      const match = key.match(prefixRexp);
      if (match) {
        acc[match[1]] = value;
      }
      return acc;
    },
    {},
  );
};


export const randomString = (bytesSize = 32) =>
  crypto.randomBytes(bytesSize).toString('hex');
