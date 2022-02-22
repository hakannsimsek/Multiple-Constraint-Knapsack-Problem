package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Array;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        // define file variable here
        File file = new File("test4.txt");

        // give file variable to Reader objects
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        int lineCounter = 0;
        int totalKnapsackCount = 0, totalItemCount = 0;
        int itemLineCounter = 0, knapsackLineCounter = 0;
        int weightsFirstLine = 0, weightsLastLine = 0;
        int knapsackIter = 0, knapsackItemCounter = 0;
        // List that holds all the item objects
        List<Item> itemList = new ArrayList<Item>();
        // List that holds knapsack objects
        List<Knapsack> knapsackList = new ArrayList<Knapsack>();
        int[] tempWeights = new int[0];


        //  We read the code line by line with this while loop
        //  With first line we understand how many knapsacks and items we have
        //  Then we calculate which lines have the item values
        //  Then we know which line or lines has the knapsack capacities
        //  Then we calculate and read the lines that has the weights of the items
        //  for each individual knapsack

        // read the first line here
        while ((st = br.readLine()) != null) {
            if (lineCounter == 0) {
                System.out.println(st);

                // split the line from whitespace and get values
                String[] tokens = st.split(" ");
                totalKnapsackCount = Integer.parseInt(tokens[0]);
                totalItemCount = Integer.parseInt(tokens[1]);
                // calculate total lines that have item values
                itemLineCounter = (totalItemCount / 10) + 1;
                if (totalItemCount % 10 == 0) {
                    itemLineCounter--;
                }
                // calculate which line has knapsack capacity values
                knapsackLineCounter = itemLineCounter + (totalKnapsackCount / 10) + 1;
                if (totalKnapsackCount % 10 == 0) {
                    knapsackLineCounter--;
                }
                // calculate which lines has the knapsack weight values
                weightsFirstLine = knapsackLineCounter + 1;
                weightsLastLine = weightsFirstLine + (totalKnapsackCount * itemLineCounter);
                tempWeights = new int[totalItemCount];

                System.out.println("total knapsacks = " + totalKnapsackCount);
                System.out.println("total items = " + totalItemCount);
                System.out.println("knapsack line = " + knapsackLineCounter);

            }

            // create item objects and add them to the itemList
            if (lineCounter > 0 && lineCounter <= itemLineCounter) {
                int tokenId = 0;
                String[] tokens = st.split(" ");
                for (String token : tokens) {
                    int tempToken = Integer.parseInt(token);
                    int tempId = tokenId + (lineCounter - 1) * 10;
                    Item tempItem = new Item(tempId, tempToken);
                    itemList.add(tempItem);
                    tokenId++;
                }
            }

            // create knapsack objects and add them to the list
            if (lineCounter > itemLineCounter && lineCounter <= knapsackLineCounter) {
                int tokenId = 0;
                String[] tokens = st.split(" ");
                for (String token : tokens) {
                    int tempId = tokenId + (lineCounter - itemLineCounter - 1) * 10;
                    Knapsack tempKnapsack = new Knapsack(tempId, Integer.parseInt(tokens[tokenId]));
                    knapsackList.add(tempKnapsack);
                    tokenId++;
                }
            }

            // read each items weights on each knapsack and create weight values for
            // every individual knapsack
            if (lineCounter >= weightsFirstLine && lineCounter < weightsLastLine) {
                if (knapsackIter < totalKnapsackCount) {
                    String[] tokens = st.split(" ");
                    for (String token : tokens) {
                        tempWeights[knapsackItemCounter] = Integer.parseInt(token);
                        knapsackItemCounter++;
                    }
                    if (knapsackItemCounter == totalItemCount) {
                        knapsackList.get(knapsackIter).setWeights(tempWeights);

                        knapsackIter++;
                        knapsackItemCounter = 0;
                    }
                }

            }


            lineCounter++;
        }


        // creating a values array from item values to use in algorithm
        int itemCounter = 0;
        int[] values = new int[itemList.size()];
        for (Item item : itemList) {
            values[itemCounter] = item.getValue();
            itemCounter++;
        }

