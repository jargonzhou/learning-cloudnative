package com.spike.giantdataanalysis.rdfstore.sparql.support;

import java.util.Date;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import com.spike.giantdataanalysis.db.commons.data.MoreBytes;
import com.spike.giantdataanalysis.rdfstore.sparql.core.XSDDataTypeEnum;

public final class IdAssigner {
  public static byte[] fromNode(String node) {
    return Hashing.md5().hashString(node, Charsets.UTF_8).asBytes();
  }

  public static byte[] fromLiteral(XSDDataTypeEnum xsdDataType, Object literalValue) {

    switch (xsdDataType) {
    case DECIMAL:
      Preconditions.checkArgument(literalValue instanceof Double,
        "invalid XSD data type: DECIMAL!");
      return MoreBytes.toBytes((Double) literalValue);
    case INTEGER:
      Preconditions.checkArgument(literalValue instanceof Integer,
        "invalid XSD data type: INTEGER!");
      return MoreBytes.toBytes((Integer) literalValue);
    case DATETIME:
      Preconditions.checkArgument(literalValue instanceof Date, "invalid XSD data type!: DATETIME");
      return MoreBytes.toBytes(((Date) literalValue).getTime());
    case DATE:
      Preconditions.checkArgument(literalValue instanceof Date, "invalid XSD data type!: DATE");
      return MoreBytes.toBytes(((Date) literalValue).getTime());
    case BOOLEAN:
      Preconditions.checkArgument(literalValue instanceof Boolean,
        "invalid XSD data type!: BOOLEAN");
      return MoreBytes.toBytes((Boolean) literalValue);
    default:
      Preconditions.checkArgument(false, "invalid XSD data type!");
      return null;
    }

  }
}
