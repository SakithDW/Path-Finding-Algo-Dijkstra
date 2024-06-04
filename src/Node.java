public class Node {

    private int row;
    private int col;
    private char character;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }



}
