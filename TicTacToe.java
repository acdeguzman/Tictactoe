import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TicTacToe extends JFrame implements ActionListener{
	JPanel tictactoe = new JPanel(new GridLayout(3, 3));
	JButton[][] button = new JButton[3][3];
	Board board = new Board();
	
	public TicTacToe() {		
		
		for(int i=0; i<3;i++){
			for(int j=0; j<3; j++){
				button[i][j]= new JButton();
				tictactoe.add(button[i][j]);
				button[i][j].setBackground(Color.WHITE);
				button[i][j].addActionListener(this);
				button[i][j].putClientProperty("1",i);
				button[i][j].putClientProperty("2",j);
			}
		}
		
		add(tictactoe);
		setSize(300, 300);
        setVisible(true);
	}
	
    public static void main(String[] args) {
    	new TicTacToe();
    }
    
    public void actionPerformed(ActionEvent evt) {							//goes here when a player clicks a button
    	JButton btn =  (JButton) evt.getSource();
    	int a = (int) btn.getClientProperty("1");								//checks the i value
    	int b = (int) btn.getClientProperty("2");								//checks the j value

        if (!board.isGameOver()) {
        	Coordinate userMove = new Coordinate(a,b);

            board.placeAMove(userMove, 2); //2 for O and O is the user
            button[a][b].setBackground(Color.GREEN);
            
            if (!board.isGameOver()) {            
	            board.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
	            for (Data pas : board.rootsChildrenScore) 
	                System.out.println("Point: " + pas.point + " Score: " + pas.score);
	            
	            board.placeAMove(board.returnBestMove(), 1);
	            int bestX = board.returnBestMove().x;
	            int bestY = board.returnBestMove().y;				
	    	   	button[bestX][bestY].setBackground(Color.BLUE);
            }
        }
    	
        if (board.hasXWon()) {
            System.out.println("Unfortunately, you lost!");
        } else if (board.hasOWon()) {
            System.out.println("You win!");
        } else if (board.isGameOver()){
            System.out.println("It's a draw!");   
        }
   }
}

//Reference: http://www.codebytes.in/2014/11/alpha-beta-pruning-minimax-algorithm.html