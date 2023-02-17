import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

interface IExpressionEvaluator {
      
    /**
    * Takes a symbolic/numeric infix expression as input and converts it to
    * postfix notation. There is no assumption on spaces between terms or the
    * length of the term (e.g., two digits symbolic or numeric term)
    *
    * @param expression infix expression
    * @return postfix expression
    */
      
    public String infixToPostfix(String expression);
      
      
    /**
    * Evaluate a postfix numeric expression, with a single space separator
    * @param expression postfix expression
    * @return the expression evaluated value
    */
      
    public int evaluate(String expression);
    
    
    /**
    * takes the operator as input and return number indicating
    * the precedence of the operator 
    * @param op the operator
    * @return the precedence of the operator
    */

    public int precedence(char op);
    
    
    /**
    * takes a character as input and check if it is a valid operand
    * @param c the character from expression
    * @return true if operand and false otherwise
    */
    
    public boolean isOperand (char c);
    
    
    /**
    * takes a character as input and check if it is a valid operator
    * @param c the character from expression
    * @return true if valid operator and false otherwise
    */
    
    public boolean isOperator (char c);
    
    
    /**
    * takes the operand as input and return the value of it 
    * @param c the operand
    * @return the value of the operand
    */
    
    public int valueOf(char c);
    
    
    /**
    * takes two operands and an operator as input and
    * return the value of this operation
    * @param operand1 the first operand
    * @param operand2 the second operand
    * @param op the operator
    * @return the result of the operation
    */
    public int calc (Object operand1, Object operand2, char op);

}

/**
 * a class contains methods to transform an infix expression
 * into a postfix expression and evaluates its numeric value
 * @author Mohamed H.Sadek
 */
public class Evaluator implements IExpressionEvaluator {
    
    
    /**
     * indicates if the expression is valid or not
     */
    
    public static boolean flag = true;
    
    
    /**
     * value of a
     */
    
    public static int A;
    
    
    /**
     * value of b
     */
    
    public static int B;
    
    
    /**
     * value of c
     */
    
    public static int C;
    
    public int precedence(char op) {
        switch (op) {
        case '^':
            return 3;
        case '*':
            return 2;
        case '/':
            return 2;
        case '+':
            return 1;
        case '-':
            return 1;
        default:
            return 0;
        }
    }
    
    public boolean isOperand (char c) {
        if(c == 'a' || c == 'b' || c == 'c')
            return true;
        else
            return false;
    }
    
    public boolean isOperator (char c){
        if(c == '^' || c == '*' || c == '/' || c == '+' || c == '-')
            return true;
        else
            return false;
    }
    
    public String infixToPostfix(String expression) {
        MyStack stack = new MyStack();
        String postfix = "";
        if(!isOperand(expression.charAt(expression.length() - 1))){
            if(isOperator(expression.charAt(expression.length() - 1))) {
                flag = false;
                return postfix;
            }
            else {
                if(isOperator(expression.charAt(expression.length() - 2))) {
                    flag = false;
                    return postfix;
                }
            }
        }
        int counter = 0;
        while(counter < 3 && expression.charAt(counter) == '-'){
            counter++;
        }
        if(counter >= 3){
            flag = false;
            return postfix;
        }
        if(expression.charAt(0) == '-' && expression.charAt(1) == '-') {
            expression = expression.substring(2);
        }
        for(int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if(expression.charAt(i) == '-' && expression.charAt(i + 1) == '-') {
                if(isOperator(expression.charAt(i - 1))){
                    i++;
                    continue;
                }
                i++;
                c = '+';
            }
            if(!isOperand(c) && c != '(' && c!= ')' && !isOperator(c)) {
                flag = false;
                break;
            }
            if(isOperand(c)) {
                postfix += c;
            }
            else if(c == '(') {
                stack.push(c);
            }
            else if(c == ')') {
                while(!stack.isEmpty() && (char)stack.peek() != '(') {
                    postfix += stack.pop();
                }
                if(stack.isEmpty()) {
                    flag = false;
                    break;
                }
                stack.pop();
            }
            else {
                while(!stack.isEmpty() && precedence(c) <= precedence((char)stack.peek())){
                    if (c == '^' && (char)stack.peek() == '^')
                        break;
                    else
                        postfix += stack.pop();
                }
                stack.push(c);
            }
        }
        while(!stack.isEmpty()) {
            postfix += stack.pop();
        }
        return postfix;
    }
    
    public int valueOf(char c) {
        switch (c) {
        case 'a':
            return A;
        case 'b':
            return B;
        case 'c':
            return C;
        default:
            return 0;
        }
    }
    
    public int calc (Object operand1, Object operand2, char op) {
        int res = 0;
        switch (op) {
        case '^':
            res = (int)Math.pow((int)operand1, (int)operand2);
            break;
        case '*':
            res = (int)operand1 * (int)operand2;
            break;
        case '/':
            res = (int)operand1 / (int)operand2;
            break;
        case '+':
            res = (int)operand1 + (int)operand2;
            break;
        case '-':
            res = (int)operand1 - (int)operand2;
            break;
        }
        return res;
    }
    
