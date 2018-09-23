package util;

import java.util.Map;
import java.util.Stack;
import java.util.Set;

import operator.Tuple;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * this class handles the created join trees.
 * @author xl664 Xinhe Li
 *
 */
public class JoinExpressionVisitor implements ExpressionVisitor {
	private Stack<Expression> expressionStack;

	private Map<String, Integer> schemaMap;
	
	/**
	 * constructor, assigns the field to the arguments.
	 * @param map 
	 * @param array
	 * @param map2
	 * @param hash
	 */
	public JoinExpressionVisitor(Map<String, Integer> schemaMap) {
		expressionStack = new Stack<>();
		this.schemaMap = schemaMap;
	}
	
	/** 
	 * check whether this tuple is a valid one.
	 * @return the state "this tuple is valid."
	 */
	public Expression getExpression(){
		if(expressionStack.size()==1){
			return expressionStack.peek();
		}
		else{
			return null;
		}
	}

	/**
	 * method visit the parenthesis node.
	 * get the next tuple of this node and check whether it is valid.
	 * if not, fetch the next tuple and check until there is a valid one.
	 * if we meet the end of the tuple, go to the table adjacent to this
	 * table and fetch the next valid tuple. Reset this table and continue 
	 * to search for the valid one.  
	 * @param the parenthesis node to be visited.
	 */
	@Override
	public void visit(Parenthesis node) {
	}

	/**
	 * method visit the long value.
	 * push a true value on the second stack since this node
	 * indicates a cross product.
	 */
	@Override
	public void visit(LongValue node) {
		// TODO Auto-generated method stub
		expressionStack.push(node);
	}

	/**
	 * visit method for the column node.
	 * get the respective column of the alias and push the result on the stack1.
	 * if we cannot find the tuple, this indicates there are no tuples in 
	 * this table. So we set stillLeft to false and push an artificial long value
	 * on the stack to avoid EmptyStackException. 
	 * @param the expression node.
	 */
	@Override
	public void visit(Column node) {
		// TODO Auto-generated method stub
		String field = node.getWholeColumnName();
		String fieldSplit[] = field.split("\\.");
		if(fieldSplit.length == 2){
			// Sailers.A
			String tableAlias = fieldSplit[0], fieldName = fieldSplit[1];
			String schemaKey = tableAlias+'.'+fieldName;
			if(schemaMap.containsKey(schemaKey)){
				expressionStack.push(node);
			}
			else{
				expressionStack.push(null);
			}

		}
		else if(fieldSplit.length == 1){
			// A
			// need to find which table the field belongs to
		}
	}

	/**
	 * method that visit the and expression node.
	 * if isValid is true, only traverse the right part.
	 * else, make a post traverse of the tree.
	 */
	@Override
	public void visit(AndExpression node) {
		// TODO Auto-generated method stub
		node.getLeftExpression().accept(this);
		node.getRightExpression().accept(this);

		Expression expLeft = expressionStack.pop(), expRight = expressionStack.pop();
		if(expLeft != null && expRight != null){
			Expression temp = new AndExpression(expLeft, expRight);
			expressionStack.push(temp);
		}
		else if(expLeft != null){
			expressionStack.push(expLeft);
		}
		else if(expRight != null){
			expressionStack.push(expRight);
		}
		else{
			expressionStack.push(null);
		}

	}

	@Override
	public void visit(OrExpression node) {
		// TODO Auto-generated method stub
		node.getLeftExpression().accept(this);
		node.getRightExpression().accept(this);

		Expression expLeft = expressionStack.pop(), expRight = expressionStack.pop();
		if(expLeft != null && expRight != null){
			Expression temp = new AndExpression(expLeft, expRight);
			expressionStack.push(temp);
		}
		else{
			expressionStack.push(null);
		}
		
	}

