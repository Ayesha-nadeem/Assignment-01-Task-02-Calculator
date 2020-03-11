package pk.edu.pucit.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView equation;
    private TextView result;
    private char add1 = '+';
    private char sub1 = '-';
    private char mul1 = '*';
    private char div1 = '/';
    private char equalbtn = 0;
    private double num1 = Double.NaN;
    private double num2;
    private String action;
    private String s="";
    private String equ="";
    private double temp= Double.NaN;
    boolean flag=false;
    boolean flagDecimal=false;
    private double finalAnswer=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        equation=(TextView)findViewById(R.id.tv_equation);
        result=(TextView)findViewById(R.id.tv_result);


    }
    public void getequation(View v)
    {
        TextView tv = (TextView) v;
        s =s+tv.getText().toString();
        String postfixExp=infixToPostfix(s);
            double evaluatedExp = evaluatePostfix(postfixExp);
            if(!Double.isInfinite(evaluatedExp)) {
                finalAnswer = evaluatedExp;

                result.setText(evaluatedExp + "");
            }
            else {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
            }
        equation.setText(s);
    }
    public void action(View v)
    {
        flagDecimal=false;
        TextView tv = (TextView) v;
        if(s.length()!=0) {
            if (s.charAt(s.length() - 1) != '+' && s.charAt(s.length() - 1) != '-' && s.charAt(s.length() - 1) != '/' && s.charAt(s.length() - 1) != 'x'&& s.charAt(s.length() - 1) != '%') {
                s = s + tv.getText().toString();
            } else {
                s = s.substring(0, s.length() - 1);
                s = s + tv.getText().toString();

            }
            equation.setText(s);
            result.setText("");
        }
        else
        {
            equation.setText("");
            result.setText("");
        }

    }
    private double evaluatePostfix(String exp)
    {
        //create a stack
        Stack<Double> stack = new Stack<>();

        // Scan all characters one by one
        for(int i = 0; i < exp.length(); i++)
        {
            char c = exp.charAt(i);
            flag=false;
            if(c == ' ') {
                flag=false;
                continue;
            }

                // If the scanned character is an operand
                // (number here),extract the number
                // Push it to the stack.
            else if(Character.isDigit(c) || c=='.')
            {
                double n = 0;
                double num=0;

                String s="0.";

                //extract the characters and store it in num
                while((Character.isDigit(c) || c=='.') && i<exp.length())
                {
                    if(c=='.')
                    {
                        flag=true;
                    }
                    if(flag==false && Character.isDigit(c)) {
                        n = n * 10 + (double) (c - '0');
                    }
                    else if(flag==true && Character.isDigit(c))
                    {
                         s=s+c;
                        num=Double.parseDouble(s);
                    }
                    i++;
                    if(i<exp.length())
                    c = exp.charAt(i);

                }
                n=n+num;
                i--;

                //push the number in stack
                stack.push(n);
            }
            // If the scanned character is an operator, pop two
            // elements from stack apply the operator
            else
            {
                flag=false;
                double val1 = stack.pop();
                double val2 = stack.pop();

                switch(c)
                {
                    case '+':
                        stack.push(val2+val1);
                        break;

                    case '-':
                        stack.push(val2- val1);
                        break;

                    case '/':
                        stack.push(val2/val1);
                        break;

                    case 'x':
                        stack.push(val2*val1);
                        break;
                    case '%':
                        stack.push(val2%val1);
                        break;
                }
            }
        }
        return stack.pop();
    }
    private int Prec(char ch)
    {
        switch (ch)
        {
            case '+':
            case '-':
                return 1;

            case 'x':
            case '/':
                return 2;

            case '%':
                return 3;
        }
        return -1;
    }

    // The main method that converts given infix expression
    // to postfix expression.
   private String infixToPostfix(String exp)
    {
        // initializing empty String for result
        String result = new String("");

        // initializing empty stack
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i<exp.length(); ++i)
        {
            char c = exp.charAt(i);

            // If the scanned character is an operand, add it to output.
            if (Character.isDigit(c) || c=='.')
                result += c;

                // If the scanned character is an '(', push it to the stack.
            else if (c == '(')
                stack.push(c);

                //  If the scanned character is an ')', pop and output from the stack
                // until an '(' is encountered.
            else if (c == ')')
            {
                while (!stack.isEmpty() && stack.peek() != '(')
                    result += stack.pop();

                if (!stack.isEmpty() && stack.peek() != '(')
                    return "Invalid Expression"; // invalid expression
                else
                    stack.pop();
            }
            else // an operator is encountered
            {
                while (!stack.isEmpty() && Prec(c) <= Prec(stack.peek())){
                    if(stack.peek() == '(')
                        return "Invalid Expression";
                    result+=' ';
                    result += stack.pop();
                }
                result+=' ';
                stack.push(c);
            }

        }

        // pop all the operators from the stack
        while (!stack.isEmpty()){
            if(stack.peek() == '(')
                return "Invalid Expression";
            result+=' ';
            result += stack.pop();
        }
        return result;
    }
    public void addDecimal(View v)
    {
        TextView tv = (TextView) v;
        if(flagDecimal==false)
        {
            s=s+".";
            flagDecimal=true;
        }
//        if(s.length()==0)
//        {
//            s = s + tv.getText().toString().trim();
//        }
//        else if(s.charAt(s.length()-1)!='.') {
//            s = s + tv.getText().toString().trim();
//        }
        equation.setText(s);
    }
    public void ondel(View v)
    {

        if (s.length() >= 1)
        {
            if(s.charAt(s.length()-1)=='.')
            {
                flagDecimal=false;
            }
            s = s.substring(0, s.length() - 1);
            if(s.length()>=1) {
                if (!(s.charAt(s.length() - 1) == '+' || s.charAt(s.length() - 1) == '-' || s.charAt(s.length() - 1) == '/' || s.charAt(s.length() - 1) == 'x'|| s.charAt(s.length() - 1) == '%')) {

                    String postfixExp = infixToPostfix(s);
                    double evaluatedExp = evaluatePostfix(postfixExp);
                    finalAnswer = evaluatedExp;
                    equation.setText(s);
                    result.setText(evaluatedExp + "");
                }
                equation.setText(s);
            }
            else
            {
                result.setText("");
                equation.setText("");
                finalAnswer=0;
                s="";
                flag=false;
                flagDecimal=false;
            }

        }
    }
    public void onAC(View v)
    {
        result.setText("");
        equation.setText("");
        finalAnswer=0;
        s="";
        flag=false;
        flagDecimal=false;
    }
      public void onEqual(View v)
    {
        String postfixExp=infixToPostfix(s);
        double evaluatedExp = evaluatePostfix(postfixExp);
        if(!Double.isInfinite(evaluatedExp)) {
            finalAnswer = evaluatedExp;
            equation.setText(finalAnswer+"");
            result.setText( "");
        }
        else {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
        flag=false;
    }
}