    public int evaluate(String expression) {
        MyStack stack = new MyStack();
        int res = 0;
        for(int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if(isOperand(c)) {
                stack.push(valueOf(c));
            }
            else {
                Object operand2 = stack.pop();
                Object operand1 = stack.pop();
                if(operand2 == null) {
                    flag = false;
                    break;
                }
                else if(operand1 == null && c == '-') {
                    operand1 = 0;
                }
                else if (operand1 == null && c != '-') {
                    flag = false;
                    break;
                }
                stack.push(calc(operand1, operand2, c));
            }
        }
        if(stack.isEmpty()) {
            flag = false;
        }
        else {
            res = (int)stack.pop();
            if(!stack.isEmpty()) {
                flag = false;
            }
        }
        return res;
    }
    
    
    /**
     * main method
     * @param args
     * take defualt parameter of main method: array of strings
     */
    public static void main(String[] args) {
        Evaluator eval = new Evaluator();
        Scanner sc = new Scanner(System.in);
        String expression = sc.nextLine();
        String s1 = sc.nextLine();
        s1 = s1.substring(2);
        String s2 = sc.nextLine();
        s2 = s2.substring(2);
        String s3 = sc.nextLine();
        s3 = s3.substring(2);
        A = Integer.parseInt(s1);
        B = Integer.parseInt(s2);
        C = Integer.parseInt(s3);
        String postfix = eval.infixToPostfix(expression);
        int value = eval.evaluate(postfix);
        if(flag) {
            System.out.println(postfix);
            System.out.println(value);
        }
        else {
            System.out.println("Error");
        }
    }
}

/**
 * interface of the stack class
 * @author Mohamed H.Sadek
 */
interface IStack {
      
      /*** Removes the element at the top of stack and returns that element.
      * @return top of stack element, or through exception if empty
      */
      
      public Object pop();
      
      /*** Get the element at the top of stack without removing it from stack.
      * @return top of stack element, or through exception if empty
      */
      
      public Object peek();
      
      /*** Pushes an item onto the top of this stack.
      * @param object to insert*
      */
      
      public void push(Object element);
      
      /*** Tests if this stack is empty
      * @return true if stack empty
      */
      public boolean isEmpty();
      
      /**
       * @return the size of the stack
       */
      public int size();
      
      /**
       * print the stack in the form [1, 2, 3, .....]
       */
      public void printStack();
}

/**
* the class which implement the stack
* @author Mohamed H.Sadek
*/
class MyStack implements IStack {
    
    SingleLinkedList stack = new SingleLinkedList();
    
    public Object pop(){
        if(stack.isEmpty())
            return null;
        Object temp = stack.get(0);
        stack.remove(0);
        return temp;
    }
  
    public Object peek(){
        if(stack.isEmpty())
            return null;
        return stack.get(0);
    }
  
    public void push(Object element){
        stack.insertFirst(element);
    }
  
    public boolean isEmpty(){
        return stack.isEmpty();
    }
  
    public int size(){
        return stack.size();
    }
    
    public void printStack(){
        stack.printList();
    }
}

interface ILinkedList {

/**
* @param index
* @return the element at the specified position in this list.
*/
public Object get(int index);

/**
* @return true if this list contains no elements.
*/
public boolean isEmpty();

/**
* Removes the element at the specified position in this list.
* @param index
*/
public void remove(int index);

/**
* @return the number of elements in this list.
*/
public int size();

/**
 * print the list in the form [1, 2, 3, .....]
 */
public void printList();
}

/**
* class which contains single list implementation
* @author Mohamed H.Sadek
*/
class SingleLinkedList implements ILinkedList {
    public int length = 0;
    public static boolean flag;

    public class Node {
        Object item;
        Node next;
    }

    public Node head = null;
    public Node tail = null;

    public void insertFirst(Object element) {
        Node newNode = new Node();
        newNode.item = element;
        if (length == 0) {
            newNode.next = null;
            head = tail = newNode;
        } 
        else {
            newNode.next = head;
            head = newNode;
        }
        length++;
    }

    public Object get(int index) {
        if(index < 0 || index >= length) {
            return 0;
        }
        else if(index == 0) {
            return head.item;
        }
        else if(index == length - 1) {
            return tail.item;
        }
        else {
            Node current = head;
            for(int i = 0; i < index; i++) {
                current = current.next;
            }
            return current.item;
        }
    }

    public void remove(int index) {
        flag = true;
        Node current = head;
        if(index < 0 || index >= length) {
            flag = false;
        }
        else if(length == 1) {
            head = tail = null;
            length--;
        }
        else if (index == 0) {
            head = head.next;
            length--;
        }
        else if (index == length - 1) {
            for(int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = null;
            tail = current;
            length--;
        }
        else {
            for(int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
            length--;
        }
    }

    public boolean isEmpty() {
        if (length == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public int size() {
        return length;
    }
    
     public void printList() {
        Node current = head;
        if(current == null) {
            System.out.println("[]");
        }
        else {
            System.out.print("[" + current.item);
            current = current.next;
            while (current != null) {
                System.out.print(", " + current.item);
                current = current.next;
            }
            System.out.println("]");
        }
    }
}