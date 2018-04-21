package org.jena.rules.tutorial;

import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalcFinalMark extends BaseBuiltin {
  private static Logger logger = LoggerFactory.getLogger(CalcFinalMark.class);
  
  @Override
  public String getName() {
    return "calcFinalMark";
  }

  @Override
  public int getArgLength() {
      return 4;
  }

  @Override
  public void headAction(Node[] args, int length, RuleContext context) {
    doUserRequiredAction(args, length, context);
  }  
  
  @Override
  public boolean bodyCall(Node[] args, int length, RuleContext context) {
    return doUserRequiredAction(args, length, context);
  }
  
  private boolean doUserRequiredAction(Node[] args, int length, RuleContext context) {
    logger.trace("#### doUserRequiredAction");
    
    // Check we received the correct number of parameters
    checkArgs(length, context);

    boolean success = false;
    
    // Retrieve the input arguments
    Node studentTestResult = getArg(0, args, context);
    Node studentExamResult = getArg(1, args, context);
    Node studentProjectResult = getArg(2, args, context);
 
    // Verify the typing of the parameters
    if (studentTestResult.isLiteral() && studentExamResult.isLiteral() && 
        studentProjectResult.isLiteral()) {
      Node finalMark = null;
      if (studentTestResult.getLiteralValue() instanceof Number && 
          studentExamResult.getLiteralValue() instanceof Number &&
          studentProjectResult.getIndexingValue() instanceof Number) {
        
        Number nvStudentTestResult = (Number)studentTestResult.getLiteralValue();
        Number nvStudentExamResult = (Number)studentExamResult.getLiteralValue();
        Number nvStudentProjectResult = (Number)studentProjectResult.getLiteralValue();
        
        // Doing the calculation
        int nFinalMark = (nvStudentTestResult.intValue() * 20)/100 + 
            (nvStudentExamResult.intValue() * 50)/100 +
            (nvStudentProjectResult.intValue() * 30)/100;
           
        // Creating a node for the output parameter
        finalMark = Util.makeIntNode(nFinalMark);
        
        // Binding the output parameter to the node
        BindingEnvironment env = context.getEnv();
        success = env.bind(args[3], finalMark);
      } 
    }   
    return success;
  }
}
