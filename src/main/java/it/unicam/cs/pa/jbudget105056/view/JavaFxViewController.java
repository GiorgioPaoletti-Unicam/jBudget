 package it.unicam.cs.pa.jbudget105056.view;

 import com.google.inject.Guice;
 import it.unicam.cs.pa.jbudget105056.controller.*;
 import it.unicam.cs.pa.jbudget105056.model.*;
 import javafx.application.Application;
 import javafx.beans.property.SimpleBooleanProperty;
 import javafx.beans.property.SimpleDoubleProperty;
 import javafx.beans.property.SimpleIntegerProperty;
 import javafx.beans.property.SimpleStringProperty;
 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.fxml.FXML;
 import javafx.fxml.FXMLLoader;
 import javafx.fxml.Initializable;
 import javafx.scene.Node;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.scene.control.*;
 import javafx.scene.control.cell.PropertyValueFactory;
 import javafx.scene.layout.BorderPane;
 import javafx.scene.layout.GridPane;
 import javafx.stage.Stage;
 import org.controlsfx.control.CheckComboBox;

 import java.io.IOException;
 import java.net.URL;
 import java.time.LocalDate;
 import java.time.ZoneId;
 import java.util.*;
 import java.util.function.Function;

 /**
  * La classe ha responsabilita di costruire l'iterfaccia grafica per l'utente finale
  * Inoltre fa da Controller per il file JavaFxView.fxml costruito con l'uso del programma SceneBuilder
  *
  * La classe e stata implementata tramite l'utilizzo delle JavaFX API
  * Inoltre si e utilizzata la classe CheckComboBox della libreria ControlsFX
  *
  * @author      Giorgio Paoletti
  *              giorgio.paoletti@studenti.unicam.it
  *              matricola: 105056
  *
  * @version     Terza Consegna 18/07/2020
  *
  */
public class JavaFxViewController extends Application implements Initializable {

    private final Controller controller = Guice.createInjector(new ControllerImplementationModule()).getInstance(ControllerImplementation.class);

    private MovementType type;

    //Componenti generali
    @FXML public Label MessaggioErrore;

    //Componenti Account
    @FXML public TableView<Account> AccountTableView;
    @FXML public ChoiceBox<String> AccountTypeChoiceBox;
    @FXML public Spinner<Double> OpeningAccountSpinner;
    @FXML public GridPane AddAccountPane;
    @FXML public TextField DescriptionAccountTextField, NameAccountTextField;
    @FXML public Button OkAccountButton, AddAccountButton, CancelAccountButton, DeleteAccountButton;

    //Componenti Tag
    @FXML public TableView<Tag> TagTableView;
    @FXML public TextField DescrizioneTagTextField, NameTagTextField;
    @FXML public Button  CancelTagButon, DeleteTagButton, AddTagButton, OkTagButon;
    @FXML public GridPane AddTagPane;

    //Componenti Transazioni
    @FXML public TableView<Transaction> TransactionTableView;
    @FXML public Button AddTransactionButton, DeleteTransactionButton;
    @FXML public ChoiceBox<String> AlContoChoiceBox, DalContoChoiceBox;
    @FXML public Spinner<Double> TransactionAmountSpinner;
    @FXML public Spinner<Integer> TransactionOraSpinner, TransactionMinutiSpinner, TransactionSecondiSpinner;
    @FXML public DatePicker TransactionDataPicker;
    @FXML public TextField TransactionDescrizione;
    @FXML public Button TransactionCancelButton, TransactionOkButton;
    @FXML public CheckComboBox<String> TransactionCategoriaCheckComboBox;
    @FXML public Spinner<Double> TransactionCommissioneSpinner;
    @FXML public Label AlContoLabel, DalContoLabel, CommissioneLabel;
    @FXML public Button UscitaButton, EntrataButton, TrasferimentoButton;
    @FXML public BorderPane AddTransactionPane;

    //Componenti Budget
    @FXML public Button EnableAddBudgetPaneButton, DisableAddBudgetPaneButton, AddBudgetButton, DeleteBudgetButton;
    @FXML public ChoiceBox<String> CategoriaChoiceBox;
    @FXML public Spinner<Double> ValueBudgetSpinner;
    @FXML public TableView<Map.Entry<Tag,Double>> BudgetTableView;
    @FXML public GridPane AddBudgetPane;

