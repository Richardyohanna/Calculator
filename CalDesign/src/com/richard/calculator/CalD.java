package com.richard.calculator;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

//import net.objecthunter.exp4j.Expression;
//import net.objecthunter.exp4j.ExpressionBuilder;

import net.miginfocom.swing.MigLayout;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalD {	
	
	private JTextField area = new JTextField("Enter any number");	
	private JButton btns[] = new JButton[20];	 
	private String btn[] = {"CE", "%", "M", "DEL",
					"1" , "2", "3", "+",
					"4" , "5", "6", "-",
					"7" , "8", "9", "/",
					"0" , ".", "=", "x"
					};
	private StringBuilder save = new StringBuilder();
	private List<String> btnlist = new ArrayList<>();
	private int k;
	
	public CalD() {
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setSize(365,330);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		
		area.setEditable(false);
		area.setHorizontalAlignment(JTextField.CENTER);
		area.setPreferredSize(new java.awt.Dimension(100,60));
		area.setBackground(new Color(30,30,30));
		
	
		// miglayout constraints , column Constraints, row constraints wrap 3", 
		//"[grow, sizegroup btn][grow, sizegroup btn][grow, sizegroup btn]", "[]10[]10[]"		
		JPanel panel = new JPanel(); 
		panel.setLayout(new MigLayout("wrap 4, fill", "[grow, sizegroup btn, center]"
													+ "[grow, sizegroup btn, center]"
													+ "[grow, sizegroup btn, center]"
													+ "[grow, sizegroup btn, center]",
													"[]10[]10[]10[]"));
		panel.setBackground(new Color(0));
		panel.add(area, "span, grow");
		
	
		for (int i = 0; i < 5; i++) {
			for(int j = 0; j < 4; j++) {
				
				//increment the value of k in each loop by one
				k = k + 1;

				//Instantiate the buttons
				btns[k-1] = new JButton(btn[k-1]) {
		            @Override
		            protected void paintComponent(Graphics g) {
		                Graphics2D g2 = (Graphics2D) g;
		                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		                g2.setColor(getBackground());
		                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded corners
		                super.paintComponent(g2);
		            }
		         };
		         
		         

				btns[k-1].setPreferredSize(new java.awt.Dimension(100, 50));
				btns[k-1].setFocusPainted(false);
				btns[k-1].setContentAreaFilled(false);
				btns[k-1].setBorderPainted(false);
				btns[k-1].setBackground(new Color(100, 200, 100));
				btns[k-1].setFont(new Font("Arial", Font.PLAIN, 16));					
				btns[k-1].setForeground(Color.BLACK);

			
				//color of the equal to sign
				if(k-1 == 18) {
					btns[k-1].setBackground(new Color(255, 164, 0));
				}
				
			   
				if(k % 4 == 0) {
					btns[k-1].setBackground(new Color(100, 150, 100));
					panel.add(btns[k-1], "wrap, grow");
				}
				
				//color of the delete
				if(k-1 == 3) {
					btns[k-1].setBackground(new Color(255, 50, 50));
				}
				else {
					panel.add(btns[k-1]);
				}
				
				
			}
		    
		}
		
		for(JButton bt : btns) {

			bt.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					 String buttonText = ((JButton) e.getSource()).getText();					
					 handleButtonPress(buttonText);
				}
				
			});
		}
		
		
		frame.add(panel);
		frame.repaint();
		frame.revalidate();
		
		
		
		
	}
 	
	private void handleButtonPress(String buttonText) {
	    switch (buttonText) {
	        case "CE":
	            clearText();
	            break;

	        case "DEL":
	            deleteLastCharacter();
	            break;

	        case "=":
	            calculate();
	            break;
	            
	        case "M":
	        	history();
	        	break;
	        	
	        case "%":
	        	//position();
	            appendOperator(buttonText);
	            break;

	        case "+":
	        case "-":
	        case "/":
	        case "x":	       
	            appendOperator(buttonText);
	            break;

	        default:
	            appendText(buttonText);
	            break;
	    }
	}

	private void appendOperator(String operator) {
	    if (save.length() > 0 && !isLastCharacterOperator()) {
	        save.append(operator.equals("x") ? "*" : operator); // Replace 'x' with '*'
	        area.setText(save.toString());
	    }
	}

	private boolean isLastCharacterOperator() {
	    if (save.length() == 0) return false;
	    char lastChar = save.charAt(save.length() - 1);
	    return "+-*/%".indexOf(lastChar) != -1;
	}

	private void calculate() {
	    try {
	        String expression = save.toString();
	        double result = evaluateExpression(expression);

	        // Update the history
	        btnlist.add(expression + "="+ result);

	        // Display result and reset input
	        save.setLength(0);
	        save.append(result);
	        area.setText(save.toString());
	        System.out.println(save.toString());
	    } catch (IllegalArgumentException ex) {
	        area.setText("Error: Invalid Expression");
	        save.setLength(0); // Clear the input on error
	    }
	}

	private double evaluateExpression(String expression) {
		try {
	        Expression exp = new ExpressionBuilder(expression).build();
	        return exp.evaluate();
	    } catch (Exception e) {
	        throw new IllegalArgumentException("Invalid Expression: " + e.getMessage());
	    }
	}
	
	private void JAreaDesign(JTextField area) {
		
		area.setFont(new Font("arial", Font.PLAIN, 17));
		area.setForeground(new Color(255,255,255));
		area.setText(save.toString());
	}
	

	private void history() {
		save.setLength(0);
		save.append(btnlist.get(btnlist.size()-1));
	    JAreaDesign(area);
		
	}

    private void appendText(String text) {
        save.append(text);
        JAreaDesign(area);
	}

    private void clearText() {
        save.setLength(0); // Clear all text
        JAreaDesign(area);
    }

    private void deleteLastCharacter() {
        if (save.length() > 0) {
            save.deleteCharAt(save.length() - 1);
            JAreaDesign(area);
        }
    }

   
    
  

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new CalD();

	}
	
	

}