	/**
	 * visit method for the equals to node.
	 * post traverse the node, pop two long values out of the stack1.
	 * check whether the two values are the same and push the result
	 * on the stack2.
	 * @param an equals to expression node.
	 */
	@Override
	public void visit(EqualsTo node) {
		// TODO Auto-generated method stub
		node.getLeftExpression().accept(this);
		node.getRightExpression().accept(this);

		Expression expLeft = expressionStack.pop(), expRight = expressionStack.pop();
		if(expLeft!=null && expRight!=null){
			expressionStack.push(node);
		}
		else{
			expressionStack.push(null);
		}
	}

	/**
	 * visit method for the greater than node.
	 * post traverse the node, pop two long values out of the stack1.
	 * check whether the second one popped is greater than the first
	 * one and push the result on the stack2.
	 * @param an greater expression node.
	 */
	@Override
	public void visit(GreaterThan node) {
		// TODO Auto-generated method stub
		node.getLeftExpression().accept(this);
		node.getRightExpression().accept(this);

		Expression expLeft = expressionStack.pop(), expRight = expressionStack.pop();
		if(expLeft!=null && expRight!=null){
			expressionStack.push(node);
		}
		else{
			expressionStack.push(null);
		}
	}

	/**
	 * visit method for the greater than equals node.
	 * post traverse the node, pop two long values out of the stack1.
	 * check whether the second one popped is greater than or equals 
	 * to the first one and push the result on the stack2.
	 * @param an greater than equals expression node.
	 */
	@Override
	public void visit(GreaterThanEquals node) {
		// TODO Auto-generated method stub
		node.getLeftExpression().accept(this);
		node.getRightExpression().accept(this);

		Expression expLeft = expressionStack.pop(), expRight = expressionStack.pop();
		if(expLeft!=null && expRight!=null){
			expressionStack.push(node);
		}
		else{
			expressionStack.push(null);
		}
	}

	/**
	 * visit method for the minor than node.
	 * post traverse the node, pop two long values out of the stack1.
	 * check whether the second one popped is minor than 
	 * to the first one and push the result on the stack2.
	 * @param a minor than expression node.
	 */
	@Override
	public void visit(MinorThan node) {
		// TODO Auto-generated method stub
		node.getLeftExpression().accept(this);
		node.getRightExpression().accept(this);

		Expression expLeft = expressionStack.pop(), expRight = expressionStack.pop();
		if(expLeft!=null && expRight!=null){
			expressionStack.push(node);
		}
		else{
			expressionStack.push(null);
		}
	}

	/**
	 * visit method for the minor than equals node.
	 * post traverse the node, pop two long values out of the stack1.
	 * check whether the second one popped is minor than equals
	 * to the first one and push the result on the stack2.
	 * @param a minor than equals expression node.
	 */
	@Override
	public void visit(MinorThanEquals node) {
		// TODO Auto-generated method stub
		node.getLeftExpression().accept(this);
		node.getRightExpression().accept(this);

		Expression expLeft = expressionStack.pop(), expRight = expressionStack.pop();
		if(expLeft!=null && expRight!=null){
			expressionStack.push(node);
		}
		else{
			expressionStack.push(null);
		}
	}

	/**
	 * visit method for the equals to node.
	 * post traverse the node, pop two long values out of the stack1.
	 * check whether the two values are not the same and push the result
	 * on the stack2.
	 * @param an not equals to expression node.
	 */
	@Override
	public void visit(NotEqualsTo node) {
		// TODO Auto-generated method stub
		node.getLeftExpression().accept(this);
		node.getRightExpression().accept(this);

		Expression expLeft = expressionStack.pop(), expRight = expressionStack.pop();
		if(expLeft!=null && expRight!=null){
			expressionStack.push(node);
		}
		else{
			expressionStack.push(null);
		}
	}
	
	@Override
	public void visit(NullValue node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Function node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InverseExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(JdbcParameter node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DoubleValue node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DateValue node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TimeValue node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TimestampValue node) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void visit(StringValue node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Addition node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Division node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Multiplication node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Subtraction node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Between node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IsNullExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LikeExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SubSelect node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CaseExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WhenClause node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExistsExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AllComparisonExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AnyComparisonExpression node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Concat node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Matches node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseAnd node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseOr node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseXor node) {
		// TODO Auto-generated method stub
		
	}

}