//----------------------------------------------------------------------------------------------------------------------
        int knapsackCount = totalKnapsackCount;
        int itemCount = totalItemCount;


        int[][] weights = new int[knapsackCount][itemCount];
        int[] knapsackCapacity = new int[totalKnapsackCount];

        //  Below we put the weights of item to weights[] array and knapsack capacities to knapsackCapacity[] array
        for (int i = 0; i < knapsackCount; i++) {
            System.arraycopy(knapsackList.get(i).getWeights(), 0, weights[i], 0, itemCount);
            knapsackCapacity[i] = knapsackList.get(i).getCapacity();
        }

        // Here we defined 2 dimentional valuePerWeight[][] array to calculate value per weight for each knapsacks
        double[][] valuePerWeight = new double[knapsackCount][itemCount];


        // Here we defined totalValuePerWeight[] array to sum up value per weight of an item to one dimentional array
        double[] totalValuePerWeight = new double[itemCount];

        // Below we defined knapsacks[][] 2 dimentional array to put item by 0s and 1s and last 2 space is for total weight and total value
        double[][] knapsacks = new double[knapsackCount][itemCount + 2];

        // Below for loop calculates value per weight for each item and knapsack by dividing value to weight
        for (int j = 0; j < knapsackCount; j++) {

            for (int i = 0; i < itemCount; i++) {

                if (weights[j][i] == 0) {
                    valuePerWeight[j][i] = 0;
                } else {
                    valuePerWeight[j][i] = 1.0 * values[i] / weights[j][i];
                }

                totalValuePerWeight[i] += valuePerWeight[j][i];

            }

        }

        //Below for loop we try to put item into knapsack with given constraints
        for (int x = 0; x < itemCount; x++) {
            //find max
            double max = 0;
            int maxIndex = 0;
            //Below for loop finds max in totalValuePerWeight[] array and stores it into maxIndex
            for (int i = 0; i < itemCount; i++) {
                if (max < totalValuePerWeight[i] && knapsacks[0][i] == 0) {
                    max = totalValuePerWeight[i];
                    maxIndex = i;
                }
            }

            // Below for loop checks if we put item found above into knapsack will it exceed capacity
            int capacityLimit = 1;
            for (int i = 0; i < knapsackCount; i++) {
                if (!(knapsackCapacity[i] > knapsacks[i][itemCount] + weights[i][maxIndex])) {
                    capacityLimit = 0;
                }
            }

            // Below for loop add item into knapsack if it is not going to exceed capacity determined above
            if (capacityLimit == 1) {
                for (int i = 0; i < knapsackCount; i++) {
                    knapsacks[i][maxIndex] = 1;
                    knapsacks[i][itemCount] += weights[i][maxIndex];
                    knapsacks[i][itemCount + 1] += values[maxIndex];
                }

            } else {
                knapsacks[0][maxIndex] = -1;

            }

        }


        //var olan knapsacktan 1 eleman cıkartır

        // Below line determines how many times we are going to try randomly generated string, we selected 10 million randomly generated string, which will gives output at most in 5 minutes
        for (int q = 0; q < 10000000; q++) {

            //Here we created new knapsact to try new strings and not to demolish found by greedy algorithm
            double[][] knapsacksNew = new double[knapsackCount][itemCount + 2];

            //  We copy content of knapsack found by greedy algorithm to knapsacksNew
            for (int i = 0; i < knapsackCount; i++) {
                System.arraycopy(knapsacks[i], 0, knapsacksNew[i], 0, itemCount + 2);
            }

            // In greedy algorith to put -1 tried but not inserted items, now we will convert them to 0 to try again here
            for (int i = 0; i < itemCount; i++) {
                if (knapsacksNew[0][i] != 1) {
                    knapsacksNew[0][i] = 0;
                }
            }

            //  Below for loop count how many item inside the knapsack and put it into itemInside
            int itemInside = 0;
            for (int i = 0; i < itemCount; i++) { //how many item in knapsack
                if (knapsacks[1][i] == 1) {
                    itemInside++;
                }
            }

            // Below we create random number to get how many item from knapsack
            int rand = ((int) Math.floor(Math.random() * itemInside)) / 2;  //how many item will out
            // Below for loop select how which items will be out thanks to random function
            for (int j = 0; j < rand; ) {
                int i = (int) Math.floor(Math.random() * itemCount);
                if (knapsacksNew[0][i] == 1) {
                    j++;
                    for (int n = 0; n < knapsackCount; n++) {   //hangi knapsack oldugu n
                        knapsacksNew[n][i] = 0;
                        knapsacksNew[n][itemCount] -= weights[n][i];
                        knapsacksNew[n][itemCount + 1] -= values[i];
                    }
                }
            }

            //  Below loop tries to put randomly selected item to knapsack, if it is not exceed capacity
            int capacityLimit = 1;
            for (int x = 0; x < itemCount * 5; x++) {

                int rndom = (int) Math.floor(Math.random() * itemCount);
                if (knapsacksNew[0][rndom] == 1) {
                    continue;
                }

                for (int i = 0; i < knapsackCount; i++) {
                    if ((knapsackCapacity[i] < weights[i][rndom] + knapsacksNew[i][itemCount])) {
                        capacityLimit = 0;
                    }
                }

                if (capacityLimit == 1) {
                    for (int i = 0; i < knapsackCount; i++) {
                        knapsacksNew[i][rndom] = 1;
                        knapsacksNew[i][itemCount] += weights[i][rndom];
                        knapsacksNew[i][itemCount + 1] += values[rndom];
                    }

                } else {
                    knapsacksNew[0][rndom] = -1;
                }

            }

            // Above we get items and tried to put newly selected items to knapsacks and here finally we compare knapsacksNew and knapsacks which
            // is generated randomly, if knapsacksNew's value is greater, we will use it in future so, we copy that array to knapsack array
            if (knapsacksNew[0][itemCount + 1] > knapsacks[0][itemCount + 1]) {

                for (int i = 0; i < knapsackCount; i++) {
                    System.arraycopy(knapsacksNew[i], 0, knapsacks[i], 0, itemCount + 2);
                }

            }


        }


        //Below loop writes outputs to output file specified as in homework rules
        FileWriter myWriter = new FileWriter("output4.txt");
        myWriter.write("" + (int) knapsacks[0][itemCount + 1] + "\n");
        for (int i = 0; i < itemCount; i++) {
            if ((int) knapsacks[0][i] == 1) {
                myWriter.write("" + 1 + "\n");
            } else {
                myWriter.write("" + 0 + "\n");
            }
        }
        myWriter.close();



    }

}

