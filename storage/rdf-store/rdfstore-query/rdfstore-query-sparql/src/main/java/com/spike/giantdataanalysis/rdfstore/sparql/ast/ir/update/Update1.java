package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.update;

// [30] Update1 ::= Load | Clear | Drop | Add | Move | Copy | Create | InsertData | DeleteData
// | DeleteWhere | Modify
public class Update1 {
  public enum UpdateTypeEnum {
    Load, Clear, Drop, Add, Move, Copy, Create, InsertData, DeleteData, DeleteWhere, Modify
  }

  public UpdateTypeEnum type;

  public Load load;
  public Clear clear;
  public Drop drop;
  public Add add;
  public Move move;
  public Copy copy;
  public Create create;
  public InsertData insertData;
  public DeleteData deleteData;
  public DeleteWhere deleteWhere;
  public Modify modify;
}