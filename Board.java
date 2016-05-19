import java.util.ArrayList;
import java.util.List;

public class Board {
    List<Coordinate> availablePoints;
    int[][] board = new int[3][3]; 

    List<Data> childrenUtility = new ArrayList<>();

    public int evaluateBoard() {
        int utility = 0;

        //Check all rows
        for (int i = 0; i < 3; ++i) {
            int X = 0;
            int O = 0;
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == 2) {
                	O++;
                } else if (board[i][j] == 1) {
                    X++;
                }

            } 
            utility+=changeInUtility(X, O); 
        }

        //Check all columns
        for (int j = 0; j < 3; ++j) {
            int X = 0;
            int O = 0;
            for (int i = 0; i < 3; ++i) {
                if (board[i][j] == 2) {
                    O++;
                } else if (board[i][j] == 1) {
                    X++;
                } 
            }
            utility+=changeInUtility(X, O);
        }

        int X = 0;
        int O = 0;

        //Check diagonal (first)
        for (int i = 0, j = 0; i < 3; ++i, ++j) {
            if (board[i][j] == 1) {
                X++;
            } else if (board[i][j] == 2) {
                O++;
            }
        }

        utility+=changeInUtility(X, O);

        X = 0;
        O = 0;

        //Check Diagonal (Second)
        for (int i = 2, j = 0; i > -1; --i, ++j) {
            if (board[i][j] == 1) {
                X++;
            } else if (board[i][j] == 2) {
                O++;
            }
        }

        utility+=changeInUtility(X, O);

        return utility;
    }
    
    private int changeInUtility(int X, int O){
        int change;
        if (X == 3) {
            change = 100;
        } else if (X == 2 && O == 0) {
            change = 10;
        } else if (X == 1 && O == 0) {
            change = 1;
        } else if (O == 3) {
            change = -100;
        } else if (O == 2 && X == 0) {
            change = -10;
        } else if (O == 1 && X == 0) {
            change = -1;
        } else {
            change = 0;
        } 
        return change;
    }
    
    //Set this to some value if you want to have some specified depth limit for search
    int uptoDepth = -1;
    
    public int alphaBetaMinimax(int alpha, int beta, int depth, int turn){
        
        if(beta<=alpha){
        	System.out.println("Pruning at depth = " + depth);
            if(turn == 1) return Integer.MAX_VALUE;
            else return Integer.MIN_VALUE; 
        }
        
        if(depth == uptoDepth || isGameOver()) return evaluateBoard();
        
        List<Coordinate> pointsAvailable = getAvailableStates();
        
        if(pointsAvailable.isEmpty()) return 0;
        
        if(depth==0) childrenUtility.clear(); 
        
        int maxValue = Integer.MIN_VALUE;
        int minValue = Integer.MAX_VALUE;
        
        for(int i=0;i<pointsAvailable.size(); ++i){
        	Coordinate point = pointsAvailable.get(i);
            
            int currentUtility = 0;
            
            if(turn == 1){
                placeAMove(point, 1); 
                currentUtility = alphaBetaMinimax(alpha, beta, depth+1, 2);
                maxValue = Math.max(maxValue, currentUtility); 
                
                //Set alpha
                alpha = Math.max(currentUtility, alpha);
                
                if(depth == 0)
                    childrenUtility.add(new Data(currentUtility, point));
            }else if(turn == 2){
                placeAMove(point, 2);
                currentUtility = alphaBetaMinimax(alpha, beta, depth+1, 1); 
                minValue = Math.min(minValue, currentUtility);
                
                //Set beta
                beta = Math.min(currentUtility, beta);
            }
            //reset board
            board[point.x][point.y] = 0; 
            
            //If a pruning has been done, don't evaluate the rest of the sibling states
            if(currentUtility == Integer.MAX_VALUE || currentUtility == Integer.MIN_VALUE) break;
        }
        return turn == 1 ? maxValue : minValue;
    }  

    public boolean isGameOver() {				//Game is over is someone has won, or board is full (draw)
        return (xWins() || oWins() || getAvailableStates().isEmpty());
    }

    public boolean xWins() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 1)
        			|| (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 1)) {
        	return true;
        }
        for (int i = 0; i < 3; ++i) {
            if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 1)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 1))) {
            	return true;
            }
        }
        return false;
    }

    public boolean oWins() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 2) 
        			|| (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 2)) {
        	return true;
        }
        for (int i = 0; i < 3; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 2)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 2)) {
            	return true;
            }
        }

        return false;
    }

    public List<Coordinate> getAvailableStates() {
        availablePoints = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Coordinate(i, j));
                }
            }
        }
        return availablePoints;
    }

    public void placeAMove(Coordinate point, int player) {
        board[point.x][point.y] = player;   //player = 1 for X, 2 for O
    }

    public Coordinate returnBestMove() {
        int MAX = -100000;
        int best = -1;

        for (int i = 0; i < childrenUtility.size(); ++i) {
            if (MAX < childrenUtility.get(i).utility) {
                MAX = childrenUtility.get(i).utility;
                best = i;
            }
        }

        return childrenUtility.get(best).coordinate;
    }
    
    public void resetBoard() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                board[i][j] = 0;
            }
        }
    } 
}