import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

//Name : Sakith Deepna Wijenanda
//ID : 20220273

public class Main {


    private static final int[][] directions = new int[][] {{1, 0},{-1, 0}, {0, 1}, {0, -1}};

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        while(true) {
            String file = chooseFile();
            long start = System.currentTimeMillis();
            Map map = createMap(file);
            List<Integer[]> answer = solve(map);

            long stop = System.currentTimeMillis();
            double countedTime = stop - start;

            printPath(answer);

            System.out.println();
            System.out.println("Time = " + countedTime + " milliseconds");
            System.out.println("Do you want to check another maze:(Y/N)");
            String choice = input.next();
            if(choice.equalsIgnoreCase("Y")||choice.equalsIgnoreCase("N")){
                if (choice.equalsIgnoreCase("N")){
                    break;
                }
            }
            else {
                System.out.println("Invalid Input");
                break;
            }
        }
    }
    public static String chooseFile(){

        System.out.println("Enter the name of the file");
        String fileName = input.next();

        return "./benchmark_series/"+fileName+".txt";

    }
    public static Map createMap(String filePath){

        Map map = new Map();


        try {
            // Read the puzzle from the file
            FileReader fileReader = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;


            // Read the dimensions of the puzzle
            int numRows = 0;
            int numCols = 0;
            while ((line = reader.readLine()) != null) {
                numRows++;
                numCols = line.length();
            }


            // Reset reader
            reader.close();
            reader = new BufferedReader(new FileReader(filePath));

            // Create a 2D array to hold the puzzle
            map.setGrid(new Node[numRows][numCols]);


            // Fill the puzzle grid with the characters from the file
            int row = 0;

            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < numCols; col++) {
                    Node node = new Node(row,col);
                    map.getGrid()[row][col] = node;

                    if(line.charAt(col)=='.'){
                        node.setCharacter('.');
                    }else if (line.charAt(col)=='S') {
                        node.setCharacter('S');

                    }else if (line.charAt(col)=='F') {
                        node.setCharacter('F');

                    }else {
                        node.setCharacter('0');
                    }
                }
                row++;
            }
            // Close the reader
            reader.close();


        }catch (FileNotFoundException e){
            System.err.println("File not found"+ e);
            System.out.println("Enter a valid file Path");
        }catch (IOException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }catch (IndexOutOfBoundsException e) {
            System.err.println("Error"+e.getMessage());
        }
        return map;
    }

    public static void printMaze(Map map){
        // Print the puzzle grid
        for (Node[] rowArray : map.getGrid()) {
            for (Node node : rowArray) {
                System.out.print(node.getCharacter());
//                System.out.print("g"+node.getgCost()+",h"+ node.gethCost()+"  ");
            }
            System.out.println();
        }
    }
    public static List<Integer[]> solve(Map map) {
        int rows = map.getGrid().length;
        int columns = map.getGrid()[0].length;
        int startRow = 0;
        int startCol = 0;
        int endCol = 0;
        int endRow = 0;

        for(Node[]array : map.getGrid()) {
            for (Node node: array) {
                if(node.getCharacter()=='S') {
                    startRow = node.getRow();
                    startCol = node.getCol();
                }
                if (node.getCharacter()=='F') {
                    endRow = node.getRow();
                    endCol = node.getCol();
                }
            }
        }

        int[][] distance = new int[rows][columns];

        for (int i = 0; i < distance.length; i++) {
            for(int j = 0; j < columns; j++) {
                distance[i][j] = Integer.MAX_VALUE;
            }
        }
        PriorityQueue<Queue> priorityQueue = new PriorityQueue<>();
        Integer[] array = new Integer[] {startRow, startCol};

        System.out.println();

        List<Integer[]> arrayList = new ArrayList<>();
        arrayList.add(array);
        priorityQueue.add(new Queue(0, startRow, startCol,  arrayList));

        while (!priorityQueue.isEmpty()) {
            Queue queueObj = priorityQueue.remove();
            List<Integer[]> path = queueObj.coordinates;
            if ((path.getLast()[0] == endRow) && (path.getLast()[1] == endCol)) {
                return path;
            }

            for(int[] direction : directions) {
                int count = 0;
                int currentRow = queueObj.row;
                int currentColumn = queueObj.col;
                int rowDirection = direction[0];
                int columnDirection = direction[1];


                while (((0 <= currentRow + rowDirection) && (currentRow + rowDirection < rows)) &&
                        ((0 <=  currentColumn+ columnDirection) && (currentColumn + columnDirection < columns)) &&
                        map.getGrid()[currentRow +rowDirection][currentColumn + columnDirection].getCharacter()!='0') {
                    count += 1;
                    currentRow += rowDirection;
                    currentColumn += columnDirection;
                    if (map.getGrid()[currentRow][currentColumn].getCharacter()=='F'){
                        path.add(new Integer[] {currentRow, currentColumn});
                        return path ;
                    }
                }

                if (distance[currentRow][currentColumn] == Integer.MAX_VALUE ||
                        queueObj.distance + count < distance[currentRow][currentColumn]) {
                    distance[currentRow][currentColumn] = queueObj.distance + count;
                    List<Integer[]> integers = new ArrayList<>(path);
                    integers.add(new Integer[] {currentRow, currentColumn});
                    priorityQueue.add(new Queue(queueObj.distance + count, currentRow, currentColumn, integers));
                }

            }
        }
        return new ArrayList<>();
    }

    public static void printPath(List<Integer[]> path) {
        if (!path.isEmpty()) {
            System.out.printf("1. Start at: (%d, %d) \n", path.get(0)[1] + 1, path.get(0)[0] + 1);
            for (int i = 1; i < path.size(); i++) {
                int currentX = path.get(i)[0];
                int currentY = path.get(i)[1];
                int previousX = path.get(i - 1)[0];
                int previousY = path.get(i - 1)[1];
                int pathX = currentX - previousX;
                int pathY = currentY - previousY;
                if (pathX < 0) {
                    System.out.printf((i + 1) + ". Move up to : (%d, %d) \n", path.get(i)[1] + 1, path.get(i)[0] + 1);
                } else if (pathX > 0) {
                    System.out.printf((i + 1) + ". Move Down to : (%d, %d) \n", path.get(i)[1] + 1, path.get(i)[0] + 1);
                } else if (pathY < 0) {
                    System.out.printf((i + 1) + ". Move Left to : (%d, %d) \n", path.get(i)[1] + 1, path.get(i)[0] + 1);
                } else {
                    System.out.printf((i + 1) + ". Move Right to : (%d, %d) \n", path.get(i)[1] + 1, path.get(i)[0] + 1);
                }
            }
            System.out.println((path.size() + 1) + ". Done!");
        }
    }
}