    //Componenti Report
    @FXML public TableView<Map.Entry<Tag,Double>> ReportTableView;

    //Componenti Scheduled Transaction
    @FXML public TableView<ScheduledTransaction> ScheduledTransactionTableView;
    @FXML public GridPane AddScheduledTransactionAddPane;
    @FXML public TextField ScheduleDescrizione;
    @FXML public ChoiceBox<String> ScheduleAccountChoiceBox;
    @FXML public DatePicker ScheduleFirstDate;
    @FXML public Spinner<Integer> ScheduleNumeroTransaction, ScheduleIntervalloGiorni;
    @FXML public Button ScheduleCancelButton, EnableScheduleTransactionAddPaneButton, ScheduleOkButton, DeleteScheduledTransactionButton;
    @FXML public Spinner<Double> ScheduleValoreTotale;


     public JavaFxViewController() throws IOException {}

     @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/JavaFxView.fxml"));
        stage.setTitle("Jbudget105056");
        stage.setScene(new Scene(root));
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


     /**
      * Il metodo ha la responsabilita dell'inizializzazione dei vari componenti grafici e il caricamento delle istanze del Ledger e Budget
      */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try{ controller.loadData(); }
        catch (Exception e){ e.getStackTrace(); }

        schedule();

        loadTabPaneComponents();
    }

     /**
      * Il metodo si occupa del caricamento dei dati relativi alla lista delle transazioni
      *
      * @param tableView                Table view su cui caricare i dati relativi alle transazioni
      * @param transactionList          lista dalle transazioni da cui vengono presi i dati
      * @param movementView             scelta se visualizzare alla pressione della riga che visualizza la transazione anche una tabella dei relativi movimenti
      * @return                         Table view su vengono caricati i dati relativi alle transazioni da utilizzare, viee utilizzato come nodo nella visualizzazione dele transazioni programmate
      */
    private TableView<Transaction> constructTransactionTable(TableView<Transaction> tableView, List<Transaction> transactionList,  boolean movementView) {

        TableColumn<Transaction, Integer> IdCol = new TableColumn<>("ID");
        IdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Transaction, String> tagsCol = new TableColumn<>("Tags");
        tagsCol.setCellValueFactory(
                (TableColumn.CellDataFeatures<Transaction, String> p) -> {
                    StringBuilder tagNameList = new StringBuilder();
                    for(Tag tag : p.getValue().tags()) {
                        tagNameList.append(tag.getName()).append(" ");
                    }
                    return new SimpleStringProperty(tagNameList.toString());
                }
        );
        TableColumn<Transaction, Date> DateCol = new TableColumn<>("Date");
        DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Transaction, Double> AmountCol = new TableColumn<>("Amount");
        AmountCol.setCellValueFactory(new PropertyValueFactory<>("TotalAmount"));
        tableView.getColumns().setAll(IdCol, DateCol, tagsCol, AmountCol);
        tableView.setItems(FXCollections.observableArrayList(transactionList));

        if(movementView) {
            tableView.setRowFactory(tv -> new TableRow<>() {
                Node detailsPane;

                {
                    this.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            detailsPane = constructMovementTable(getItem().movements(), true);
                            this.getChildren().add(detailsPane);
                        } else this.getChildren().remove(detailsPane);
                        this.requestLayout();
                    });
                }

                @Override
                protected double computePrefHeight(double width) {
                    if (isSelected()) return super.computePrefHeight(width) + detailsPane.prefHeight(60);
                    else return super.computePrefHeight(width);
                }

                @Override
                protected void layoutChildren() {
                    super.layoutChildren();
                    if (isSelected()) {
                        double width = getWidth();
                        double paneHeight = detailsPane.prefHeight(width);
                        detailsPane.resizeRelocate(0, getHeight() - paneHeight, width, paneHeight);
                    }
                }
            });
        }

        return tableView;
    }

     /**
      *  Il metodo si occupa del caricamento dati relativi alla lista degli account nella relativa tabella
      */
    private void constructAccountTable() {

        TableColumn<Account, Integer> IdCol = new TableColumn<>("ID");
        IdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Account, String> NameCol = new TableColumn<>("Name");
        NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Account, String> DescriptionCol = new TableColumn<>("Description");
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Account, Double> AmountCol = new TableColumn<>("Saldo");
        AmountCol.setCellValueFactory(new PropertyValueFactory<>("Balance"));
        TableColumn<Account, AccountType> TypeCol = new TableColumn<>("Tipo");
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        AccountTableView.getColumns().setAll(IdCol, NameCol, DescriptionCol, AmountCol, TypeCol );
        try{ AccountTableView.setItems(FXCollections.observableArrayList(controller.getAccounts())); }
        catch(Exception e) {e.printStackTrace();}

        AccountTableView.setRowFactory(tv -> new TableRow<>() {
            Node detailsPane;
            {
                this.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        detailsPane = constructMovementTable(getItem().getMovements(), false);
                        this.getChildren().add(detailsPane);
                    } else this.getChildren().remove(detailsPane);

                    this.requestLayout();
                });
            }

            @Override
            protected double computePrefHeight(double width) {
                if (isSelected()) return super.computePrefHeight(width) + detailsPane.prefHeight(60);
                else return super.computePrefHeight(width);
            }

            @Override
            protected void layoutChildren() {
                super.layoutChildren();
                if (isSelected()) {
                    double width = getWidth();
                    double paneHeight = detailsPane.prefHeight(width);
                    detailsPane.resizeRelocate(0, getHeight() - paneHeight, width, paneHeight);
                }
            }
        });
    }


     /**
      *
      * Il metodo si occupa di costruire la tebella realativa alla lista dei movimenti
      */
    private TableView<Movement> constructMovementTable(List<Movement> movementList, boolean accountName) {

        TableView<Movement> MovementTable = new TableView<>();
        TableColumn<Movement, Integer> IdCol = new TableColumn<>("ID");
        IdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Movement, String> DescriptionCol = new TableColumn<>("Description");
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        TableColumn<Movement, Double> AmountCol = new TableColumn<>("Amount");
        AmountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        MovementTable.getColumns().addAll(IdCol, DescriptionCol, AmountCol);
        if(accountName) {
            TableColumn<Movement, String> accountCol = new TableColumn<>("Account");
            accountCol.setCellValueFactory(
                    (TableColumn.CellDataFeatures<Movement, String> p) ->
                            new SimpleStringProperty(p.getValue().getAccount().getName())
            );
            MovementTable.getColumns().addAll(accountCol);
        }
        MovementTable.setItems(FXCollections.observableArrayList(movementList));
        MovementTable.setPrefHeight(50+(movementList.size()*30));
        return MovementTable;

    }

     /**
      * il metodo si occupa di costruire la tabella relativa alla lista delle categorie
      */
    private void constructTagTable() {

        TableColumn<Tag, Integer> IdCol = new TableColumn<>("ID");
        IdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Tag, String> NameCol = new TableColumn<>("Name");
        NameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        TableColumn<Tag, String> DescriptionCol = new TableColumn<>("Description");
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        TagTableView.getColumns().setAll(IdCol, NameCol, DescriptionCol);
        TagTableView.setItems(FXCollections.observableArrayList(controller.getTags()));
    }

     /**
      * si occupa di caricare tutte le specifiche dei componenti che compongono i vari tab
      */
    private void loadTabPaneComponents(){
        loadAccountTabComponents();
        loadTransactionTabComponents();
        loadTagTabComponents();
        loadBudgetTabComponents();
        loadReportTabComponents();
        loadScheduledTransactionComponents();
    }

     /**
      * Metodo parametrizzato che opermette di caricare in una choice box i nomi di una lista di oggetti data
      *
      * @param choiceBox    su cui caricare i nomi
      * @param list         da cui prendere i nomi
      * @param function     riferimento al metodo per recuperare il nome
      * @param <T>          classe degli oggeti contenuti nella lista
      */
    private <T> void loadChoiceBox(ChoiceBox<String> choiceBox, List<T> list, Function<T, String> function){
        List<String> nameList = new ArrayList<>();
        for (T t : list) nameList.add(function.apply(t));
        choiceBox.setItems(FXCollections.observableList(nameList));
    }

     /**
      *
      * Metodo parametrizzato che opermette di caricare in una check choice box i nomi di una lista di oggetti data
      *
      * @param checkComboBox    su cui caricare i nomi
      * @param list             da cui prendere i nomi
      * @param function         riferimento al metodo per recuperare il nome
      * @param <T>              classe degli oggeti contenuti nella lista
      */
    private <T> void loadCheckComboBox(CheckComboBox<String> checkComboBox, List<T> list, Function<T, String> function){
        List<String> nameList = new ArrayList<>();
        for (T t : list) nameList.add(function.apply(t));
        checkComboBox.getItems().clear();
        checkComboBox.getItems().addAll(FXCollections.observableList(nameList));
    }

     /**
      * si occupa di caricare i componentirelativi al tab che gestisce gli account
      */
    private void loadAccountTabComponents() {
        try{ constructAccountTable(); } catch(Exception e) { e.printStackTrace(); }

        loadChoiceBox(AccountTypeChoiceBox, Arrays.asList(AccountType.values()), AccountType::name);
        OpeningAccountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 999999999.99, 0.00, 0.01));
    }

     /**
      * si occupa di caricare i componentirelativi al tab che gestisce le transazioni
      */
    private void loadTransactionTabComponents() {
        try{ constructTransactionTable(TransactionTableView, controller.getTransactions(), true);}
        catch (Exception e) { e.printStackTrace(); }
        try{
            loadChoiceBox(AlContoChoiceBox, controller.getAccounts(), Account::getName);
            loadChoiceBox(DalContoChoiceBox, controller.getAccounts(), Account::getName);
            loadCheckComboBox(TransactionCategoriaCheckComboBox, controller.getTags(), Tag::getName);
        }catch (Exception e) { e.printStackTrace(); }
        TransactionAmountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 999999999.99, 0.00, 0.01));
        TransactionOraSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0, 1));
        TransactionMinutiSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 1));
        TransactionSecondiSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 1));
        TransactionDataPicker.setValue(LocalDate.now());
        TransactionCommissioneSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 999999999.99, 0.00, 0.01));
        disableRightButton(false, true, false);
        type = MovementType.DEBIT;
    }

     /**
      * si occupa di caricare i componentirelativi al tab che gestisce le categorie
      */
    private void loadTagTabComponents() {
        try { constructTagTable(); } catch (Exception e) { e.printStackTrace(); }
    }

    public void EnableAddAccountPane() {
        disableEnableAddPane(null, AddAccountPane, AddAccountButton, DeleteAccountButton, false);
    }

    public void DisableAddAccountPane() {
        disableEnableAddPane(null, AddAccountPane, AddAccountButton, DeleteAccountButton, true);
    }

     /**
      * il metodo si occupa di aggiungere un account prendendo i dti dai componenti grafici opportuni
      */
    public void AddAccount() {

        if(AccountTypeChoiceBox.getSelectionModel().isEmpty() || DescriptionAccountTextField.getText().equals("") || NameAccountTextField.getText().equals(""))
            MessaggioErrore.setText("Sono presenti campi vuoti");
        else{
            try{
                controller.addAccount(NameAccountTextField.getText(), DescriptionAccountTextField.getText(), OpeningAccountSpinner.getValue(), AccountType.valueOf(AccountTypeChoiceBox.getValue()));
                MessaggioErrore.setText("");
                loadTabPaneComponents();
                controller.saveData();
            }
            catch (Exception e) { MessaggioErrore.setText(e.getMessage()); }
        }
        AddAccountPane.disableProperty().setValue(true);
        AddAccountButton.disableProperty().setValue(false);
        DeleteAccountButton.disableProperty().setValue(false);
    }

     /**
      * il metodo si occupa di eliminare l'account selezionato nella tabella opportuna
      */
    public void DeleteSelectedAccountOnTableView() {
        try {
            controller.removeAccount(controller.getAccounts(a -> a.getID() == AccountTableView.getSelectionModel().getSelectedItem().getID()).get(0));
            loadTabPaneComponents();
            controller.saveData();
        }catch (Exception e) { MessaggioErrore.setText("Seleziona un elemento"); }
    }

    public void DisableAddTagPane() {
        disableEnableAddPane(null, AddTagPane, AddTagButton, DeleteTagButton, true);
    }

     /**
      * il metodo si occupa di aggiungere una categoria prendendo i dati dai componenti grafici opportuni
      */
    public void AddTag() {
        if(DescrizioneTagTextField.getText().equals("") || NameTagTextField.getText().equals(""))
            MessaggioErrore.setText("Sono presenti campi vuoti");
        else{
            try{
                controller.addTag(NameTagTextField.getText(), DescrizioneTagTextField.getText());
                MessaggioErrore.setText("");
                loadTabPaneComponents();
                controller.saveData();
            }
            catch (Exception e) { MessaggioErrore.setText(e.getMessage()); }
        }
        disableEnableAddPane(null, AddTagPane, AddTagButton, DeleteTagButton, true);
    }

    public void EnableAddTagPane() {
        disableEnableAddPane(null, AddTagPane, AddTagButton, DeleteTagButton, false);
    }

     /**
      * il metodo si occupa di eliminare la categoria selezionata nella tabella opportuna
      */
    public void DeleteTag() {
        //TODO cosa succede se elimino un tag?
    }

    public void EnableAddTrasactionTabPane() {
        disableEnableAddPane(AddTransactionPane, null, AddTransactionButton, DeleteTransactionButton, false);
    }

     /**
      * il metodo si occupa di aggiungere una categoria prendendo i dati dai componenti grafici opportuni
      * a secondo del punzante sezionato verra inserita un'entrata o una spesa o un trasferimento
      */
    public void AddTransaction() {
        if(AlContoChoiceBox.getSelectionModel().isEmpty() ||
                TransactionDescrizione.getText().equals("") ||
                TransactionCategoriaCheckComboBox.getItems().isEmpty() ||
                TransactionDataPicker.getValue().equals(LocalDate.now()) ||
                (DalContoLabel.visibleProperty().getValue().equals(true) && (DalContoChoiceBox.getSelectionModel().isEmpty() || TransactionCommissioneSpinner.getValue().equals(0.00)))
        ) MessaggioErrore.setText("Sono presenti campi vuoti");
        else{
            Date date = Date.from(TransactionDataPicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            date.setHours(TransactionOraSpinner.getValue());
            date.setMinutes(TransactionMinutiSpinner.getValue());
            date.setSeconds(TransactionSecondiSpinner.getValue());
            try {
                if(DalContoLabel.visibleProperty().getValue().equals(true)){
                    controller.addTrasferimento(controller.getAccounts(a -> a.getName().equals(DalContoChoiceBox.getValue())).get(0),
                            controller.getAccounts(a -> a.getName().equals(AlContoChoiceBox.getValue())).get(0),
                            TransactionAmountSpinner.getValue(), TransactionDescrizione.getText(), TransactionCommissioneSpinner.getValue(),
                            tagCheckComboBoxToTagList(TransactionCategoriaCheckComboBox), date
                    );
                }
                else{
                    controller.addTransaction(controller.getAccounts(a -> a.getName().equals(AlContoChoiceBox.getValue())).get(0),
                            type, TransactionAmountSpinner.getValue(),
                            tagCheckComboBoxToTagList(TransactionCategoriaCheckComboBox),
                            date, TransactionDescrizione.getText()
                    );
                }
                MessaggioErrore.setText("");
                loadTabPaneComponents();
                controller.saveData();
            } catch (Exception e) {
                MessaggioErrore.setText(e.getMessage());
            }
        }
        disableEnableAddPane(AddTransactionPane, null, AddTransactionButton, DeleteTransactionButton, true);
    }

     /**
      * Il metodo preleva le categorie selezioante dalla CheckComboBox e le traforma in una lista
      */
    private List<Tag> tagCheckComboBoxToTagList(CheckComboBox<String> checkComboBox){
        List<Tag> tagList = new ArrayList<>();
        for(String s: new ArrayList<>(checkComboBox.getCheckModel().getCheckedItems()))
            tagList.add(controller.getTags(t -> t.getName().equals(s)).get(0));
        return tagList;
    }

     /**
      * il metodo si occupa di eliminare la transazione selezionata nella tabella opportuna
      */
    public void DeleteTransaction() {
        try {
            controller.removeTransaction(controller.getTransactions(a -> a.getID() == TransactionTableView.getSelectionModel().getSelectedItem().getID()).get(0));
            loadTabPaneComponents();
            controller.saveData();
        }catch (Exception e) { MessaggioErrore.setText("Seleziona un elemento"); }
    }

    public void DisableAddTransactionTabPane() {
        disableEnableAddPane(AddTransactionPane, null, AddTransactionButton, DeleteTransactionButton, true);
    }

    private void disableEnableAddPane(BorderPane borderPane, GridPane gridPane, Button addButton, Button deleteButton, boolean value){
        if(borderPane != null) borderPane.disableProperty().setValue(value);
        if(gridPane != null) gridPane.disableProperty().setValue(value);
        addButton.disableProperty().setValue(!value);
        deleteButton.disableProperty().setValue(!value);
    }

     /**
      * Il metodo si occupa di setare i componenti grafici opportuni alla pressione dei tasti nel pannello di aggiunta delle transazioni
      */
    private void setCorrectArgument(String string){
        DalContoChoiceBox.visibleProperty().setValue(false);
        DalContoLabel.visibleProperty().setValue(false);
        TransactionCommissioneSpinner.visibleProperty().setValue(false);
        CommissioneLabel.visibleProperty().setValue(false);
        AlContoLabel.setText("Conto:");
        switch(string){
            case "entrata":
                type = MovementType.CREDITS;
                disableRightButton(true, false, false);
                break;

            case "uscita" :
                type = MovementType.DEBIT;
                disableRightButton(false, true, false);
                break;
            case "trasferimento":
                DalContoChoiceBox.visibleProperty().setValue(true);
                DalContoLabel.visibleProperty().setValue(true);
                TransactionCommissioneSpinner.visibleProperty().setValue(true);
                CommissioneLabel.visibleProperty().setValue(true);
                AlContoLabel.setText("Al Conto:");
                disableRightButton(false, false, true);
                break;
        }
    }

    private void disableRightButton(Boolean entrataButton, Boolean uscitaButton, Boolean trasferimentoButton){
        EntrataButton.disableProperty().setValue(entrataButton);
        UscitaButton.disableProperty().setValue(uscitaButton);
        TrasferimentoButton.disableProperty().setValue(trasferimentoButton);
    }

    public void SelectEntrataArgument() {
        setCorrectArgument("entrata");
    }

    public void SelectSpesaArgument() {
        setCorrectArgument("uscita");
    }

    public void SelectTrasferimentoArgument() {
        setCorrectArgument("trasferimento");
    }

     /**
      * si occupa di caricare i componenti relativi al tab che gestisce i budget
      */
    private void loadBudgetTabComponents() throws IllegalStateException {
        try { constructBudgetReportTable(BudgetTableView, controller.getBudgets(), "Expected Value"); } catch (Exception e) { e.printStackTrace(); }
        try { loadChoiceBox(CategoriaChoiceBox, controller.getTags(), Tag::getName); }
        catch (IllegalStateException e) { e.printStackTrace(); }
        ValueBudgetSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 999999999.99, 0.00, 0.01));

    }

     /**
      * si occupa di caricare i componentirelativi al tab che gestisce i report
      */
    private void loadReportTabComponents(){
        try { constructBudgetReportTable(ReportTableView, controller.getReportBudgets().report(), "Reported Value"); }
        catch (IllegalStateException e) { MessaggioErrore.setText("Report Table is Empty because Transaction Table or Budgets are Empty"); }
    }

     /**
      *  Il metodo si occupa del caricamento dati relativi alla lista dei budget o report nella relativa tabella
      */
    private void constructBudgetReportTable(TableView<Map.Entry<Tag,Double>> tableView, Map<Tag, Double> map, String s) {
        ObservableList<Map.Entry<Tag, Double>> budgetList = FXCollections.observableArrayList(map.entrySet());
        if(!budgetList.isEmpty()) tableView.setItems(budgetList);

        TableColumn<Map.Entry<Tag,Double>,Integer> IdCol = new TableColumn<>("ID");
        IdCol.setCellValueFactory(
                (TableColumn.CellDataFeatures<Map.Entry<Tag, Double>, Integer> p) ->
                        new SimpleIntegerProperty(p.getValue().getKey().getID()).asObject()
        );

        TableColumn<Map.Entry<Tag,Double>, String> NameCol = new TableColumn<>("Name");
        NameCol.setCellValueFactory(
                (TableColumn.CellDataFeatures<Map.Entry<Tag, Double>, String> p) ->
                        new SimpleStringProperty(p.getValue().getKey().getName())
        );

        TableColumn<Map.Entry<Tag,Double>, String> DescriptionCol = new TableColumn<>("Description");
        DescriptionCol.setCellValueFactory(
                (TableColumn.CellDataFeatures<Map.Entry<Tag, Double>, String> p) ->
                        new SimpleStringProperty(p.getValue().getKey().getDescription())
        );

        TableColumn<Map.Entry<Tag,Double>, Double> ValueCol = new TableColumn<>(s);
        ValueCol.setCellValueFactory(
                (TableColumn.CellDataFeatures<Map.Entry<Tag, Double>, Double> p) ->
                        new SimpleDoubleProperty(p.getValue().getValue()).asObject()
        );

        TableColumn<Map.Entry<Tag,Double>, String> TagCol = new TableColumn<>("Categoria");
        TagCol.getColumns().addAll(IdCol,NameCol, DescriptionCol);
        tableView.getColumns().clear();
        tableView.getColumns().addAll(TagCol, ValueCol);
    }

     /**
      * il metodo si occupa di aggiungere un budget prendendo i dati dai componenti grafici opportuni
      */
    public void AddBudget() {
        if(CategoriaChoiceBox.getSelectionModel().isEmpty()) MessaggioErrore.setText("Sono presenti campi vuoti");
        else{
            try{
                controller.addBudget(controller.getTags(t -> t.getName().equals(CategoriaChoiceBox.getValue())).get(0), ValueBudgetSpinner.getValue());
                MessaggioErrore.setText("");
                loadTabPaneComponents();
                controller.saveData();
            }
            catch (Exception e) { MessaggioErrore.setText(e.getMessage()); }
        }
        disableEnableAddPane(null, AddBudgetPane, EnableAddBudgetPaneButton, DeleteBudgetButton, true);
    }

    public void DisableAddBudgetPane() {
        disableEnableAddPane(null, AddBudgetPane, EnableAddBudgetPaneButton, DeleteBudgetButton, true);
    }

    public void EnableAddBudgetPane() {
        disableEnableAddPane(null, AddBudgetPane, EnableAddBudgetPaneButton, DeleteBudgetButton, false);
    }

     /**
      * il metodo si occupa di eliminare il budget selezionato nella tabella opportuna
      */
    public void DeleteBudget() {
        try {
            controller.removeBudget(controller.getTags(t -> t.getID() == BudgetTableView.getSelectionModel().getSelectedItem().getKey().getID()).get(0));
            loadTabPaneComponents();
            controller.saveData();
        }catch (Exception e) { MessaggioErrore.setText("Seleziona un elemento"); }
    }

     /**
      *  Il metodo si occupa del caricamento dati relativi alla lista delle transazioni programmate nella relativa tabella
      */
    private void constructScheduledTransactionTable() throws IllegalStateException, NullPointerException {
        TableColumn<ScheduledTransaction, Integer> IdCol = new TableColumn<>("ID");
        IdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<ScheduledTransaction, String> DescriptionCol = new TableColumn<>("Description");
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        TableColumn<ScheduledTransaction, String> AccountCol = new TableColumn<>("Account");
        AccountCol.setCellValueFactory(
                (TableColumn.CellDataFeatures<ScheduledTransaction, String> p) ->
                        new SimpleStringProperty(p.getValue().getAccount().getName())
        );
        TableColumn<ScheduledTransaction, Boolean> IsCompletedCol = new TableColumn<>("Completed");
        IsCompletedCol.setCellValueFactory(
                (TableColumn.CellDataFeatures<ScheduledTransaction, Boolean> p) ->
                        new SimpleBooleanProperty(p.getValue().isCompleted())
        );
        ScheduledTransactionTableView.getColumns().setAll(IdCol, DescriptionCol/*, AccountCol*/, IsCompletedCol);
        ScheduledTransactionTableView.setItems(FXCollections.observableArrayList(controller.getScheduledTransactions()));

        ScheduledTransactionTableView.setRowFactory(tv -> new TableRow<>() {
            Node detailsPane;
            {
                this.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        detailsPane = constructTransactionTable(new TableView<>(), getItem().getTransactions(), false);
                        this.getChildren().add(detailsPane);
                    } else this.getChildren().remove(detailsPane);

                    this.requestLayout();
                });
            }

            @Override
            protected double computePrefHeight(double width) {
                if (isSelected()) return super.computePrefHeight(width) + detailsPane.prefHeight(60);
                else return super.computePrefHeight(width);
            }

            @Override
            protected void layoutChildren() {
                super.layoutChildren();
                if (isSelected()) {
                    double width = getWidth();
                    double paneHeight = detailsPane.prefHeight(width);
                    detailsPane.resizeRelocate(0, getHeight() - paneHeight, width, paneHeight);
                }
            }
        });
    }

     /**
      * si occupa di caricare i componentirelativi al tab che gestisce le transazioni programmate
      */
    private void loadScheduledTransactionComponents() {
        try{ constructScheduledTransactionTable(); }
        catch (Exception e){ e.printStackTrace(); }
        try{ loadChoiceBox(ScheduleAccountChoiceBox, controller.getAccounts(), Account::getName); }
        catch (IllegalStateException | NullPointerException e){ e.printStackTrace(); }
        ScheduleValoreTotale.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, 999999999.99, 0.00, 0.01));
        ScheduleNumeroTransaction.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 100, 1, 1));
        ScheduleIntervalloGiorni.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1, 1));
        ScheduleFirstDate.setValue(LocalDate.now());
    }

     /**
      * si occupa di schedulare le transazioni programmate
      */
    private void schedule(){
        controller.schedule();
        controller.saveData();
    }

     /**
      * il metodo si occupa di aggiungere una transazione programmata prendendo i dati dai componenti grafici opportuni
      */
    public void AddScheduleTransaction() {
        if(ScheduleAccountChoiceBox.getSelectionModel().isEmpty() || ScheduleDescrizione.getText().isEmpty() || ScheduleValoreTotale.getValue().equals(0.00))
            MessaggioErrore.setText("Sono presenti campi vuoti o valore totale uguale a 0");
        else{
            try{
                Date date = Date.from(ScheduleFirstDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                controller.addScheduledTransaction(ScheduleDescrizione.getText(),
                        controller.getAccounts(a -> a.getName().equals(ScheduleAccountChoiceBox.getValue())).get(0),
                        ScheduleNumeroTransaction.getValue(), ScheduleIntervalloGiorni.getValue(),
                        date, ScheduleValoreTotale.getValue()
                );
                MessaggioErrore.setText("");
                loadTabPaneComponents();
                controller.saveData();
            }
            catch (Exception e) { MessaggioErrore.setText(e.getMessage()); }
        }
        disableEnableAddPane(null, AddScheduledTransactionAddPane, EnableScheduleTransactionAddPaneButton, DeleteScheduledTransactionButton, true);
    }

    public void DisableScheduleAddPane() {
        disableEnableAddPane(null, AddScheduledTransactionAddPane, EnableScheduleTransactionAddPaneButton, DeleteScheduledTransactionButton, true);
    }

    public void EnableScheduleAddPane() {
        disableEnableAddPane(null, AddScheduledTransactionAddPane, EnableScheduleTransactionAddPaneButton, DeleteScheduledTransactionButton, false);
    }

     /**
      * il metodo si occupa di eliminare la transazione programmata selezionata nella tabella opportuna
      */
    public void DeleteScheduleTransaction() {
        try {
            controller.removeScheduledTransaction(controller.getScheduledTransactions(st -> st.getID() == ScheduledTransactionTableView.getSelectionModel().getSelectedItem().getID()).get(0));
            loadTabPaneComponents();
            controller.saveData();
        }catch (Exception e) { MessaggioErrore.setText("Seleziona un elemento"); }
    }
}
