import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//@SuppressWarnings("serial")
public class TicTacToe extends JFrame implements ActionListener{
	JPanel tictactoe = new JPanel();
	JPanel cont = new JPanel();
	GridLayout grid = new GridLayout(3,3);
	JButton[][] button = new JButton[3][3];
	Board board = new Board();
	JButton first = new JButton("AI first");
	JButton reset = new JButton("Reset");
	
	public TicTacToe() {		
		
		for(int i=0; i<3;i++){
			for(int j=0; j<3; j++){
				button[i][j]= new JButton();
				tictactoe.add(button[i][j]);
				button[i][j].setBackground(Color.BLACK);
				button[i][j].addActionListener(this);
				button[i][j].putClientProperty("1",i);
				button[i][j].putClientProperty("2",j);
				button[i][j].setFont(new Font("Arial", Font.BOLD, 70));
				button[i][j].setForeground(Color.WHITE);
			}
		}

		tictactoe.setPreferredSize(new Dimension(300, 300));
		tictactoe.setLayout(grid);

		cont.setPreferredSize(new Dimension(300,400));
		cont.add(tictactoe);
		cont.add(first);
		cont.add(reset);

		setPreferredSize(new Dimension(300,400));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(cont);
        setVisible(true);
        pack();
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
            button[a][b].setText("X");
            
            if (!board.isGameOver()) {            
	            board.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
	            
	            board.placeAMove(board.returnBestMove(), 1);
	            int bestX = board.returnBestMove().x;
	            int bestY = board.returnBestMove().y;				
	    	   	//button[bestX][bestY].setBackground(Color.BLUE);
	    	   	button[bestX][bestY].setText("O");
            }
        }
    	
        if (board.xWins()) {
        	JOptionPane.showMessageDialog(cont, "You Lost!", "You Lost!", JOptionPane.CLOSED_OPTION);
        	resetBoard();
        } else if (board.oWins()) {
        	JOptionPane.showMessageDialog(cont, "You Win!", "You Win!", JOptionPane.CLOSED_OPTION);
        	resetBoard();
        } else if (board.isGameOver()){
        	JOptionPane.showMessageDialog(cont, "Draw!", "Draw!", JOptionPane.CLOSED_OPTION);
        	resetBoard();
        }
   }

   public void resetBoard() {

		for(int i=0; i<3;i++){
			for(int j=0; j<3; j++){
				button[i][j].setText("");
				button[i][j].putClientProperty("1",i);
				button[i][j].putClientProperty("2",j);
				board.board[i][j] = 0;
			}
		}   		
   }
}

//Reference: http://www.codebytes.in/2014/11/alpha-beta-pruning-minimax-algorithm.html