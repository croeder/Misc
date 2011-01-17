
// Chris Roeder 2/5/04
// translated to java 2/14/04

public class One {

public static void main(String args[])
{
     Point pt = new Point(2,3);
     Piece p[] = new Piece[7];
     p[3] = new Knight(pt);
     p[1] = new Bishop(pt);
     p[2] = new Rook(pt);
     p[0] = new Queen(pt);
     p[4] = new King(pt);
     p[5] = new Pawn(pt, false);
     p[6] = new Pawn(pt, true);
     int r[][] = new int[8][8];

     // remember, this obstacle array is upside down form how its displayed!
     // positionless pieces used now for display only
     Piece z = null;
     Piece P = new Pawn(true);
     Piece B = new Bishop();
     Piece N = new Knight();
     Piece K = new King();
     Piece R = new Rook();
     Piece Q = new Queen();
     Piece  obs[][] = { 
                       {z,B,z,z,z,z,z,z},
                       {z,N,z,z,z,z,z,z},
                       {z,K,z,z,z,z,z,z},
                       {z,B,z,z,z,z,z,P},
                       {z,R,z,z,z,z,P,z},
                       {z,Q,z,z,z,P,z,z},
                       {P,P,P,P,P,P,P,P},
                       {z,K,z,z,z,z,z,z} };
	 ReachableBoard rb;
     for (int i=0; i<7; i++) {
         //p[i].print15x15();
         //rb = p[i].getReachableBoardNoObs();
         //rb.printBoard();
     
         rb = p[i].getReachableBoard(obs, obs);
         rb.printBoard(obs);    
     }
}
}
