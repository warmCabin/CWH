package cop.poop.cwh;

import java.util.HashSet;

public class ChessLogic {

    //Base Piece Class
    //ChessLogic.Piece piece; import static whatever.something.ChessLogic.*;
    //This is a Christian logic class, so please, NO swearing!
    public static abstract class Piece {
        protected boolean white;
        protected int drawableID;
        public Piece(boolean isWhite) {
            white=isWhite;
            drawableID = white ? R.drawable.white_default : R.drawable.black_default;
        }
        abstract HashSet<Integer> getMoves(int loc, Piece[][] board);
        boolean inBounds(int r,int c) {
            return r>=0&&r<8&&c>=0&&c<8;
        }
        public int getDrawableID(){
            return drawableID; //lol
        }
    }

    //Pawn
    public static class Pawn extends Piece {
        public Pawn(boolean isWhite) {
            super(isWhite);
            drawableID = white ? R.drawable.white_pawn : R.drawable.black_pawn;

        }
        HashSet<Integer> getMoves(int loc, Piece[][] board){
            HashSet<Integer> moves=new HashSet<Integer>();
            int r=loc/8,c=loc%8;

            //If the Pawn is white, it starts on row 6 and goes up (down? I hate that convention.), otherwise, it starts on row 1 and goes down.
            int dr=-1,start=6;
            if(!white) {
                dr=1;
                start=1;
            }
            if(inBounds(r+dr,c)&&board[r+dr][c]==null) {
                board[r+dr][c]=board[r][c];
                board[r][c]=null;
                //FIXME: There's infinite recursion here.
                if(!inCheck(board[r+dr][c].white,board))
                    moves.add((r+dr)*8+c);
                board[r][c]=board[r+dr][c];
                board[r+dr][c]=null;

                //Pawns can move two squares on their first turn.
                if(r==start) {
                    if(inBounds(r+2*dr,c)&&board[r+2*dr][c]==null) {
                        board[r+2*dr][c]=board[r][c];
                        board[r][c]=null;
                        if(!inCheck(board[r+2*dr][c].white,board))
                            moves.add((r+2*dr)*8+c);
                        board[r][c]=board[r+2*dr][c];
                        board[r+2*dr][c]=null;
                    }
                }
            }

            //Check if it can capture
            //TODO: We need a reference to the captured piece, or at least a flag to indicate someting was captured.
            //We may need to put a makeMove function in here or something
            if(inBounds(r+dr,c-1)&&board[r+dr][c-1]!=null&&board[r+dr][c-1].white!=white) {
                Piece temp=board[r+dr][c-1];
                board[r+dr][c-1]=board[r][c];
                board[r][c]=null;
                if(!inCheck(board[r+dr][c-1].white,board))
                    moves.add((r+dr)*8+c-1);
                board[r][c]=board[r+dr][c-1];
                board[r+dr][c-1]=temp;
            }
            if(inBounds(r+dr,c+1)&&board[r+dr][c+1]!=null&&board[r+dr][c+1].white!=white) {
                Piece temp=board[r+dr][c+1];
                board[r+dr][c+1]=board[r][c];
                board[r][c]=null;
                if(!inCheck(board[r+dr][c+1].white,board))
                    moves.add((r+dr)*8+c+1);
                board[r][c]=board[r+dr][c+1];
                board[r+dr][c+1]=temp;
            }

            return moves;
        }
    }

    //King
    public static class King extends Piece {
        public King(boolean isWhite) {
            super(isWhite);
            drawableID = white ? R.drawable.white_king : R.drawable.black_king;
        }
        HashSet<Integer> getMoves(int loc,Piece[][] board) {
            int r=loc/8,c=loc%8;
            int[] dr= {-1,-1,0,1,1,1,0,-1},dc= {0,1,1,1,0,-1,-1,-1};
            HashSet<Integer> moves=new HashSet<Integer>();

            //Check all eight directions
            for(int i=0;i<8;i++) {
                int r2=r+dr[i],c2=c+dc[i];
                if(inBounds(r2,c2)&&(board[r2][c2]==null||board[r2][c2].white!=white)) {
                    Piece temp=board[r2][c2];
                    board[r2][c2]=board[r][c];
                    board[r][c]=null;
                    if(!inCheck(board[r2][c2].white,board))
                        moves.add(r2*8+c2);
                    board[r][c]=board[r2][c2];
                    board[r2][c2]=temp;
                }
            }
            return moves;
        }
    }

    //Knight
    public static class Knight extends Piece {
        public Knight(boolean isWhite) {
            super(isWhite);
            drawableID = white ? R.drawable.white_knight : R.drawable.black_knight;
        }
        HashSet<Integer> getMoves(int loc,Piece[][] board){
            int r=loc/8,c=loc%8;
            int[] dr= {-2,-1,1,2,2,1,-1,-2}, dc= {1,2,2,1,-1,-2,-2,-1};
            HashSet<Integer> moves=new HashSet<Integer>();
            for(int i=0;i<8;i++) {
                int r2=r+dr[i],c2=c+dc[i];
                if(inBounds(r2,c2)&&(board[r2][c2]==null||board[r2][c2].white!=white)) {
                    Piece temp=board[r2][c2];
                    board[r2][c2]=board[r][c];
                    board[r][c]=null;
                    if(!inCheck(board[r2][c2].white,board))
                        moves.add(r2*8+c2);
                    board[r][c]=board[r2][c2];
                    board[r2][c2]=temp;
                }
            }
            return moves;
        }
    }

