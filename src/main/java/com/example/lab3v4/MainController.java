package com.example.lab3v4;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {
    private Database db = new Database();
    @FXML
    private ListView<String>  ListView1;
    @FXML
    private TextField FindIndexField;
    @FXML
    private Label FindValueLabel;
    @FXML
    private TextField DeleteIndexField;
    @FXML
    private Label DeleteLabel;
    @FXML
    private TextField EditIndexField;
    @FXML
    private TextField EditValueField;
    @FXML
    private TextField AddIndexField;
    @FXML
    private TextField AddValueField;
    @FXML
    private Label EditLabel;
    @FXML
    private Label AddLabel;
    @FXML
    public void setDatabase() throws IOException {
        db.init();
        db.printArray();
        db.setSparseArray();
        db.printSparse();
        db.setDenseArray();
        db.printDense();
        db.printOverflow();
        setListView1(db.getBaseArr());
       // System.out.println(db.Search(43).getValue());

    }
    public void setListView1(ArrayList<Item> arrList){
        ListView1.getItems().clear();
        for(int i=0; i<arrList.size(); i++){
            String item ="";
            item += arrList.get(i).getIndex() + " " + arrList.get(i).getValue();
            ListView1.getItems().add(item);
        }
    }
    public void findIndex(){
        if(FindIndexField.getText().matches("[0-9]+")){
           int index = Integer.parseInt(FindIndexField.getText());
           int indexInDense = db.binarySearch(index);
           if(indexInDense!=-1)FindValueLabel.setText("Value " + db.getBaseArr().get(indexInDense).getValue());
           else  FindValueLabel.setText("Not found ");
        }else{
            FindValueLabel.setText("Set valid id ");
        }
    }
    public void deleteIndex(){
        if(DeleteIndexField.getText().matches("[0-9]+")){
            int index = Integer.parseInt(DeleteIndexField.getText());
            boolean isDeleted = db.deleteElement(index);
            if(isDeleted) {
                DeleteLabel.setText("Element was deleted");
                setListView1(db.getBaseArr());
            }
            else  DeleteLabel.setText("Not found");
        }else{
            DeleteLabel.setText("Set valid id");
        }
    }
    public void editElement(){
        if(EditIndexField.getText().matches("[0-9]+")&&EditValueField.getText().matches("[0-9]+")){
            int index = Integer.parseInt(EditIndexField.getText());
            int value = Integer.parseInt(EditValueField.getText());
            boolean isEdited = db.editElement(index, value);
            if(isEdited){
                EditLabel.setText("Element was edited");
                setListView1(db.getBaseArr());
            }else EditLabel.setText("Not found");
        }else{
          EditLabel.setText("Set valid data");
        }
    }
    public void addElement(){
        if(AddIndexField.getText().matches("[0-9]+")&&AddValueField.getText().matches("[0-9]+")){
            int index = Integer.parseInt(AddIndexField.getText());
            int value = Integer.parseInt(AddValueField.getText());
            int isAdded = db.addElement(index, value);
            if(isAdded==2){
                AddLabel.setText("Element was added");
                setListView1(db.getBaseArr());
            }else if(isAdded==1)AddLabel.setText("Added in overflow");
            else AddLabel.setText("Already exists");
        }else{
            AddLabel.setText("Set valid data");
        }
    }
    public void exit() throws IOException{
        if(!db.getBaseArr().isEmpty())db.writeFile();
        Platform.exit();
        System.exit(0);
    }

}