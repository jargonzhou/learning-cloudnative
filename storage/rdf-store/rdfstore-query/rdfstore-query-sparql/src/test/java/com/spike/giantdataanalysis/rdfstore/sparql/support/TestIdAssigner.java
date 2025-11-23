package com.spike.giantdataanalysis.rdfstore.sparql.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Strings;
import com.spike.giantdataanalysis.rdfstore.sparql.core.XSDDataTypeEnum;

public class TestIdAssigner {

  @Test
  public void testFromNode() {
    String node = "hello";
    Assert.assertEquals(16, IdAssigner.fromNode(node).length);
    node = "hello2";
    Assert.assertEquals(16, IdAssigner.fromNode(node).length);
    node = Strings.repeat("hello2", 10);
    Assert.assertEquals(16, IdAssigner.fromNode(node).length);
  }

  @Test
  public void testFromLiteral() throws ParseException {
    Assert.assertEquals(8,
      IdAssigner.fromLiteral(XSDDataTypeEnum.DECIMAL, Double.valueOf(Double.MIN_VALUE)).length);
    Assert.assertEquals(8,
      IdAssigner.fromLiteral(XSDDataTypeEnum.DECIMAL, Double.valueOf(Double.MAX_VALUE)).length);
    Assert.assertEquals(4,
      IdAssigner.fromLiteral(XSDDataTypeEnum.INTEGER, Integer.MIN_VALUE).length);
    Assert.assertEquals(4,
      IdAssigner.fromLiteral(XSDDataTypeEnum.INTEGER, Integer.MAX_VALUE).length);
    Assert.assertEquals(8,
      IdAssigner.fromLiteral(XSDDataTypeEnum.DATETIME, new Date(Long.MIN_VALUE)).length);
    Assert.assertEquals(8,
      IdAssigner.fromLiteral(XSDDataTypeEnum.DATETIME, new Date(Long.MAX_VALUE)).length);
    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
    Assert.assertEquals(8,
      IdAssigner.fromLiteral(XSDDataTypeEnum.DATE, sdf.parse("1970-01-01")).length);
    Assert.assertEquals(8,
      IdAssigner.fromLiteral(XSDDataTypeEnum.DATE, sdf.parse("2018-09-16")).length);
    Assert.assertEquals(1, IdAssigner.fromLiteral(XSDDataTypeEnum.BOOLEAN, true).length);
    Assert.assertEquals(1, IdAssigner.fromLiteral(XSDDataTypeEnum.BOOLEAN, false).length);
  }
}