    //Rook
    public static class Rook extends Piece {
        public Rook(boolean isWhite) {
            super(isWhite);
        }
        HashSet<Integer> getMoves(int loc,Piece[][] board) {
            int r=loc/8,c=loc%8;
            int[] dr= {-1,0,1,0},dc= {0,1,0,-1};
            HashSet<Integer> moves=new HashSet<Integer>();
            for(int i=0;i<4;i++) {
                int r2=r,c2=c;
                //Trying going as far as possible in each direction
                for(int j=0;j<8;j++) {
                    r2+=dr[i];
                    c2+=dc[i];
                    if(!inBounds(r2,c2))
                        break;
                    if(board[r2][c2]==null) {
                        board[r2][c2]=board[r][c];
                        board[r][c]=null;
                        if(!inCheck(board[r2][c2].white,board))
                            moves.add(r2*8+c2);
                        board[r][c]=board[r2][c2];
                        board[r2][c2]=null;
                        continue;
                    }
                    //Capture
                    if(board[r2][c2].white!=white) {
                        Piece temp=board[r2][c2];
                        board[r2][c2]=board[r][c];
                        board[r][c]=null;
                        if(!inCheck(board[r2][c2].white,board))
                            moves.add(r2*8+c2);
                        board[r][c]=board[r2][c2];
                        board[r2][c2]=temp;
                    }

                    //If there was a Piece here, the rook can't go any further in this direction.
                    break;
                }
            }
            return moves;
        }
    }

    //Bishop
    public static class Bishop extends Piece {
        public Bishop(boolean isWhite) {
            super(isWhite);
            drawableID = white ? R.drawable.white_bishop : R.drawable.black_bishop;
        }
        HashSet<Integer> getMoves(int loc,Piece[][] board){
            int r=loc/8,c=loc%8;
            int[] dr= {-1,1,1,-1},dc= {1,1,-1,-1};
            HashSet<Integer> moves=new HashSet<Integer>();
            for(int i=0;i<4;i++) {
                int r2=r,c2=c;
                //Trying going as far as possible in each direction
                for(int j=0;j<8;j++) {
                    r2+=dr[i];
                    c2+=dc[i];
                    if(!inBounds(r2,c2))
                        break;
                    if(board[r2][c2]==null) {
                        Piece temp=board[r2][c2];
                        board[r2][c2]=board[r][c];
                        board[r][c]=null;
                        if(!inCheck(board[r2][c2].white,board))
                            moves.add(r2*8+c2);
                        board[r][c]=board[r2][c2];
                        board[r2][c2]=temp;
                        continue;
                    }
                    //Capture
                    if(board[r2][c2].white!=white) {
                        Piece temp=board[r2][c2];
                        board[r2][c2]=board[r][c];
                        board[r][c]=null;
                        if(!inCheck(board[r2][c2].white,board))
                            moves.add(r2*8+c2);
                        board[r][c]=board[r2][c2];
                        board[r2][c2]=temp;
                    }

                    break;
                }
            }
            return moves;
        }
    }

    //Queen
    public static class Queen extends Piece {
        public Queen(boolean isWhite) {
            super(isWhite);
            drawableID = white ? R.drawable.white_queen : R.drawable.black_queen;
        }

        //This is basically a rook with a king's dr/dc array.
        HashSet<Integer> getMoves(int loc,Piece[][] board){
            int r=loc/8,c=loc%8;
            int[] dr= {-1,-1,0,1,1,1,0,-1},dc= {0,1,1,1,0,-1,-1,-1};
            HashSet<Integer> moves=new HashSet<Integer>();
            for(int i=0;i<8;i++) {
                int r2=r,c2=c;
                for(int j=0;j<8;j++) {
                    r2+=dr[i];
                    c2+=dc[i];
                    if(!inBounds(r2,c2))
                        break;
                    if(board[r2][c2]==null) {
                        Piece temp=board[r2][c2];
                        board[r2][c2]=board[r][c];
                        board[r][c]=null;
                        if(!inCheck(board[r2][c2].white,board))
                            moves.add(r2*8+c2);
                        board[r][c]=board[r2][c2];
                        board[r2][c2]=temp;
                        continue;
                    }
                    //Capture
                    if(board[r2][c2].white!=white) {
                        Piece temp=board[r2][c2];
                        board[r2][c2]=board[r][c];
                        board[r][c]=null;
                        if(!inCheck(board[r2][c2].white,board))
                            moves.add(r2*8+c2);
                        board[r][c]=board[r2][c2];
                        board[r2][c2]=temp;
                    }
                    break;
                }
            }
            return moves;
        }
    }


    static public int findKing(boolean color,Piece[][] board) {
        for(int i=0;i<8;i++) {
            for(int j=0;j<8;j++) {
                if(board[i][j]!=null&&board[i][j] instanceof King&&board[i][j].white==color)
                    return i*8+j;
            }
        }
        return -1;
    }

    static boolean inCheck(boolean color,Piece[][] board) {
        if(true) return false; //keeps the compiler happy
        int loc=findKing(color,board);
        for(int i=0;i<8;i++) {
            for(int j=0;j<8;j++) {
                if(board[i][j]!=null&&board[i][j].white!=color) {
                    HashSet<Integer> moves=board[i][j].getMoves(i*8+j, board);
                    if(moves.contains(loc))
                        return true;
                }
            }
        }
        return false;
    }

    //One for checkmate, two for draw, zero for neither
    public static int gameOver(boolean color,Piece[][] board) {
        boolean check=inCheck(color,board);
        for(int i=0;i<8;i++) {
            for(int j=0;j<8;j++) {
                if(board[i][j]!=null&&board[i][j].white==color) {
                    if(board[i][j].getMoves(i*8+j,board).size()!=0)
                        return 0;
                }
            }
        }
        if(check)
            return 1;
        return 2;
    }
}