package com.example.lab3v4;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Database {
    private ArrayList<Item> baseArr = new ArrayList<>();
    private ArrayList<Item> sparseArray = new ArrayList<>();
    private ArrayList<Item> denseArray = new ArrayList<>();
    private ArrayList<Item> overflowArray = new ArrayList<>();
    private int maxBaseSize = 5000;
    public void init() throws IOException {
        File data = new File("src/main/resources/com/example/lab3v4/data.txt");
        if(data.length()!=0) readFile();
        else{
        ArrayList<Integer> usedIndex = new ArrayList<>();
        for(int i=0; i<5000; i++){
            Random random = new Random();
            int value = random.nextInt(1,   10000);
            int index = 0;
            while(usedIndex.contains(index)) index = random.nextInt(0, 5000);
            usedIndex.add(index);
            Item element = new Item();
            element.setValue(value);
            element.setIndex(index);
            baseArr.add(element);
        }
        }
    }
    public ArrayList<Item> getBaseArr(){
        return denseArray;
    }
    public void printArray(){
        System.out.println("Database");
        String line = "";
        for(int i=0; i<denseArray.size(); i++){
            line = denseArray.get(i).getIndex() + " value " + denseArray.get(i).getValue();
            System.out.println(line);
        }
    }
    public void printSparse(){
        System.out.println("Sparse array");
        String row ="";
        for(int i=0; i<sparseArray.size();i++){

            row += " " + sparseArray.get(i).getIndex();

        }
        System.out.println(row);
    }
    public void printDense(){
        System.out.println("Dense array");
        String row ="";
        for(int i=0; i<denseArray.size();i++){

            row += " " + denseArray.get(i).getIndex();

        }
        System.out.println(row);
    }
    public void setSparseArray(){
        ArrayList<ArrayList<Integer>> arrays = indexes();
        ArrayList<Integer> indexesArr = arrays.get(0);
        ArrayList<Integer> values = arrays.get(1);
        for(int i=0; i<=getMaxIndex(indexesArr); i++){
            if(indexesArr.contains(i)){
                int elementNumber = indexesArr.indexOf(i);
                int elementValue = values.get(elementNumber);
                Item el = new Item();
                el.setIndex(i);
                el.setValue(elementValue);
                if(calculateElements()<maxBaseSize)sparseArray.add(el);
                else overflowArray.add(el);
            }else{
                if(calculateElements()<maxBaseSize){
                Item el = new Item();
                el.setIndex(-1);
                el.setValue(0);
                sparseArray.add(el);
                }
            }

        }
    }
    public int calculateElements(){
        int elements = 0;
        for(int i=0; i<sparseArray.size(); i++){
            if(sparseArray.get(i).getIndex()!=-1)elements++;
        }
        return elements;
    }
    public ArrayList<ArrayList<Integer>> indexes(){
        ArrayList<Integer> indexList = new ArrayList<>();
        ArrayList<Integer> valuesList = new ArrayList<>();
        for(int i=0; i<baseArr.size(); i++){
          indexList.add(baseArr.get(i).getIndex());
          valuesList.add(baseArr.get(i).getValue());
        }
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        result.add(indexList);
        result.add(valuesList);
        return result;
    }
    public int getMaxIndex(ArrayList<Integer> arrIndex){
        int max = 0;
        for(int i=0; i<arrIndex.size(); i++){
            if(arrIndex.get(i)>max) max = arrIndex.get(i);
        }
        return max;
    }
    public void setDenseArray(){
        denseArray.clear();
        for(int i=0; i<sparseArray.size(); i++){
            if(sparseArray.get(i).getIndex()!=-1){
                denseArray.add(sparseArray.get(i));
            }
        }
    }
    public int binarySearch(int index){
        int currentIndex;
        int f = 0;
        int c = denseArray.size()-1;
        int counter = 0;
        while(f<=c){
            currentIndex = f+(c-f)/2;
            int valueIndex = denseArray.get(currentIndex).getIndex();
            if(valueIndex==index){
                System.out.println("Found with "+ counter + " steps");
                return currentIndex;
            }
            else if(valueIndex>index){
                c = currentIndex-1;
                counter++;
            }
            else{
                f=currentIndex+1;
                counter++;
            }
        }
        currentIndex = -1;
        return currentIndex;
    }
    public boolean deleteElement(int index){
        boolean isDeleted = false;
        Item deletedElement = new Item();
        deletedElement.setIndex(-1);
        deletedElement.setValue(0);
        if((sparseArray.size()>index)&&sparseArray.get(index).getIndex()!=-1){
            sparseArray.set(index, deletedElement);
            if(!overflowArray.isEmpty()) addOverflow();
            setDenseArray();
            isDeleted = true;
        }
        return isDeleted;
    }
    public void addOverflow() {
        int sizeSparse = calculateElements();
        int smallestIndexInOverflow = 0;
        int smallestIndex = Integer.MAX_VALUE;
        for (int i = 0; i < overflowArray.size(); i++) {
            if (overflowArray.get(i).getIndex() < smallestIndex) {
                smallestIndexInOverflow = i;
                smallestIndex = overflowArray.get(i).getIndex();
            }
        }
        if (sizeSparse < maxBaseSize) {
            if (sparseArray.size() <= overflowArray.get(smallestIndexInOverflow).getIndex()) {
                System.out.println(overflowArray.get(smallestIndexInOverflow).getIndex());
                int j = overflowArray.get(smallestIndexInOverflow).getIndex();
                for (int i = sparseArray.size(); i < j; i++) {
                    Item item = new Item();
                    item.setValue(0);
                    item.setIndex(-1);
                    sparseArray.add(i, item);
                }
                sparseArray.add(overflowArray.get(smallestIndexInOverflow).getIndex(), overflowArray.get(smallestIndexInOverflow));
            }else {
                sparseArray.set(overflowArray.get(smallestIndexInOverflow).getIndex(), overflowArray.get(smallestIndexInOverflow));
                overflowArray.remove(smallestIndexInOverflow);
                sizeSparse++;
            }

        }

    }

    public boolean editElement(int index, int value){
        boolean isEdited = false;
        if(index<sparseArray.size()){
        if(sparseArray.get(index).getIndex()!=-1){
        Item editedElement = new Item();
        editedElement.setValue(value);
        editedElement.setIndex(index);
        sparseArray.set(index, editedElement);
        int indexDense = binarySearch(index);
        denseArray.set(indexDense, editedElement);
        isEdited = true;
        }
        }
        return isEdited;
    }
    public void printOverflow(){
        String arr="";
        for(int i=0; i<overflowArray.size(); i++){
            arr+= overflowArray.get(i).getIndex() + " ";
        }
        System.out.println(arr);
    }
    public int addElement(int index, int value){
        int isAdded = 0;
        if(binarySearch(index)==-1&&!isInOverflow(index)){
            int elementsSparse = calculateElements();
            Item addItem = new Item();
            addItem.setIndex(index);
            addItem.setValue(value);
            if(elementsSparse<maxBaseSize){
                if(index>=sparseArray.size()){
                    for(int i=sparseArray.size(); i<index; i++){
                        Item emptyItem = new Item();
                        emptyItem.setIndex(-1);
                        emptyItem.setValue(0);
                        sparseArray.add(i, emptyItem);
                    }
                    sparseArray.add(index, addItem);
                }else{
                    sparseArray.set(index,addItem);
                }
                setDenseArray();
                isAdded = 2;
            }else{
                overflowArray.add(addItem);
                isAdded = 1;
            }
        }
        return isAdded;
    }
    public boolean isInOverflow(int index){
        boolean isInOverflow = false;
        for (int i=0; i<overflowArray.size(); i++) {
            if(overflowArray.get(i).getIndex()==index){
                isInOverflow = true;
                break;
            }
        }
        return isInOverflow;
    }
    public void writeFile() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/com/example/lab3v4/data.txt"));
        for(int i=0; i<denseArray.size(); i++){
            writer.write(denseArray.get(i).getIndex()+" "+denseArray.get(i).getValue() + "\n");
        }
        for(int i=0; i<overflowArray.size(); i++){
            writer.write(overflowArray.get(i).getIndex()+" "+overflowArray.get(i).getValue()+ "\n");
        }
        writer.close();
    }
    public void readFile()throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/lab3v4/data.txt"));
        String line;
        while ((line=reader.readLine())!=null){
            String[] data = line.split(" ");
            Item element = new Item();
            element.setValue(Integer.parseInt(data[1]));
            element.setIndex(Integer.parseInt(data[0]));
            baseArr.add(element);
        }
        reader.close();
    }
}
